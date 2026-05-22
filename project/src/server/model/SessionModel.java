package server.model;

import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface SessionModel {
    ArrayList<Session> getAllSessions();

    Session getSession(int index);

    void removeSessionById(int id);

    int getSize();

    void removeSession(int index);

    void addPropertyChangeListener(PropertyChangeListener listener);

  boolean bookSession(int studentId, int sessionId);

  boolean createSession(Session session);

  boolean editSession(Session updatedSession);

  boolean deleteSession(int sessionId);

  Session getSessionById(int sessionId);

  boolean createStudentProfile(Student student);

  boolean createTutorProfile(Tutor tutor);

  boolean handleTutorRegistration(int tutorId, String action, int adminId);

  List<String> getSubjects();

  boolean editStudentProfile(Student student);

  boolean editTutorProfile(Tutor tutor);

  ArrayList<Session> getAvailableSessions() throws SQLException;

  ArrayList<Session> getBookedSessions(int studentId) throws SQLException;
  ArrayList<Session> getTutorSessions(int tutorId) throws SQLException;

  ArrayList<Tutor> getPendingTutors() throws SQLException;

  User login(String email, String password) throws SQLException;

  int getAdminId(String email, String password) throws SQLException;
}
