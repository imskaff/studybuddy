package dao;

import model.Session;
import model.Student;
import model.Tutor;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SessionDAOImpl implements SessionDAO {
    private static SessionDAOImpl instance;

    private SessionDAOImpl() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }

    public static synchronized SessionDAOImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new SessionDAOImpl();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2", "postgres",
                "password");
    }

    @Override
    public Session create(String description, double price, String location, DateTimeFormatter start_date,
            DateTimeFormatter end_date, Tutor tutor, Student student) throws SQLException {

        String sql = "INSERT INTO sessions (description, price, location, start_date, end_date, tutor_id, student_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, description);
            statement.setDouble(2, price);
            statement.setString(3, location);
            statement.setObject(4, start_date);
            statement.setObject(5, end_date);
            statement.setInt(6, tutor.getId());
            statement.setInt(7, student.getId());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                return new Session(id, description, price, location, start_date, end_date, tutor, student);
            } else {
                throw new SQLException("No keys generated");
            }
        }
    }

    @Override
    public Session readByInt(int id) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM sessions WHERE id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int sessionId = resultSet.getInt(1);
                String description = resultSet.getString(2);
                double price = resultSet.getDouble(3);
                String location = resultSet.getString(4);
                DateTimeFormatter start_date = resultSet.getObject(5, DateTimeFormatter.class);
                DateTimeFormatter end_date = resultSet.getObject(6, DateTimeFormatter.class);
                Tutor tutor = resultSet.getObject(7, Tutor.class);
                Student student = resultSet.getObject(8, Student.class);
                return new Session(sessionId, description, price, location, start_date, end_date, tutor, student);
            } else {
                throw new SQLException("No session found with that id");
            }
        }
    }

    @Override
    public void update(Session session) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE sessions SET description = ?, price = ?, location = ?, start_date = ?, end_date = ?, tutor_id = ?, student_id = ? WHERE id = ?")) {
            statement.setString(1, session.getDescription());
            statement.setDouble(2, session.getPrice());
            statement.setString(3, session.getLocation());
            statement.setObject(4, session.getStart_date());
            statement.setObject(5, session.getEnd_date());
            statement.setInt(6, session.getTutor().getId());
            statement.setInt(7, session.getStudent().getId());
            statement.setInt(8, session.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Session session) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM sessions WHERE id = ?")) {
            statement.setInt(1, session.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public List<Session> readAll() throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM sessions")) {
            ResultSet resultSet = statement.executeQuery();
            List<Session> sessions = new ArrayList<>();
            while (resultSet.next()) {
                int sessionId = resultSet.getInt(1);
                String description = resultSet.getString(2);
                double price = resultSet.getDouble(3);
                String location = resultSet.getString(4);
                DateTimeFormatter start_date = resultSet.getObject(5, DateTimeFormatter.class);
                DateTimeFormatter end_date = resultSet.getObject(6, DateTimeFormatter.class);
                Tutor tutor = resultSet.getObject(7, Tutor.class);
                Student student = resultSet.getObject(8, Student.class);
                sessions.add(
                        new Session(sessionId, description, price, location, start_date, end_date, tutor, student));
            }
            return sessions;
        }
    }
}
