package client.mediator;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

// client-side
public interface ClientModel
{
    //void connect(String host, int port) throws Exception;

    //void disconnect() throws Exception;

  boolean bookSession(int studentId, int sessionId);

  // Sends a create session request to the server.
  boolean createSession(Session session);

  // Sends an edit session request to the server.
  boolean editSession(int tutorId, Session session);

  // Sends a delete session request to the server.
  boolean deleteSession(int sessionId, int tutorId);

  // Sends a student profile registration request to the server.
  boolean createStudentProfile(Student student, boolean rulesConfirmed);

  // Sends a tutor profile registration request to the server.
  boolean createTutorProfile(Tutor tutor, boolean rulesConfirmed);

  // Sends a tutor registration handling request to the server.
  boolean handleTutorRegistration(int tutorId, String action, int adminId);

    void addPropertyChangeListener(PropertyChangeListener listener);

  void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession);
}
