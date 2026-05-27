package client.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

/**
 * Client-side business-logic contract used by all ViewModels.
 * 
 * Implemented by {@link ModelManager}. Abstracts the network communication
 * and session state so that ViewModels never depend directly on
 * {@link client.mediator.Client}.
 * 
 *
 * @author Adam Terelak
 * @author Daniela Vihovanet
 */
public interface Model {

    /**
     * Opens a connection to the server.
     *
     * @throws Exception if the connection cannot be established
     */
    void connect() throws Exception;

    /**
     * Closes the connection to the server.
     */
    void disconnect();

    /**
     * Books a session for the given student and fires a {@code "SessionBooked"} event on success.
     *
     * @param loggedInStudent the student booking the session
     * @param sessionTutor    the tutor who owns the session
     * @param bookedSession   the session to book
     * @throws Exception if the booking request fails
     */
    void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession) throws Exception;

    /**
     * Returns all available (unbooked, future) sessions from the server.
     *
     * @return a list of available sessions
     */
    ArrayList<Session> getAllSessions();

    /**
     * Returns all sessions belonging to the currently logged-in tutor.
     *
     * @return a list of the tutor's sessions, or an empty list if no tutor is logged in
     */
    ArrayList<Session> getTutorSessions();

    /**
     * Returns all sessions booked by the currently logged-in student.
     *
     * @return a list of booked sessions, or an empty list if no student is logged in
     */
    ArrayList<Session> getBookedSessions();

    /**
     * Stores the logged-in student for the current session.
     *
     * @param student the authenticated student
     */
    void setLoggedInStudent(Student student);

    /**
     * Returns the currently logged-in student.
     *
     * @return the logged-in student, or {@code null} if none
     */
    Student getLoggedInStudent();

    /**
     * Stores the ID of the logged-in administrator for the current session.
     *
     * @param adminId the authenticated administrator's ID
     */
    void setLoggedInAdminId(int adminId);

    /**
     * Returns the ID of the currently logged-in administrator.
     *
     * @return the admin ID, or {@code -1} if none
     */
    int getLoggedInAdminId();

    /**
     * Returns all tutor registrations awaiting administrator approval.
     *
     * @return a list of pending tutors
     */
    ArrayList<Tutor> getPendingTutors();

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
     * @param session the session with updated field values
     * @return {@code true} if the server confirmed the update
     */
    boolean editSession(Session session);

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
     * Sends a delete-session request to the server.
     *
     * @param sessionId the ID of the session to delete
     * @return {@code true} if the server confirmed the deletion
     */
    boolean deleteSession(int sessionId);

    /**
     * Sends an approve-tutor request to the server on behalf of the logged-in admin.
     *
     * @param tutorId the ID of the tutor to approve
     * @return {@code true} if the server confirmed the approval
     */
    boolean approveTutor(int tutorId);

    /**
     * Sends a decline-tutor request to the server on behalf of the logged-in admin.
     *
     * @param tutorId the ID of the tutor to decline
     * @return {@code true} if the server confirmed the decline
     */
    boolean declineTutor(int tutorId);

    /**
     * Sends a student registration request to the server.
     *
     * @param student the student data to register
     * @return {@code true} if the server confirmed the registration
     */
    boolean registerStudent(Student student);

    /**
     * Sends a tutor registration request to the server.
     *
     * @param tutor the tutor data to register
     * @return {@code true} if the server confirmed the registration
     */
    boolean registerTutor(Tutor tutor);

    /**
     * Stores the logged-in tutor for the current session.
     *
     * @param tutor the authenticated tutor
     */
    void setLoggedInTutor(Tutor tutor);

    /**
     * Returns the currently logged-in tutor.
     *
     * @return the logged-in tutor, or {@code null} if none
     */
    Tutor getLoggedInTutor();

    /**
     * Sends a student profile update request to the server.
     *
     * @param student the student with updated field values
     * @return {@code true} if the server confirmed the update
     */
    boolean editStudentProfile(Student student);

    /**
     * Registers a property-change listener to receive model events.
     *
     * @param listener the listener to add
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Returns all available subject names from the server.
     *
     * @return a list of subject names
     */
    List<String> getSubjects();

    /**
     * Sends a tutor profile update request to the server.
     *
     * @param tutor the tutor with updated field values
     * @return {@code true} if the server confirmed the update
     */
    boolean editTutorProfile(Tutor tutor);

    /**
     * Authenticates a user and stores their data locally on success.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return the user type string ({@code "student"}, {@code "tutor"}, {@code "admin"}),
     *         or {@code null} if login failed
     */
    String login(String email, String password);

}
