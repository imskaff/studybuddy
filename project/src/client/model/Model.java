package client.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

public interface Model {

  void connect() throws Exception;

  void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSesion) throws Exception;

  ArrayList<Session> getAllSessions();

  ArrayList<Session> getBookedSessions();

  void setLoggedInStudent(Student student);

  Student getLoggedInStudent();

  void addPropertyChangeListener(PropertyChangeListener listener);

}
