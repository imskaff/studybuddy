package server.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the server-side business-logic contract for the tutoring application.
 * 
 * Implemented by {@link ModelManager}. All operations that can be requested
 * by a connected client (via {@code ClientHandler}) must go through this interface.
 * 
 *
 * @author
 */
public interface SessionModel {

    /**
     * Returns all sessions that are currently available for booking (not in the past,
     * not already booked).
     *
     * @return a list of available sessions
     */
    List<Session> getAvailableSessions();

    /**
     * Returns all sessions booked by the student with the given ID.
     *
     * @param studentId the ID of the student
     * @return a list of sessions booked by that student
     */
    List<Session> getBookedSessions(int studentId);

    /**
     * Returns all sessions owned by the tutor with the given ID.
     *
     * @param tutorId the ID of the tutor
     * @return a list of sessions belonging to that tutor
     */
    List<Session> getTutorSessions(int tutorId);

    /**
     * Returns all tutors whose registration is pending administrator approval.
     *
     * @return a list of pending tutors
     */
    List<Tutor> getPendingTutors();

    /**
     * Looks up an administrator by credentials and returns their ID.
     *
     * @param email    the administrator's username or email
     * @param password the administrator's password
     * @return the administrator's ID, or {@code -1} if credentials do not match
     */
    int getAdminId(String email, String password);

    /**
     * Approves or declines a pending tutor registration, recording the
     * administrator who performed the action.
     *
     * @param tutorId the ID of the tutor to process
     * @param action  {@code "APPROVE"} or {@code "DECLINE"}
     * @param adminId the ID of the administrator performing the action
     * @return {@code true} if the action succeeded; {@code false} otherwise
     */
    boolean handleTutorRegistration(int tutorId, String action, int adminId);

    /**
     * Adds a session to the in-memory list.
     *
     * @param student   the student involved (may be {@code null} for available sessions)
     * @param tutor     the tutor who owns the session
     * @param sessionId the session to add
     */
    void bookSession(Student student, Tutor tutor, Session sessionId);

    /**
     * Returns all sessions currently held in the in-memory list.
     *
     * @return all sessions
     */
    ArrayList<Session> getAllSessions();

    /**
     * Returns the session at the given index in the in-memory list.
     *
     * @param index the zero-based index
     * @return the session at that index
     */
    Session getSession(int index);

    /**
     * Removes the session with the given ID from the in-memory list.
     *
     * @param id the database identifier of the session to remove
     */
    void removeSessionById(int id);

    /**
     * Returns the number of sessions in the in-memory list.
     *
     * @return the session count
     */
    int getSize();

    /**
     * Removes the session at the given index from the in-memory list.
     *
     * @param index the zero-based index
     */
    void removeSession(int index);

    /**
     * Registers a property-change listener.
     *
     * @param listener the listener to add
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Books a session for a student, persisting the change to the database.
     *
     * @param studentId the ID of the student booking the session
     * @param sessionId the ID of the session to book
     * @return {@code true} if the booking succeeded; {@code false} otherwise
     */
    boolean bookSession(int studentId, int sessionId);

    /**
     * Creates a new session and persists it to the database.
     *
     * @param session the session to create (must have a tutor, non-empty description,
     *                positive price, and non-empty location)
     * @return {@code true} if the session was created; {@code false} if validation failed
     */
    boolean createSession(Session session);

    /**
     * Updates an existing session in the database with new field values.
     *
     * @param updatedSession the session containing the updated values and the existing ID
     * @return {@code true} if the update succeeded; {@code false} if validation failed or
     *         the session was not found
     */
    boolean editSession(Session updatedSession);

    /**
     * Permanently deletes a session from the database.
     *
     * @param sessionId the ID of the session to delete
     * @return {@code true} if the session was deleted; {@code false} if not found
     */
    boolean deleteSession(int sessionId);

    /**
     * Returns the session with the given ID from the in-memory list.
     *
     * @param sessionId the database identifier
     * @return the matching session, or {@code null} if not found
     */
    Session getSessionById(int sessionId);

    /**
     * Creates and persists a new student profile.
     *
     * @param student the student to register (all fields must be non-blank; email must be unique)
     * @return {@code true} if registration succeeded; {@code false} otherwise
     */
    boolean createStudentProfile(Student student);

    /**
     * Creates and persists a new tutor profile pending administrator approval.
     *
     * @param tutor the tutor to register (all fields including education level must be non-blank;
     *              email must be unique)
     * @return {@code true} if registration succeeded; {@code false} otherwise
     */
    boolean createTutorProfile(Tutor tutor);

    /**
     * Approves or declines a pending tutor registration without recording an admin ID.
     *
     * @param tutorId the ID of the tutor to process
     * @param action  {@code "APPROVE"} or {@code "DECLINE"}
     * @return {@code true} if the action succeeded; {@code false} otherwise
     */
    boolean handleTutorRegistration(int tutorId, String action);

    /**
     * Authenticates a user by email and password.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return the matching {@link User} ({@link Student} or {@link Tutor}),
     *         or {@code null} if credentials do not match
     */
    User login(String email, String password);

    /**
     * Cancels a session on behalf of a student or tutor.
     * 
     * A student cancellation reverts the session to {@link Available};
     * a tutor cancellation moves it to the {@link Cancel} state.
     * 
     *
     * @param userId    the ID of the user requesting the cancellation
     * @param sessionId the ID of the session to cancel
     * @param type      {@code "student"} or {@code "tutor"}
     * @return {@code true} if the cancellation succeeded; {@code false} otherwise
     */
    boolean cancelSession(int userId, int sessionId, String type);

    /**
     * Returns all available subjects from the database.
     *
     * @return a list of subject names
     */
    List<String> getSubjects();

    /**
     * Updates the profile information for an existing student.
     *
     * @param student the student with updated field values (all fields must be non-blank)
     * @return {@code true} if the update succeeded; {@code false} otherwise
     */
    boolean editStudentProfile(Student student);

    /**
     * Updates the profile information for an existing tutor.
     *
     * @param tutor the tutor with updated field values (all fields must be non-blank)
     * @return {@code true} if the update succeeded; {@code false} otherwise
     */
    boolean editTutorProfile(Tutor tutor);

    /**
     * Marks a booked session as complete if its end date has passed.
     *
     * @param sessionId the ID of the session to complete
     * @return {@code true} if the session was completed; {@code false} otherwise
     */
    boolean completeSession(int sessionId);
}
