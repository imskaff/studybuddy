package server.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public interface SessionModel {
  List<Session> getAvailableSessions();
  List<Session> getBookedSessions(int studentId);
  List<Session> getTutorSessions(int tutorId);
  List<Tutor> getPendingTutors();
  int getAdminId(String email, String password);
  boolean handleTutorRegistration(int tutorId, String action, int adminId);

    void bookSession(Student student, Tutor tutor, Session sessionId);

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

  boolean handleTutorRegistration(int tutorId, String action);

  User login(String email, String password);

  boolean cancelSession(int userId, int sessionId, String type);

  List<String> getSubjects();

  boolean editStudentProfile(Student student);

  boolean editTutorProfile(Tutor tutor);

  boolean completeSession(int sessionId);

}

