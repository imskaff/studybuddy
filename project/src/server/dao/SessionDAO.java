package server.dao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

/**
 * Data Access Object interface for {@link Session} persistence.
 * 
 * Defines all database operations related to tutoring sessions,
 * including CRUD operations and specialised queries used by the business layer.
 * 
 *
 * @author
 */
public interface SessionDAO {

    /**
     * Inserts a new session record into the database and returns the persisted entity.
     *
     * @param description the session description
     * @param price       the session price
     * @param subject     the subject name (resolved to an ID internally)
     * @param location    the session location
     * @param start_date  the start date and time
     * @param end_date    the end date and time
     * @param tutor       the tutor who owns the session
     * @param student     the student who booked the session, or {@code null} if unbooked
     * @return the newly created {@link Session} with its generated database ID
     * @throws SQLException if the insert fails
     */
    Session create(String description, double price, String subject, String location, LocalDateTime start_date,
                   LocalDateTime end_date, Tutor tutor, Student student) throws SQLException;

    /**
     * Fetches a single session by its database ID.
     *
     * @param id the session ID to look up
     * @return the matching {@link Session}
     * @throws SQLException if no session is found or a database error occurs
     */
    Session readByInt(int id) throws SQLException;

    /**
     * Persists all mutable fields of the given session to the database.
     *
     * @param session the session to update (identified by its ID)
     * @throws SQLException if the update fails
     */
    void update(Session session) throws SQLException;

    /**
     * Deletes the given session from the database.
     *
     * @param session the session to delete (identified by its ID)
     * @throws SQLException if the deletion fails
     */
    void delete(Session session) throws SQLException;

    /**
     * Returns all sessions in the database regardless of status.
     *
     * @return a list of all sessions
     * @throws SQLException if the query fails
     */
    List<Session> readAll() throws SQLException;

    /**
     * Returns all sessions that are currently available for booking.
     * Only sessions with no assigned student, an {@code Available} status,
     * and a future end date are included.
     *
     * @return a list of available sessions
     * @throws SQLException if the query fails
     */
    List<Session> readAvailable() throws SQLException;

    /**
     * Returns all sessions booked by the student with the given ID.
     *
     * @param studentId the ID of the student
     * @return a list of sessions booked by that student
     * @throws SQLException if the query fails
     */
    List<Session> readByStudent(int studentId) throws SQLException;

    /**
     * Returns all sessions owned by the tutor with the given ID.
     *
     * @param tutorId the ID of the tutor
     * @return a list of sessions belonging to that tutor
     * @throws SQLException if the query fails
     */
    List<Session> readByTutor(int tutorId) throws SQLException;

    /**
     * Returns all subject names from the {@code subjects} table, ordered alphabetically.
     *
     * @return a list of subject names
     * @throws SQLException if the query fails
     */
    List<String> readSubjects() throws SQLException;
}
