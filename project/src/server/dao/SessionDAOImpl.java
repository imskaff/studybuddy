package server.dao;

import server.model.Available;
import server.model.Book;
import server.model.Cancel;
import server.model.Complete;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * PostgreSQL implementation of {@link SessionDAO}.
 * Each method opens its own connection and closes it in a try-with-resources block.
 * Uses an injected {@link UserDAO} to load the tutor and student for each session row.
 *
 * @author
 */
public class SessionDAOImpl implements SessionDAO {
    private final UserDAO userDAO;

    /**
     * Constructs a SessionDAOImpl and registers the PostgreSQL JDBC driver.
     *
     * @param userDAO the DAO used to load tutor and student objects
     * @throws SQLException if the JDBC driver cannot be registered
     */
    public SessionDAOImpl(UserDAO userDAO) throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
        this.userDAO = userDAO;
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
     * Inserts a new session into the database and returns the created object.
     *
     * @param description the session description
     * @param price       the session price
     * @param subject     the subject name
     * @param location    the session location
     * @param start_date  the start date and time
     * @param end_date    the end date and time
     * @param tutor       the tutor who owns the session
     * @param student     the student (may be {@code null})
     * @return the created {@link Session} with its generated ID
     * @throws SQLException if the insert fails
     */
    @Override
    public Session create(String description, double price, String subject, String location, LocalDateTime start_date,
            LocalDateTime end_date, Tutor tutor, Student student) throws SQLException {

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

    /**
     * Reads a session by ID from the database.
     *
     * @param id the session ID
     * @return the matching {@link Session}
     * @throws SQLException if the session is not found or a query error occurs
     */
    @Override
    public Session readByInt(int id) throws SQLException {
        String sql = "SELECT s.id, s.description, s.price, s.location, s.start_date, s.end_date, "
                + "s.tutor_id, s.student_id, s.status, sub.name AS subject_name "
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

    /**
     * Saves the updated session data to the database.
     *
     * @param session the session to update
     * @throws SQLException if the update fails
     */
    @Override
    public void update(Session session) throws SQLException {
        Integer subjectId = findSubjectId(session.getSubject());

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE sessions SET description = ?, price = ?, location = ?, start_date = ?, end_date = ?, tutor_id = ?, student_id = ?, subject_id = ?, status = ? WHERE id = ?")) {
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
            statement.setString(9, session.getStatus());
            statement.setInt(10, session.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Deletes the given session from the database.
     *
     * @param session the session to delete
     * @throws SQLException if the delete fails
     */
    @Override
    public void delete(Session session) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM sessions WHERE id = ?")) {
            statement.setInt(1, session.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Returns all sessions from the database.
     *
     * @return list of all sessions
     * @throws SQLException if the query fails
     */
    @Override
    public List<Session> readAll() throws SQLException {
        String sql = "SELECT s.id, s.description, s.price, s.location, s.start_date, s.end_date, "
                + "s.tutor_id, s.student_id, s.status, sub.name AS subject_name "
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

    /**
     * Returns sessions that have no assigned student, are in Available status,
     * and whose end date is in the future.
     *
     * @return list of available sessions
     * @throws SQLException if the query fails
     */
    @Override
    public List<Session> readAvailable() throws SQLException {
        String sql = "SELECT s.id, s.description, s.price, s.location, s.start_date, s.end_date, "
                + "s.tutor_id, s.student_id, s.status, sub.name AS subject_name "
                + "FROM sessions s LEFT JOIN subjects sub ON s.subject_id = sub.id "
                + "WHERE s.student_id IS NULL AND s.status = 'Available' AND s.end_date > NOW()";
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

    /**
     * Returns all sessions booked by the given student.
     *
     * @param studentId the student's ID
     * @return list of the student's booked sessions
     * @throws SQLException if the query fails
     */
    @Override
    public List<Session> readByStudent(int studentId) throws SQLException {
        String sql = "SELECT s.id, s.description, s.price, s.location, s.start_date, s.end_date, "
                + "s.tutor_id, s.student_id, s.status, sub.name AS subject_name "
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

    /**
     * Returns all sessions belonging to the given tutor.
     *
     * @param tutorId the tutor's ID
     * @return list of the tutor's sessions
     * @throws SQLException if the query fails
     */
    @Override
    public List<Session> readByTutor(int tutorId) throws SQLException {
        String sql = "SELECT s.id, s.description, s.price, s.location, s.start_date, s.end_date, "
                + "s.tutor_id, s.student_id, s.status, sub.name AS subject_name "
                + "FROM sessions s LEFT JOIN subjects sub ON s.subject_id = sub.id "
                + "WHERE s.tutor_id = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, tutorId);
            ResultSet resultSet = statement.executeQuery();
            List<Session> sessions = new ArrayList<>();
            while (resultSet.next()) {
                sessions.add(mapRow(resultSet));
            }
            return sessions;
        }
    }

    /**
     * Converts the current result set row into a {@link Session} object.
     * Loads the tutor and student via the injected {@link UserDAO} and
     * restores the correct state based on the persisted status string.
     *
     * @param resultSet the result set positioned on the row to map
     * @return the mapped {@link Session}
     * @throws SQLException if a database or type-cast error occurs
     */
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
        String status = resultSet.getString("status");

        Tutor tutor = (Tutor) userDAO.readByInt(tutorId);
        Student student = null;
        if (!studentWasNull) {
            student = (Student) userDAO.readByInt(studentId);
        }
        Session session = new Session(sessionId, description, price, subject, location, start_date, end_date, tutor, student);
        if (status != null) {
            switch (status) {
                case "Cancel":   session.setState(new Cancel());   break;
                case "Booked":   session.setState(new Book());     break;
                case "Complete": session.setState(new Complete()); break;
                default:         session.setState(new Available()); break;
            }
        }
        return session;
    }

    /**
     * Returns all subject names from the database.
     *
     * @return list of subject names
     * @throws SQLException if the query fails
     */
    @Override
    public List<String> readSubjects() throws SQLException {
        String sql = "SELECT name FROM subjects ORDER BY name";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<String> subjects = new ArrayList<>();
            while (rs.next()) subjects.add(rs.getString("name"));
            return subjects;
        }
    }

    /**
     * Looks up a subject name in the database and returns its ID.
     * Returns {@code null} if the name is blank or not found.
     *
     * @param subjectName the subject name to look up
     * @return the subject ID, or {@code null} if not found
     * @throws SQLException if the query fails
     */
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
