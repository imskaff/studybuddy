package server.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import server.dao.SessionDAO;
import server.dao.UserDAO;

/**
 * Server-side implementation of {@link SessionModel}.
 * Acts as the central business-logic layer: validates requests, delegates
 * persistence to the injected {@link SessionDAO} and {@link UserDAO},
 * and applies the State pattern for session lifecycle transitions.
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 * @author Daniela Vihovanet
 * @author Volodymyr Neka
 */
public class ModelManager implements SessionModel, PropertyChangeListener {
    private final SessionList list;
    private PropertyChangeSupport support;
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;

    /**
     * Constructs a {@code ModelManager} with the given DAO dependencies.
     *
     * @param sessionDAO the DAO used for session persistence
     * @param userDAO    the DAO used for user persistence
     */
    public ModelManager(SessionDAO sessionDAO, UserDAO userDAO) {
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
        list = new SessionList();
        support = new PropertyChangeSupport(this);
    }

    /**
     * Adds the session to the in-memory list.
     *
     * @param student   the student (unused directly here)
     * @param tutor     the tutor (unused directly here)
     * @param sessionId the session to add
     */
    @Override
    public void bookSession(Student student, Tutor tutor, Session sessionId) {
        list.addSession(sessionId);
    }

    /**
     * Returns all sessions from the in-memory list.
     *
     * @return list of all sessions
     */
    @Override
    public ArrayList<Session> getAllSessions() {
        return list.getAllSessions();
    }

    /**
     * Returns the session at the given index from the in-memory list.
     *
     * @param index the index of the session
     * @return the session at that index
     */
    @Override
    public Session getSession(int index) {
        return list.getSession(index);
    }

    /**
     * Returns the session with the given ID from the in-memory list.
     *
     * @param id the session ID
     * @return the matching session, or {@code null} if not found
     */
    @Override
    public Session getSessionById(int id) {
        return list.getSessionById(id);
    }

    /**
     * Returns the number of sessions in the in-memory list.
     *
     * @return the session count
     */
    @Override
    public int getSize() {
        return list.getSize();
    }

    /**
     * Removes the session at the given index from the in-memory list.
     *
     * @param index the index of the session to remove
     */
    @Override
    public void removeSession(int index) {
        list.removeSession(index);
    }

    /**
     * Removes the session with the given ID from the in-memory list.
     *
     * @param id the ID of the session to remove
     */
    @Override
    public void removeSessionById(int id) {
        list.removeSessionById(id);
    }

    /**
     * Registers a listener for property-change events.
     *
     * @param listener the listener to add
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Reads the session and student from the database, verifies the session is
     * unbooked, assigns the student, transitions to the {@link Book} state
     * and persists the change.
     *
     * @param studentId the student's ID
     * @param sessionId the session's ID
     * @return {@code true} if the booking succeeded
     */
    @Override public boolean bookSession(int studentId, int sessionId)
    {
        try {
            Session session = sessionDAO.readByInt(sessionId);
            Student student = (Student) userDAO.readByInt(studentId);
            if (session == null || student == null)
            {
                return false;
            }
            if (session.getStudent() != null)
            {
                return false;
            }
            session.setStudent(student);
            session.book(session);
            sessionDAO.update(session);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates that the session has a tutor, a non-empty description, a positive
     * price and a non-empty location, then saves it to the database.
     *
     * @param session the session to create
     * @return {@code true} if the session was created successfully
     */
    @Override
    public boolean createSession(Session session)
    {
        try {
            if (session == null || session.getTutor() == null || session.getDescription() == null
                || session.getDescription().isEmpty() || session.getPrice() <= 0
                || session.getLocation() == null || session.getLocation().isEmpty())
            {
                return false;
            }
            sessionDAO.create(session.getDescription(), session.getPrice(),
                session.getSubject(), session.getLocation(), session.getStart_date(),
                session.getEnd_date(), session.getTutor(), session.getStudent());

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates the updated session, fetches the existing record, applies the
     * new field values and persists the change to the database.
     *
     * @param updatedSession the session with updated values
     * @return {@code true} if the update succeeded
     */
    @Override
    public boolean editSession(Session updatedSession)
    {
        try {
            if (updatedSession == null || updatedSession.getTutor() == null || updatedSession.getDescription() == null
                || updatedSession.getDescription().isEmpty() || updatedSession.getPrice() <= 0
                || updatedSession.getLocation() == null || updatedSession.getLocation().isEmpty())
            {
                return false;
            }
            Session existing = sessionDAO.readByInt(updatedSession.getId());

            if (existing == null)
            {
                return false;
            }
            existing.setDescription(updatedSession.getDescription());
            existing.setPrice(updatedSession.getPrice());
            existing.setSubject(updatedSession.getSubject());
            existing.setLocation(updatedSession.getLocation());
            existing.setStart_date(updatedSession.getStart_date());
            existing.setEnd_date(updatedSession.getEnd_date());
            sessionDAO.update(existing);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads the session from the database and deletes it.
     *
     * @param sessionId the ID of the session to delete
     * @return {@code true} if the session was deleted successfully
     */
    @Override
    public boolean deleteSession(int sessionId)
    {
        try {
            Session session = sessionDAO.readByInt(sessionId);
            if (session == null)
            {
                return false;
            }
            sessionDAO.delete(session);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates all required student fields and checks that the email is not
     * already registered, then creates the student record in the database.
     *
     * @param student the student to register
     * @return {@code true} if the student was created successfully
     */
    @Override
    public boolean createStudentProfile(Student student)
    {
        try
        {
            if (student == null || student.getFirst_name() == null || student.getFirst_name().isEmpty()
                || student.getLast_name() == null || student.getLast_name().isEmpty() || student.getDate_of_birth() == null
                || student.getEmail() == null || student.getEmail().isEmpty() || student.getPhone() == null
                || student.getPhone().isEmpty() || student.getPassword() == null || student.getPassword().isEmpty()
                || student.getGender() == null || student.getGender().isEmpty() || student.getLanguages() == null
                || student.getLanguages().isEmpty())
            {
                return false;
            }
            if (userDAO.readByEmail(student.getEmail()) != null)
            {
                return false;
            }
            userDAO.create(student.getFirst_name(), student.getLast_name(),
                student.getDate_of_birth(), student.getEmail(), student.getPhone(),
                student.getPassword(), student.getGender(), student.getLanguages(), "Student", null);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates all required tutor fields including education level, checks that the
     * email is not already registered, then creates the tutor record in the database.
     *
     * @param tutor the tutor to register
     * @return {@code true} if the tutor was created successfully
     */
    @Override
    public boolean createTutorProfile(Tutor tutor)
    {
        try {
            if (tutor == null || tutor.getFirst_name() == null || tutor.getFirst_name().isEmpty()
                || tutor.getLast_name() == null || tutor.getLast_name().isEmpty() || tutor.getDate_of_birth() == null
                || tutor.getEmail() == null || tutor.getEmail().isEmpty() || tutor.getPhone() == null
                || tutor.getPhone().isEmpty() || tutor.getPassword() == null || tutor.getPassword().isEmpty()
                || tutor.getGender() == null || tutor.getGender().isEmpty() || tutor.getLanguages() == null
                || tutor.getLanguages().isEmpty() || tutor.getEducation_level() == null || tutor.getEducation_level().isEmpty())
            {
                return false;
            }
            if (userDAO.readByEmail(tutor.getEmail()) != null)
            {
                return false;
            }
            userDAO.create(tutor.getFirst_name(), tutor.getLast_name(), tutor.getDate_of_birth(),
                tutor.getEmail(), tutor.getPhone(), tutor.getPassword(), tutor.getGender(),
                tutor.getLanguages(), "Tutor", tutor.getEducation_level());

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Approves or declines a tutor by calling the DAO with the given action string.
     *
     * @param tutorId the ID of the tutor
     * @param action  {@code "APPROVE"} or {@code "DECLINE"}
     * @return {@code true} if the action succeeded
     */
    @Override
    public boolean handleTutorRegistration(int tutorId, String action)
    {
        try
        {
            if ("APPROVE".equals(action))
            {
                return userDAO.updateTutorApproval(tutorId, true);
            }
            else if ("DECLINE".equals(action))
            {
                return userDAO.updateTutorApproval(tutorId, false);
            }

            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates all required student fields and updates the record in the database.
     *
     * @param student the student with updated values
     * @return {@code true} if the update succeeded
     */
    @Override
    public boolean editStudentProfile(Student student)
    {
        try {
            if (student == null || student.getFirst_name() == null || student.getFirst_name().isEmpty()
                || student.getLast_name() == null || student.getLast_name().isEmpty()
                || student.getDate_of_birth() == null || student.getEmail() == null || student.getEmail().isEmpty()
                || student.getPhone() == null || student.getPhone().isEmpty()
                || student.getPassword() == null || student.getPassword().isEmpty()
                || student.getGender() == null || student.getGender().isEmpty()
                || student.getLanguages() == null || student.getLanguages().isEmpty())
            {
                return false;
            }
            userDAO.update(student);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates all required tutor fields including education level and updates
     * the record in the database.
     *
     * @param tutor the tutor with updated values
     * @return {@code true} if the update succeeded
     */
    @Override
    public boolean editTutorProfile(Tutor tutor)
    {
        try {
            if (tutor == null || tutor.getFirst_name() == null || tutor.getFirst_name().isEmpty()
                || tutor.getLast_name() == null || tutor.getLast_name().isEmpty()
                || tutor.getDate_of_birth() == null || tutor.getEmail() == null || tutor.getEmail().isEmpty()
                || tutor.getPhone() == null || tutor.getPhone().isEmpty()
                || tutor.getPassword() == null || tutor.getPassword().isEmpty()
                || tutor.getGender() == null || tutor.getGender().isEmpty()
                || tutor.getLanguages() == null || tutor.getLanguages().isEmpty()
                || tutor.getEducation_level() == null || tutor.getEducation_level().isEmpty())
            {
                return false;
            }
            userDAO.update(tutor);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cancels a session using the State pattern. A student cancellation clears the
     * assigned student and resets the session to {@link Available}; a tutor
     * cancellation leaves it in the {@link Cancel} state.
     *
     * @param userId    the ID of the user requesting cancellation
     * @param sessionId the ID of the session to cancel
     * @param type      {@code "student"} or {@code "tutor"}
     * @return {@code true} if the cancellation succeeded
     */
    @Override
    public boolean cancelSession(int userId, int sessionId, String type)
    {
        try
        {
            Session session = sessionDAO.readByInt(sessionId);

            if (session == null)
            {
                return false;
            }

            SessionState stateBefore = session.getState();
            session.cancel(session);

            if (session.getState() == stateBefore)
            {
                return false;
            }

            if (type.equalsIgnoreCase("student"))
            {
                session.setStudent(null);
                session.setState(new Available());
            }

            sessionDAO.update(session);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Looks up a user by email and password in the database.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return the matching {@link User}, or {@code null} if not found
     */
    @Override
    public User login(String email, String password)
    {
        try {
            if (email == null || email.isEmpty() || password == null || password.isEmpty())
            {
                return null;
            }
            return userDAO.readByEmailAndPassword(email, password);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns all available subject names from the database.
     *
     * @return list of subject names
     */
    @Override
    public List<String> getSubjects()
    {
        try
        {
            return sessionDAO.readSubjects();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Returns available sessions, auto-completing any expired booked ones first.
     *
     * @return list of available sessions
     */
    @Override
    public List<Session> getAvailableSessions() {
        try {
            List<Session> sessions = sessionDAO.readAvailable();
            autoCompleteSessions(sessions);
            return sessions;
        }
        catch (Exception e) { e.printStackTrace(); return new ArrayList<>(); }
    }

    /**
     * Returns sessions booked by the given student, auto-completing any expired
     * ones first.
     *
     * @param studentId the student's ID
     * @return list of the student's booked sessions
     */
    @Override
    public List<Session> getBookedSessions(int studentId) {
        try {
            List<Session> sessions = sessionDAO.readByStudent(studentId);
            autoCompleteSessions(sessions);
            return sessions;
        }
        catch (Exception e) { e.printStackTrace(); return new ArrayList<>(); }
    }

    /**
     * Returns sessions belonging to the given tutor, auto-completing any expired
     * ones first.
     *
     * @param tutorId the tutor's ID
     * @return list of the tutor's sessions
     */
    @Override
    public List<Session> getTutorSessions(int tutorId) {
        try {
            List<Session> sessions = sessionDAO.readByTutor(tutorId);
            autoCompleteSessions(sessions);
            return sessions;
        }
        catch (Exception e) { e.printStackTrace(); return new ArrayList<>(); }
    }

    /**
     * Returns all tutors waiting for admin approval.
     *
     * @return list of pending tutors
     */
    @Override
    public List<Tutor> getPendingTutors() {
        try { return userDAO.readPendingTutors(); }
        catch (Exception e) { e.printStackTrace(); return new ArrayList<>(); }
    }

    /**
     * Returns the ID of the admin with the given credentials, or {@code -1} if
     * no matching admin is found.
     *
     * @param email    the admin's email
     * @param password the admin's password
     * @return the admin ID, or {@code -1}
     */
    @Override
    public int getAdminId(String email, String password) {
        try { return userDAO.getAdminId(email, password); }
        catch (Exception e) { e.printStackTrace(); return -1; }
    }

    /**
     * Approves or declines a tutor and records which admin performed the action.
     *
     * @param tutorId the ID of the tutor
     * @param action  {@code "APPROVE"} or {@code "DECLINE"}
     * @param adminId the ID of the admin making the decision
     * @return {@code true} if the action succeeded
     */
    @Override
    public boolean handleTutorRegistration(int tutorId, String action, int adminId) {
        try {
            if ("APPROVE".equals(action)) {
                userDAO.updateApprovalStatus(tutorId, true, adminId);
                return true;
            }
            if ("DECLINE".equals(action)) {
                userDAO.declineTutor(tutorId);
                return true;
            }
            return false;
        }
        catch (Exception e) { e.printStackTrace(); return false; }
    }

    /**
     * Iterates through a list of sessions and auto-completes any that are in the
     * {@link Book} state and whose end date has already passed.
     * Only sessions whose state actually changes are persisted.
     *
     * @param sessions the list of sessions to check and potentially update
     */
    private void autoCompleteSessions(List<Session> sessions)
    {
        for (Session session : sessions)
        {
            SessionState stateBefore = session.getState();
            session.complete(session);

            if (session.getState() != stateBefore)
            {
                try
                {
                    sessionDAO.update(session);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Forwards any received property-change events to this model's own listeners.
     *
     * @param evt the property-change event to propagate
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }
}
