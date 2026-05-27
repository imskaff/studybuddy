package server.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import server.model.Student;
import server.model.Tutor;
import server.model.User;

/**
 * PostgreSQL implementation of {@link UserDAO}.
 * Implemented as a thread-safe Singleton. Each public method opens its own
 * connection and closes it in a try-with-resources block.
 * Language records are stored in the {@code user_languages} join table,
 * and tutor-specific data lives in a separate {@code tutors} table.
 *
 * @author
 */
public class UserDAOImpl implements UserDAO {
    private static UserDAOImpl instance;

    /**
     * Private constructor that registers the PostgreSQL JDBC driver.
     *
     * @throws SQLException if the driver cannot be registered
     */
    private UserDAOImpl() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }

    /**
     * Returns the singleton instance, creating it if necessary.
     *
     * @return the singleton {@code UserDAOImpl} instance
     * @throws SQLException if the instance cannot be created
     */
    public static synchronized UserDAOImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }

    /**
     * Opens and returns a new database connection.
     *
     * @return a new {@link Connection}
     * @throws SQLException if the connection cannot be established
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=studybuddy", "postgres",
                "password");
    }

    /**
     * Inserts a new user into the database and returns the created object.
     * Also inserts language entries and a tutor or student row depending on the type.
     *
     * @param first_name     first name
     * @param last_name      last name
     * @param date_of_birth  date of birth
     * @param email          email address
     * @param phone          phone number
     * @param password       password
     * @param gender         gender
     * @param languages      list of spoken languages
     * @param type           {@code "Student"} or {@code "Tutor"}
     * @param educationLevel education level (only used for tutors)
     * @return the created {@link User}
     * @throws SQLException if the insert fails
     */
    @Override
    public User create(String first_name, String last_name, LocalDate date_of_birth, String email, String phone,
            String password, String gender, ArrayList<String> languages, String type, String educationLevel) throws SQLException {
        try (Connection connection = getConnection()) {
            int id;
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (first_name, last_name, date_of_birth, email, phone, password, gender, user_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, first_name);
                statement.setString(2, last_name);
                statement.setObject(3, date_of_birth);
                statement.setString(4, email);
                statement.setString(5, phone);
                statement.setString(6, password);
                statement.setString(7, gender);
                statement.setString(8, type.toLowerCase());
                statement.executeUpdate();
                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    id = keys.getInt(1);
                } else {
                    throw new SQLException("No keys generated");
                }
            }
            try (PreparedStatement ins = connection.prepareStatement(
                    "INSERT INTO user_languages (user_id, language) VALUES (?, ?)")) {
                for (String lang : languages) {
                    if (!lang.isBlank()) {
                        ins.setInt(1, id);
                        ins.setString(2, lang.trim());
                        ins.executeUpdate();
                    }
                }
            }
            if ("Tutor".equalsIgnoreCase(type)) {
                try (PreparedStatement tutorIns = connection.prepareStatement(
                        "INSERT INTO tutors (user_id, education_level) VALUES (?, ?)")) {
                    tutorIns.setInt(1, id);
                    tutorIns.setString(2, educationLevel != null ? educationLevel : "");
                    tutorIns.executeUpdate();
                }
                return new Tutor(first_name, last_name, date_of_birth, email, phone,
                    password, gender, languages, educationLevel != null ? educationLevel : "", type, id);
            } else {
                try (PreparedStatement studentIns = connection.prepareStatement(
                        "INSERT INTO students (user_id) VALUES (?)")) {
                    studentIns.setInt(1, id);
                    studentIns.executeUpdate();
                }
                return new Student(first_name, last_name, date_of_birth, email, phone,
                    password, gender, languages, id, type);
            }
        }
    }

    /**
     * Reads a user by ID from the database.
     *
     * @param id the user's ID
     * @return the matching {@link User}
     * @throws SQLException if no user is found or the query fails
     */
    @Override
    public User readByInt(int id) throws SQLException {
        String sql = "SELECT u.id, u.first_name, u.last_name, u.date_of_birth, u.email, u.phone, "
                + "u.password, u.gender, u.user_type, t.education_level "
                + "FROM users u LEFT JOIN tutors t ON u.id = t.user_id "
                + "WHERE u.id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                LocalDate date_of_birth = rs.getObject("date_of_birth", LocalDate.class);
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                String gender = rs.getString("gender");
                String type = rs.getString("user_type");
                String educationLevel = rs.getString("education_level");
                ArrayList<String> languages = readLanguages(connection, userId);
                if ("tutor".equalsIgnoreCase(type)) {
                    return new Tutor(first_name, last_name, date_of_birth, email, phone, password, gender, languages,
                            educationLevel != null ? educationLevel : "N/A", type, userId);
                }
                return new Student(first_name, last_name, date_of_birth, email, phone, password, gender, languages,
                        userId, type);
            } else {
                throw new SQLException("No user found with that id");
            }
        }
    }

    /**
     * Updates the user's data in the database.
     * Replaces all language entries by deleting existing rows and re-inserting them.
     * Also updates the tutors table if the user is a {@link Tutor}.
     *
     * @param user the user to update
     * @throws SQLException if the update fails
     */
    @Override
    public void update(User user) throws SQLException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET first_name = ?, last_name = ?, date_of_birth = ?, email = ?, phone = ?, password = ?, gender = ?, user_type = ? WHERE id = ?")) {
                statement.setString(1, user.getFirst_name());
                statement.setString(2, user.getLast_name());
                statement.setObject(3, user.getDate_of_birth());
                statement.setString(4, user.getEmail());
                statement.setString(5, user.getPhone());
                statement.setString(6, user.getPassword());
                statement.setString(7, user.getGender());
                statement.setString(8, user.getType().toLowerCase());
                statement.setInt(9, user.getId());
                statement.executeUpdate();
            }
            try (PreparedStatement del = connection.prepareStatement(
                    "DELETE FROM user_languages WHERE user_id = ?")) {
                del.setInt(1, user.getId());
                del.executeUpdate();
            }
            try (PreparedStatement ins = connection.prepareStatement(
                    "INSERT INTO user_languages (user_id, language) VALUES (?, ?)")) {
                for (String lang : user.getLanguages()) {
                    ins.setInt(1, user.getId());
                    ins.setString(2, lang.trim());
                    ins.executeUpdate();
                }
            }
            if (user instanceof Tutor) {
                try (PreparedStatement tutorStmt = connection.prepareStatement(
                        "UPDATE tutors SET education_level = ? WHERE user_id = ?")) {
                    tutorStmt.setString(1, ((Tutor) user).getEducation_level());
                    tutorStmt.setInt(2, user.getId());
                    tutorStmt.executeUpdate();
                }
            }
        }
    }

    /**
     * Deletes the user from the database.
     *
     * @param user the user to delete
     * @throws SQLException if the delete fails
     */
    @Override
    public void delete(User user) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Looks up a user by email and password.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return the matching {@link User}, or {@code null} if not found
     * @throws SQLException if the query fails
     */
    @Override
    public User readByEmailAndPassword(String email, String password) throws SQLException {
        String sql = "SELECT u.id, u.first_name, u.last_name, u.date_of_birth, u.email, u.phone, "
                + "u.password, u.gender, u.user_type, t.education_level, t.is_approved "
                + "FROM users u LEFT JOIN tutors t ON u.id = t.user_id "
                + "WHERE u.email = ? AND u.password = ?";
        try (Connection connection = getConnection();
             PreparedStatement s = connection.prepareStatement(sql)) {
            s.setString(1, email);
            s.setString(2, password);
            ResultSet rs = s.executeQuery();
            if (!rs.next()) return null;
            int id = rs.getInt("id");
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            LocalDate dob = rs.getObject("date_of_birth", LocalDate.class);
            String em = rs.getString("email");
            String phone = rs.getString("phone");
            String pwd = rs.getString("password");
            String gender = rs.getString("gender");
            String type = rs.getString("user_type");
            String educationLevel = rs.getString("education_level");
            ArrayList<String> languages = readLanguages(connection, id);
            if ("tutor".equalsIgnoreCase(type)) {
                Tutor tutor = new Tutor(first_name, last_name, dob, em, phone, pwd, gender, languages,
                        educationLevel != null ? educationLevel : "N/A", type, id);
                tutor.setApproved(rs.getBoolean("is_approved"));
                return tutor;
            }
            return new Student(first_name, last_name, dob, em, phone, pwd, gender, languages, id, type);
        }
    }

    /**
     * Returns all tutors that are not yet approved.
     *
     * @return list of pending tutors
     * @throws SQLException if the query fails
     */
    @Override
    public ArrayList<Tutor> readPendingTutors() throws SQLException {
        String sql = "SELECT u.id, u.first_name, u.last_name, u.date_of_birth, u.email, u.phone, "
                + "u.password, u.gender, u.user_type, t.education_level "
                + "FROM users u JOIN tutors t ON u.id = t.user_id "
                + "WHERE t.is_approved = FALSE";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            ArrayList<Tutor> tutors = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                Tutor tutor = new Tutor(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getObject("date_of_birth", LocalDate.class),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getString("gender"),
                        readLanguages(connection, id),
                        rs.getString("education_level") != null ? rs.getString("education_level") : "N/A",
                        rs.getString("user_type"),
                        id);
                tutor.setApproved(false);
                tutors.add(tutor);
            }
            return tutors;
        }
    }

    /**
     * Updates the tutor's approval status and records which admin approved them.
     *
     * @param tutorId  the tutor's ID
     * @param approved the new approval status
     * @param adminId  the ID of the admin who made the decision
     * @throws SQLException if the update fails
     */
    @Override
    public void updateApprovalStatus(int tutorId, boolean approved, int adminId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE tutors SET is_approved = ?, approved_by = ? WHERE user_id = ?")) {
            statement.setBoolean(1, approved);
            statement.setInt(2, adminId);
            statement.setInt(3, tutorId);
            statement.executeUpdate();
        }
    }

    /**
     * Removes the tutor record from the tutors table (decline action).
     *
     * @param tutorId the tutor's ID
     * @throws SQLException if the delete fails
     */
    @Override
    public void declineTutor(int tutorId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM tutors WHERE user_id = ?")) {
            statement.setInt(1, tutorId);
            statement.executeUpdate();
        }
    }

    /**
     * Returns the admin's ID for the given username and password, or {@code -1} if not found.
     *
     * @param username the admin's username
     * @param password the admin's password
     * @return the admin ID, or {@code -1}
     * @throws SQLException if the query fails
     */
    @Override
    public int getAdminId(String username, String password) throws SQLException {
        String sql = "SELECT id FROM administrators WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement s = connection.prepareStatement(sql)) {
            s.setString(1, username);
            s.setString(2, password);
            ResultSet rs = s.executeQuery();
            return rs.next() ? rs.getInt("id") : -1;
        }
    }

    /**
     * Looks up a user by email address.
     *
     * @param email the email address to search for
     * @return the matching {@link User}, or {@code null} if not found
     * @throws SQLException if the query fails
     */
    @Override public User readByEmail(String email) throws SQLException
    {
        String sql = "SELECT u.id, u.first_name, u.last_name, u.date_of_birth, " +
            "u.email, u.phone, u.password, u.gender, u.user_type, " + "t.education_level " +
            "FROM users u " + "LEFT JOIN tutors t ON u.id = t.user_id " + "WHERE u.email = ?";

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if (rs.next())
            {
                int id = rs.getInt("id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                LocalDate dob = rs.getObject("date_of_birth", LocalDate.class);
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                String gender = rs.getString("gender");
                String type = rs.getString("user_type");
                String educationLevel = rs.getString("education_level");

                ArrayList<String> languages = readLanguages(connection, id);

                if ("tutor".equalsIgnoreCase(type))
                {
                    return new Tutor(first_name, last_name, dob, email, phone, password, gender, languages,
                        educationLevel != null ? educationLevel : "N/A", type, id);
                }
                return new Student(first_name, last_name, dob, email, phone, password,
                    gender, languages, id, type);
            }
            return null;
        }
    }

    /**
     * Updates the tutor's {@code is_approved} flag.
     *
     * @param tutorId  the tutor's ID
     * @param approved the new approval value
     * @return {@code true} if a row was updated
     * @throws SQLException if the update fails
     */
    @Override public boolean updateTutorApproval(int tutorId, boolean approved) throws SQLException
    {
        String sql = "UPDATE tutors SET is_approved = ? WHERE user_id = ?";

        try (Connection connection = getConnection();
              PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setBoolean(1, approved);
            statement.setInt(2, tutorId);
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Fetches all languages spoken by the given user from the {@code user_languages} table.
     *
     * @param connection the active connection to reuse
     * @param userId     the user's ID
     * @return list of language strings
     * @throws SQLException if the query fails
     */
    private ArrayList<String> readLanguages(Connection connection, int userId) throws SQLException {
        ArrayList<String> languages = new ArrayList<>();
        try (PreparedStatement s = connection.prepareStatement(
                "SELECT language FROM user_languages WHERE user_id = ?")) {
            s.setInt(1, userId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) languages.add(rs.getString("language"));
        }
        return languages;
    }

    /**
     * Returns all users from the database.
     *
     * @return list of all users
     * @throws SQLException if the query fails
     */
    @Override
    public List<User> readAll() throws SQLException {
        String sql = "SELECT u.id, u.first_name, u.last_name, u.date_of_birth, u.email, u.phone, "
                + "u.password, u.gender, u.user_type, t.education_level "
                + "FROM users u LEFT JOIN tutors t ON u.id = t.user_id";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                int userId = rs.getInt("id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                LocalDate date_of_birth = rs.getObject("date_of_birth", LocalDate.class);
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                String gender = rs.getString("gender");
                String type = rs.getString("user_type");
                String educationLevel = rs.getString("education_level");
                ArrayList<String> languages = readLanguages(connection, userId);
                if ("tutor".equalsIgnoreCase(type)) {
                    users.add(new Tutor(first_name, last_name, date_of_birth, email, phone, password, gender, languages,
                            educationLevel != null ? educationLevel : "N/A", type, userId));
                } else {
                    users.add(new Student(first_name, last_name, date_of_birth, email, phone, password, gender,
                            languages, userId, type));
                }
            }
            return users;
        }
    }
}
