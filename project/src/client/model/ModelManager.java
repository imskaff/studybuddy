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

  @Override public boolean cancelSession(int userId, int sessionId,
      String userType)
  {
    boolean success = client.cancelSession(userId, sessionId, userType);
    {
      if(success)
      {
        support.firePropertyChange("SessionsUpdated", null, null);
      }
    }
    return success;
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
    session.complete(session);
  }

  @Override public void book(Session session)
  {
    session.book(session);
  }

  @Override public void cancel(Session session)
  {
    session.cancel(session);
  }



  @Override public void remove(Session session)
  {
    session.remove(session);
  }

  @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }
}
