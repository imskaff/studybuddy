package server.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public interface SessionModel {
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

  List<String> getSubjects();

  boolean editStudentProfile(Student student);

  boolean editTutorProfile(Tutor tutor);
}
