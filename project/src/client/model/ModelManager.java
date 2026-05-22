package client.model;

import java.beans.PropertyChangeSupport;

import client.mediator.Client;
import client.mediator.ClientModel;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ModelManager implements Model, PropertyChangeListener {
    private final ClientModel client;
    private final PropertyChangeSupport support;
    private Student loggedInStudent;
    private Tutor loggedInTutor;
    private int loggedInAdminId = -1;

    public ModelManager() {
        client = new Client();
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void connect() throws Exception {
        // Registers this ModelManager as a listener for server-pushed events
        client.addPropertyChangeListener(this);

        // Opens a persistent socket connection to the server
        client.connect("localhost", 4025);
    }

    @Override
    public void disconnect() {
        client.disconnect();
    }

    @Override
    public void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession) throws Exception {
        boolean success = client.bookSession(loggedInStudent.getId(), bookedSession.getId());
        if (success) {
            bookedSession.setStudent(loggedInStudent);
            support.firePropertyChange("SessionBooked", null, bookedSession);
        }
    }

    @Override
    public ArrayList<Session> getAllSessions() {
        return client.getAvailableSessions();
    }

    @Override
    public ArrayList<Session> getBookedSessions() {
        if (loggedInStudent == null) return new ArrayList<>();
        return client.getBookedSessions(loggedInStudent.getId());
    }

    @Override
    public ArrayList<Session> getTutorSessions() {
        if (loggedInTutor == null) return new ArrayList<>();
        return client.getTutorSessions(loggedInTutor.getId());
    }

   @Override public void setLoggedInStudent(Student student)
   {
    this.loggedInStudent = student;
   }

   @Override public Student getLoggedInStudent()
   {
    return loggedInStudent;
   }

   @Override public void setLoggedInTutor(Tutor tutor)
   {
    this.loggedInTutor = tutor;
   }

   @Override public Tutor getLoggedInTutor()
   {
    return loggedInTutor;
   }

   @Override public void setLoggedInAdminId(int adminId)
   {
    this.loggedInAdminId = adminId;
   }

   @Override public int getLoggedInAdminId()
   {
    return loggedInAdminId;
   }

    @Override
    public ArrayList<Tutor> getPendingTutors() {
        return client.getPendingTutors();
    }

    @Override
    public boolean createSession(Session session) {
        return client.createSession(session);
    }

    @Override
    public boolean editSession(Session session) {
        return client.editSession(loggedInTutor.getId(), session);
    }

    @Override
    public boolean deleteSession(int sessionId) {
        return client.deleteSession(sessionId, loggedInTutor.getId());
    }

    @Override
    public boolean approveTutor(int tutorId) {
        return client.handleTutorRegistration(tutorId, "APPROVE", loggedInAdminId);
    }

    @Override
    public boolean declineTutor(int tutorId) {
        return client.handleTutorRegistration(tutorId, "DECLINE", loggedInAdminId);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public List<String> getSubjects() {
        return client.getSubjects();
    }

    @Override
    public boolean editStudentProfile(Student student) {
        boolean success = client.editStudentProfile(student);
        if (success) {
            loggedInStudent = student;
        }
        return success;
    }

    @Override
    public boolean editTutorProfile(Tutor tutor) {
        boolean success = client.editTutorProfile(tutor);
        if (success) {
            loggedInTutor = tutor;
        }
        return success;
    }

    @Override
    public String login(String email, String password) {
        LoginResult result = client.login(email, password);
        String userType = result.getUserType();
        if ("error".equals(userType)) return null;

        if ("student".equals(userType)) {
            loggedInStudent = result.getStudent();
        } else if ("tutor".equals(userType) || "tutor_pending".equals(userType)) {
            loggedInTutor = result.getTutor();
        } else if ("admin".equals(userType)) {
            loggedInAdminId = result.getAdminId();
        }
        return userType;
    }

    @Override
    public boolean registerStudent(Student student) {
        return client.createStudentProfile(student, true);
    }

    @Override
    public boolean registerTutor(Tutor tutor) {
        return client.createTutorProfile(tutor, true);
    }

  @Override public void complete(Session session)
  {
    try { session.complete(session); } catch (IllegalStateException ignored) {}
  }

  @Override public void book(Session session)
  {
    try { session.book(session); } catch (IllegalStateException ignored) {}
  }

  @Override public void cancel(Session session)
  {
    try { session.cancel(session); } catch (IllegalStateException ignored) {}
  }

  @Override public void remove(Session session)
  {
    try { session.remove(session); } catch (IllegalStateException ignored) {}
  }

  @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }
}
