package client.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

public interface Model extends StudentModel, TutorModel, AdminModel {

  void connect() throws Exception;

  void disconnect();

  void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession) throws Exception;

  ArrayList<Session> getAllSessions();

  ArrayList<Session> getBookedSessions();

  void setLoggedInStudent(Student student);

  Student getLoggedInStudent();

  void setLoggedInAdminId(int adminId);

  int getLoggedInAdminId();

  ArrayList<Tutor> getPendingTutors();

  boolean createSession(Session session);

  boolean editSession(Session session);

  boolean deleteSession(int sessionId);

  boolean approveTutor(int tutorId);

  boolean declineTutor(int tutorId);

  boolean registerStudent(Student student);

  boolean registerTutor(Tutor tutor);

  void setLoggedInTutor(Tutor tutor);

  Tutor getLoggedInTutor();

  void addPropertyChangeListener(PropertyChangeListener listener);

  List<String> getSubjects();

  boolean editStudentProfile(Student student);

  boolean editTutorProfile(Tutor tutor);

  String login(String email, String password);

  public void complete(Session session);
  public void book(Session session);
  public void cancel(Session session);
  public void remove(Session session);
}
