package client.mediator;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

// client-side
public interface ClientModel {
  // Opens a persistent connection to the server
  void connect(String host, int port) throws Exception;

  // Closes the connection to the server
  void disconnect();

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

  // Retrieves all available (unbooked) sessions from the server.
  ArrayList<Session> getAvailableSessions();

  // Retrieves all sessions booked by a specific student from the server.
  ArrayList<Session> getBookedSessions(int studentId);

  // Retrieves all pending tutor registrations from the server.
  ArrayList<Tutor> getPendingTutors();

  // Retrieves all subjects from the server.
  List<String> getSubjects();

  // Adds a property change listener to the client model.
  void addPropertyChangeListener(PropertyChangeListener listener);
}
