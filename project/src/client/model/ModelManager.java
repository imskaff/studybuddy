package client.model;

import java.beans.PropertyChangeSupport;

import client.mediator.Client;
import client.mediator.ClientModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.mediator.LocalDateAdapter;
import server.mediator.LoginResponse;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Client-side implementation of {@link Model}.
 * Delegates all server communication to {@link Client} and keeps track
 * of the currently logged-in user.
 *
 * @author Adam Terelak
 * @author Daniela Vihovanet
 */
public class ModelManager implements Model, PropertyChangeListener {
    private final ClientModel client;
    private final PropertyChangeSupport support;
    private Student loggedInStudent;
    private Tutor loggedInTutor;
    private int loggedInAdminId = -1;

    /**
     * Constructs a ModelManager and creates the underlying client.
     */
    public ModelManager() {
        client = new Client();
        support = new PropertyChangeSupport(this);
    }

    /**
     * Registers as a listener on the client, then opens the socket connection.
     *
     * @throws Exception if the connection fails
     */
    @Override
    public void connect() throws Exception {
        client.addPropertyChangeListener(this);
        client.connect("localhost", 4025);
    }

    /**
     * Closes the socket connection.
     */
    @Override
    public void disconnect() {
        client.disconnect();
    }

    /**
     * Books a session for the student and fires a {@code "SessionBooked"} event on success.
     *
     * @param loggedInStudent the student booking the session
     * @param sessionTutor    the tutor who owns the session
     * @param bookedSession   the session to book
     * @throws Exception if the booking request fails
     */
    @Override
    public void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession) throws Exception {
        boolean success = client.bookSession(loggedInStudent.getId(), bookedSession.getId());
        if (success) {
            bookedSession.setStudent(loggedInStudent);
            support.firePropertyChange("SessionBooked", null, bookedSession);
        }
    }

    /**
     * Returns all available sessions from the server.
     *
     * @return list of available sessions
     */
    @Override
    public ArrayList<Session> getAllSessions() {
        return client.getAvailableSessions();
    }

    /**
     * Returns sessions belonging to the logged-in tutor.
     *
     * @return list of the tutor's sessions, or an empty list if no tutor is logged in
     */
    @Override
    public ArrayList<Session> getTutorSessions() {
        if (loggedInTutor == null) return new ArrayList<>();
        return client.getTutorSessions(loggedInTutor.getId());
    }

    /**
     * Returns sessions booked by the logged-in student.
     *
     * @return list of booked sessions, or an empty list if no student is logged in
     */
    @Override
    public ArrayList<Session> getBookedSessions() {
        if (loggedInStudent == null) return new ArrayList<>();
        return client.getBookedSessions(loggedInStudent.getId());
    }

    /**
     * Stores the currently logged-in student.
     *
     * @param student the student to store
     */
    @Override public void setLoggedInStudent(Student student)
    {
        this.loggedInStudent = student;
    }

    /**
     * Returns the currently logged-in student.
     *
     * @return the logged-in student, or {@code null} if none
     */
    @Override public Student getLoggedInStudent()
    {
        return loggedInStudent;
    }

    /**
     * Stores the currently logged-in tutor.
     *
     * @param tutor the tutor to store
     */
    @Override public void setLoggedInTutor(Tutor tutor)
    {
        this.loggedInTutor = tutor;
    }

    /**
     * Returns the currently logged-in tutor.
     *
     * @return the logged-in tutor, or {@code null} if none
     */
    @Override public Tutor getLoggedInTutor()
    {
        return loggedInTutor;
    }

    /**
     * Stores the ID of the logged-in admin.
     *
     * @param adminId the admin's ID
     */
    @Override public void setLoggedInAdminId(int adminId)
    {
        this.loggedInAdminId = adminId;
    }

    /**
     * Returns the ID of the logged-in admin.
     *
     * @return the admin ID, or {@code -1} if no admin is logged in
     */
    @Override public int getLoggedInAdminId()
    {
        return loggedInAdminId;
    }

    /**
     * Returns a list of tutors waiting for admin approval.
     *
     * @return list of pending tutors
     */
    @Override
    public ArrayList<Tutor> getPendingTutors() {
        return client.getPendingTutors();
    }

    /**
     * Sends a create-session request to the server.
     *
     * @param session the session to create
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean createSession(Session session) {
        return client.createSession(session);
    }

    /**
     * Sends an edit-session request to the server.
     *
     * @param session the updated session
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean editSession(Session session) {
        return client.editSession(loggedInTutor.getId(), session);
    }

    /**
     * Cancels a session and fires a {@code "SessionsUpdated"} event on success.
     *
     * @param userId    the ID of the user requesting the cancellation
     * @param sessionId the ID of the session to cancel
     * @param userType  {@code "student"} or {@code "tutor"}
     * @return {@code true} if the cancellation succeeded
     */
    @Override
    public boolean cancelSession(int userId, int sessionId, String userType)
    {
        boolean success = client.cancelSession(userId, sessionId, userType);
        if (success)
        {
            support.firePropertyChange("SessionsUpdated", null, null);
        }
        return success;
    }

    /**
     * Sends a delete-session request to the server.
     *
     * @param sessionId the ID of the session to delete
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean deleteSession(int sessionId) {
        return client.deleteSession(sessionId, loggedInTutor.getId());
    }

    /**
     * Sends an approve request for the given tutor.
     *
     * @param tutorId the ID of the tutor to approve
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean approveTutor(int tutorId) {
        return client.handleTutorRegistration(tutorId, "APPROVE", loggedInAdminId);
    }

    /**
     * Sends a decline request for the given tutor.
     *
     * @param tutorId the ID of the tutor to decline
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean declineTutor(int tutorId) {
        return client.handleTutorRegistration(tutorId, "DECLINE", loggedInAdminId);
    }

    /**
     * Registers a listener for property change events.
     *
     * @param listener the listener to add
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Returns all available subjects from the server.
     *
     * @return list of subject names
     */
    @Override
    public List<String> getSubjects() {
        return client.getSubjects();
    }

    /**
     * Saves the updated student profile and updates the local cache on success.
     *
     * @param student the updated student
     * @return {@code true} if the update succeeded
     */
    @Override
    public boolean editStudentProfile(Student student) {
        boolean success = client.editStudentProfile(student);
        if (success) {
            loggedInStudent = student;
        }
        return success;
    }

    /**
     * Saves the updated tutor profile and updates the local cache on success.
     *
     * @param tutor the updated tutor
     * @return {@code true} if the update succeeded
     */
    @Override
    public boolean editTutorProfile(Tutor tutor) {
        boolean success = client.editTutorProfile(tutor);
        if (success) {
            loggedInTutor = tutor;
        }
        return success;
    }

    /**
     * Logs in the user and stores the returned object in the local cache.
     * Returns the user type string on success, or {@code null} on failure.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return the user type ({@code "student"}, {@code "tutor"}, {@code "admin"}), or {@code null}
     */
    @Override
    public String login(String email, String password) {
        LoginResponse response = client.login(email, password);
        if (!"OK".equals(response.getStatus())) return null;
        String userType = response.getUserType();
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
            .create();
        if ("student".equals(userType)) {
            loggedInStudent = gson.fromJson(response.getUserJson(), Student.class);
        } else if ("tutor".equals(userType)) {
            loggedInTutor = gson.fromJson(response.getUserJson(), Tutor.class);
        } else if ("admin".equals(userType)) {
            loggedInAdminId = Integer.parseInt(response.getUserJson());
        }
        return userType;
    }

    /**
     * Sends a student registration request to the server.
     *
     * @param student the student to register
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean registerStudent(Student student) {
        return client.createStudentProfile(student, true);
    }

    /**
     * Sends a tutor registration request to the server.
     *
     * @param tutor the tutor to register
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean registerTutor(Tutor tutor) {
        return client.createTutorProfile(tutor, true);
    }



    /**
     * Forwards any received property-change events to this model's own listeners.
     *
     * @param evt the event to forward
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }
}
