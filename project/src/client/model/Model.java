package client.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

public interface Model {

  void connect() throws Exception;

  void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession) throws Exception;

  ArrayList<Session> getAllSessions();

  ArrayList<Session> getBookedSessions();

  void setLoggedInStudent(Student student);

  Student getLoggedInStudent();

  void setLoggedInTutor(Tutor tutor);

  Tutor getLoggedInTutor();

  void addPropertyChangeListener(PropertyChangeListener listener);

  public void complete(Session session);
  public void book(Session session);
  public void cancel(Session session);
  public void remove(Session session);

}
