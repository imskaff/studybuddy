package server.dao;

import server.model.Session;
import server.model.Student;
import server.model.Tutor;
import server.model.User;

import java.sql.*;
import java.time.LocalDateTime;
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
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=studybuddy", "postgres",
                "password");
    }

    @Override
    public Session create(String description, double price, String subject, String location, LocalDateTime start_date,
            LocalDateTime end_date, Tutor tutor, Student student) throws SQLException {

        // Look up subject_id from name (nullable)
        Integer subjectId = findSubjectId(subject);

        String sql = "INSERT INTO sessions (description, price, location, start_date, end_date, tutor_id, student_id, subject_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, description);
            statement.setDouble(2, price);
            statement.setString(3, location);
            statement.setObject(4, start_date);
            statement.setObject(5, end_date);
            statement.setInt(6, tutor.getId());
            if (student != null) {
                statement.setInt(7, student.getId());
            } else {
                statement.setNull(7, Types.INTEGER);
            }
            if (subjectId != null) {
                statement.setInt(8, subjectId);
            } else {
                statement.setNull(8, Types.INTEGER);
            }
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                return new Session(id, description, price, subject, location, start_date, end_date, tutor, student);
            } else {
                throw new SQLException("No keys generated");
            }
        }
    }

    @Override
    public Session readByInt(int id) throws SQLException {
        String sql = "SELECT s.id, s.description, s.price, s.location, s.start_date, s.end_date, "
                + "s.tutor_id, s.student_id, sub.name AS subject_name "
                + "FROM sessions s LEFT JOIN subjects sub ON s.subject_id = sub.id "
                + "WHERE s.id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRow(resultSet);
            } else {
                throw new SQLException("No session found with that id");
            }
        }
    }

    @Override
    public void update(Session session) throws SQLException {
        Integer subjectId = findSubjectId(session.getSubject());

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE sessions SET description = ?, price = ?, location = ?, start_date = ?, end_date = ?, tutor_id = ?, student_id = ?, subject_id = ? WHERE id = ?")) {
            statement.setString(1, session.getDescription());
            statement.setDouble(2, session.getPrice());
            statement.setString(3, session.getLocation());
            statement.setObject(4, session.getStart_date());
            statement.setObject(5, session.getEnd_date());
            statement.setInt(6, session.getTutor().getId());
            if (session.getStudent() != null) {
                statement.setInt(7, session.getStudent().getId());
            } else {
                statement.setNull(7, Types.INTEGER);
            }
            if (subjectId != null) {
                statement.setInt(8, subjectId);
            } else {
                statement.setNull(8, Types.INTEGER);
            }
            statement.setInt(9, session.getId());
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
        String sql = "SELECT s.id, s.description, s.price, s.location, s.start_date, s.end_date, "
                + "s.tutor_id, s.student_id, sub.name AS subject_name "
                + "FROM sessions s LEFT JOIN subjects sub ON s.subject_id = sub.id";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Session> sessions = new ArrayList<>();
            while (resultSet.next()) {
                sessions.add(mapRow(resultSet));
            }
            return sessions;
        }
    }

    @Override
    public List<Session> readAvailable() throws SQLException {
        String sql = "SELECT s.id, s.description, s.price, s.location, s.start_date, s.end_date, "
                + "s.tutor_id, s.student_id, sub.name AS subject_name "
                + "FROM sessions s LEFT JOIN subjects sub ON s.subject_id = sub.id "
                + "WHERE s.student_id IS NULL";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Session> sessions = new ArrayList<>();
            while (resultSet.next()) {
                sessions.add(mapRow(resultSet));
            }
            return sessions;
        }
    }

    @Override
    public List<Session> readByStudent(int studentId) throws SQLException {
        String sql = "SELECT s.id, s.description, s.price, s.location, s.start_date, s.end_date, "
                + "s.tutor_id, s.student_id, sub.name AS subject_name "
                + "FROM sessions s LEFT JOIN subjects sub ON s.subject_id = sub.id "
                + "WHERE s.student_id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            List<Session> sessions = new ArrayList<>();
            while (resultSet.next()) {
                sessions.add(mapRow(resultSet));
            }
            return sessions;
        }
    }

    private Session mapRow(ResultSet resultSet) throws SQLException {
        int sessionId = resultSet.getInt("id");
        String description = resultSet.getString("description");
        double price = resultSet.getDouble("price");
        String location = resultSet.getString("location");
        LocalDateTime start_date = resultSet.getObject("start_date", LocalDateTime.class);
        LocalDateTime end_date = resultSet.getObject("end_date", LocalDateTime.class);
        int tutorId = resultSet.getInt("tutor_id");
        int studentId = resultSet.getInt("student_id");
        boolean studentWasNull = resultSet.wasNull();
        String subject = resultSet.getString("subject_name");

        UserDAOImpl userDAO = UserDAOImpl.getInstance();
        Tutor tutor = (Tutor) userDAO.readByInt(tutorId);
        Student student = null;
        if (!studentWasNull) {
            student = (Student) userDAO.readByInt(studentId);
        }
        return new Session(sessionId, description, price, subject, location, start_date, end_date, tutor, student);
    }

    // sessions table stores subject_id
    // this method finds subject_id by name in subjects table
    // so when creating or updating a session, it converts subject name to subject_id
    // used by create() and update() methods in this class
    private Integer findSubjectId(String subjectName) throws SQLException {
        if (subjectName == null || subjectName.isEmpty()) {
            return null;
        }
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT id FROM subjects WHERE name = ?")) {
            statement.setString(1, subjectName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return null;
        }
    }
}
