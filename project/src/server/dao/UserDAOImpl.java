package server.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import server.model.Student;
import server.model.Tutor;
import server.model.User;

public class UserDAOImpl implements UserDAO {
    private static UserDAOImpl instance;

    private UserDAOImpl() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }

    public static synchronized UserDAOImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=studybuddy", "postgres",
                "password");
    }

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

    @Override
    public void delete(User user) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        }
    }

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

    @Override
    public void declineTutor(int tutorId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM tutors WHERE user_id = ?")) {
            statement.setInt(1, tutorId);
            statement.executeUpdate();
        }
    }

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


  @Override public User readByEmail(String email) throws SQLException
  {
    String sql = "SELECT u.id, u.first_name, u.last_name, u.date_of_birth, " +
        "u.email, u.phone, u.password, u.gender, u.user_type, " + "t.education_level " +
        "FROM users u " + "LEFT JOIN tutors t ON u.id = t.user_id " + "WHERE u.email = ?";

    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql))
    {
      statement.setString(1, email);
      ResultSet rs = statement.executeQuery();

      if(rs.next())
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

        if("tutor".equalsIgnoreCase(type))
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
