package client.mediator;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import server.mediator.LoginResponse;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

/**
 * Client-side socket communication contract.
 * 
 * Implemented by {@link Client}. Each method corresponds to one JSON request
 * sent over the persistent TCP socket to the server. The client-side
 * {@link client.model.ModelManager} delegates all network calls to this interface.
 * 
 * @author Daniela Vihovanet
 */
public interface ClientModel {

    /**
     * Opens a persistent TCP connection to the server.
     *
     * @param host the server hostname or IP address
     * @param port the server port number
     * @throws Exception if the connection cannot be established
     */
    void connect(String host, int port) throws Exception;

    /**
     * Closes the persistent connection to the server.
     */
    void disconnect();

    /**
     * Sends a book-session request to the server.
     *
     * @param studentId the ID of the student booking the session
     * @param sessionId the ID of the session to book
     * @return {@code true} if the server confirmed the booking
     */
    boolean bookSession(int studentId, int sessionId);

    /**
     * Sends a create-session request to the server.
     *
     * @param session the session to create
     * @return {@code true} if the server confirmed the creation
     */
    boolean createSession(Session session);

    /**
     * Sends an edit-session request to the server.
     *
     * @param tutorId the ID of the tutor making the edit
     * @param session the session with updated field values
     * @return {@code true} if the server confirmed the update
     */
    boolean editSession(int tutorId, Session session);

    /**
     * Sends a delete-session request to the server.
     *
     * @param sessionId the ID of the session to delete
     * @param tutorId   the ID of the tutor requesting the deletion
     * @return {@code true} if the server confirmed the deletion
     */
    boolean deleteSession(int sessionId, int tutorId);

    /**
     * Sends a student registration request to the server.
     *
     * @param student        the student data to register
     * @param rulesConfirmed whether the user confirmed the platform rules
     * @return {@code true} if the server confirmed the registration
     */
    boolean createStudentProfile(Student student, boolean rulesConfirmed);

    /**
     * Sends a tutor registration request to the server.
     *
     * @param tutor          the tutor data to register
     * @param rulesConfirmed whether the user confirmed the platform rules
     * @return {@code true} if the server confirmed the registration
     */
    boolean createTutorProfile(Tutor tutor, boolean rulesConfirmed);

    /**
     * Sends a tutor registration handling request (approve or decline) to the server.
     *
     * @param tutorId the ID of the tutor to process
     * @param action  {@code "APPROVE"} or {@code "DECLINE"}
     * @param adminId the ID of the administrator performing the action
     * @return {@code true} if the server confirmed the action
     */
    boolean handleTutorRegistration(int tutorId, String action, int adminId);

    /**
     * Sends a student profile update request to the server.
     *
     * @param student the student with updated field values
     * @return {@code true} if the server confirmed the update
     */
    boolean editStudentProfile(Student student);

    /**
     * Sends a tutor profile update request to the server.
     *
     * @param tutor the tutor with updated field values
     * @return {@code true} if the server confirmed the update
     */
    boolean editTutorProfile(Tutor tutor);

    /**
     * Sends a login request to the server and returns the structured response.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return a {@link LoginResponse} containing the user type and serialised user data
     */
    LoginResponse login(String email, String password);

    /**
     * Sends a cancel-session request to the server.
     *
     * @param userId    the ID of the user cancelling the session
     * @param sessionId the ID of the session to cancel
     * @param userType  {@code "student"} or {@code "tutor"}
     * @return {@code true} if the server confirmed the cancellation
     */
    boolean cancelSession(int userId, int sessionId, String userType);

    /**
     * Retrieves all available (unbooked, future) sessions from the server.
     *
     * @return a list of available sessions, or an empty list on failure
     */
    ArrayList<Session> getAvailableSessions();

    /**
     * Retrieves all sessions belonging to the given tutor from the server.
     *
     * @param tutorId the ID of the tutor
     * @return a list of the tutor's sessions, or an empty list on failure
     */
    ArrayList<Session> getTutorSessions(int tutorId);

    /**
     * Retrieves all sessions booked by the given student from the server.
     *
     * @param studentId the ID of the student
     * @return a list of booked sessions, or an empty list on failure
     */
    ArrayList<Session> getBookedSessions(int studentId);

    /**
     * Retrieves all tutor registrations awaiting approval from the server.
     *
     * @return a list of pending tutors, or an empty list on failure
     */
    ArrayList<Tutor> getPendingTutors();

    /**
     * Retrieves all available subject names from the server.
     *
     * @return a list of subject names, or an empty list on failure
     */
    List<String> getSubjects();

    /**
     * Registers a property-change listener to receive server-pushed events.
     *
     * @param listener the listener to add
     */
    void addPropertyChangeListener(PropertyChangeListener listener);
}
