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

  boolean createSession(Session session);

  boolean editSession(int tutorId, Session session);

  boolean deleteSession(int sessionId, int tutorId);

  boolean createStudentProfile(Student student, boolean rulesConfirmed);

  boolean createTutorProfile(Tutor tutor, boolean rulesConfirmed);

  boolean handleTutorRegistration(int tutorId, String action);

    void addPropertyChangeListener(PropertyChangeListener listener);

  void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession);
}
