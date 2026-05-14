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
            String password, String gender, ArrayList<String> languages, String type) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO app_user (first_name, last_name, date_of_birth, email, phone, password, gender, languages, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, first_name);
            statement.setString(2, last_name);
            statement.setObject(3, date_of_birth);
            statement.setString(4, email);
            statement.setString(5, phone);
            statement.setString(6, password);
            statement.setString(7, gender);
            statement.setObject(8, languages);
            statement.setString(9, type);
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
          if(keys.next())
          {
            int id = keys.getInt(1);

            // This create() method is used for creating student users.
            // Tutor users are created in createTutor(),
            // because tutors need extra inserts into tutor and tutor_request tables.
            return new Student(first_name, last_name, date_of_birth,
                email, phone, password, gender, languages,
                id, type);
          }
          else {
            throw new SQLException("No keys generated");
          }
        }
    }

  @Override public Tutor createTutor(Tutor tutor) throws SQLException
  {
    try(Connection connection = getConnection()) {
      // Inserts common user information into app_user
      PreparedStatement userStatement = connection.prepareStatement(
              "INSERT INTO app_user (first_name, last_name, date_of_birth, email, phone, password, gender, languages, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
              PreparedStatement.RETURN_GENERATED_KEYS);

      userStatement.setString(1, tutor.getFirst_name());
      userStatement.setString(2, tutor.getLast_name());
      userStatement.setObject(3, tutor.getDate_of_birth());
      userStatement.setString(4, tutor.getEmail());
      userStatement.setString(5, tutor.getPhone());
      userStatement.setString(6, tutor.getPassword());
      userStatement.setString(7, tutor.getGender());
      userStatement.setObject(8, tutor.getLanguages());
      userStatement.setString(9, "Tutor");

      userStatement.executeUpdate();

      ResultSet userKeys = userStatement.getGeneratedKeys();

      if(!userKeys.next())
      {
        throw new SQLException("No user keys generated");
      }
      // This is the generated ID from app_user
      int userId = userKeys.getInt(1);
      // Inserts tutor-specific information into tutor table
      PreparedStatement tutorStatement =
          connection.prepareStatement(
              "INSERT INTO tutor (user_id, education_level) VALUES (?, ?)",
              PreparedStatement.RETURN_GENERATED_KEYS);

      tutorStatement.setInt(1, userId);
      tutorStatement.setString(2, tutor.getEducation_level());

      tutorStatement.executeUpdate();

      ResultSet tutorKeys = tutorStatement.getGeneratedKeys();

      if(!tutorKeys.next())
      {
        throw new SQLException("No tutor keys generated");
      }

      // This is the generated ID from tutor table
      int tutorId = tutorKeys.getInt(1);

      // Creates a pending tutor registration request
      // status is automatically set to 'pending' by the database
      PreparedStatement requestStatement = connection.prepareStatement(
              "INSERT INTO tutor_request (tutor_id) VALUES (?)");

      requestStatement.setInt(1, tutorId);
      requestStatement.executeUpdate();

      // Returns the created Tutor object
      return new Tutor(tutor.getFirst_name(), tutor.getLast_name(), tutor.getDate_of_birth(),
          tutor.getEmail(), tutor.getPhone(), tutor.getPassword(),
          tutor.getGender(), tutor.getLanguages(), tutor.getEducation_level(),
          "Tutor", userId);
    }
  }

  @Override
  public boolean updateTutorRequestStatus(int userId, String status) throws SQLException
  {
    String sql =
        "UPDATE tutor_request " +
            "SET status = ?, resolved_at = NOW() " +
            "WHERE tutor_id = (" +
            "SELECT id FROM tutor WHERE user_id = ?) " +
            "AND status = 'pending'";

    try(Connection connection = getConnection();
        PreparedStatement statement =
            connection.prepareStatement(sql))
    {
      statement.setString(1, status);
      statement.setInt(2, userId);

      return statement.executeUpdate() > 0;
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
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE users SET first_name = ?, last_name = ?, date_of_birth = ?, email = ?, phone = ?, password = ?, gender = ?, languages = ?, type = ? WHERE id = ?")) {
            statement.setString(1, user.getFirst_name());
            statement.setString(2, user.getLast_name());
            statement.setObject(3, user.getDate_of_birth());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getPassword());
            statement.setString(7, user.getGender());
            statement.setObject(8, user.getLanguages());
            statement.setString(9, user.getType());
            statement.setInt(10, user.getId());
            statement.executeUpdate();
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
                + "u.password, u.gender, u.user_type, t.education_level "
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
                return new Tutor(first_name, last_name, dob, em, phone, pwd, gender, languages,
                        educationLevel != null ? educationLevel : "N/A", type, id);
            }
            return new Student(first_name, last_name, dob, em, phone, pwd, gender, languages, id, type);
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
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM users")) {
            ResultSet resultSet = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                int userId = resultSet.getInt(1);
                String first_name = resultSet.getString(2);
                String last_name = resultSet.getString(3);
                LocalDate date_of_birth = resultSet.getObject(4, LocalDate.class);
                String email = resultSet.getString(5);
                String phone = resultSet.getString(6);
                String password = resultSet.getString(7);
                String gender = resultSet.getString(8);
                ArrayList<String> languages = resultSet.getObject(9, ArrayList.class);
                String type = resultSet.getString(10);
                if ("Tutor".equalsIgnoreCase(type)) {
                    users.add(new Tutor(first_name, last_name, date_of_birth, email, phone, password, gender, languages,
                            "N/A", type, userId));
                } else {
                    users.add(new Student(first_name, last_name, date_of_birth, email, phone, password, gender,
                            languages, userId, type));
                }
            }
            return users;
        }
    }
}
