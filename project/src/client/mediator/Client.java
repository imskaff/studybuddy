package client.mediator;

import com.google.gson.Gson;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.mediator.*;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

public class Client implements ClientModel
{
  // Address of the server
  private final String HOST = "localhost";

  // Port number used to connect to the server
  // Must match the port in ServerConnector
  private final int PORT = 4025;

  // Persistent socket connection to the server
  private Socket socket;

  // Input stream used to read the server response
  private BufferedReader in;

  // Output stream used to send data to the server
  private PrintWriter out;

  // Gson is used to convert Java objects <-> JSON
  private Gson gson;

  private final PropertyChangeSupport support;

  public Client() {
    support = new PropertyChangeSupport(this);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  // Opens a persistent connection to the server
  @Override
  public void connect(String host, int port) throws Exception {
    socket = new Socket(host, port);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
    gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();
    System.out.println("Connected to server " + host + ":" + port);
  }

  // Closes the connection to the server
  @Override
  public void disconnect() {
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // This helper method is used to send any request object to the server.
  // It converts the request into JSON, sends it through the persistent socket,
  // receives the server response, converts the response from JSON,
  // and returns true if the operation was successful.
  private boolean sendRequest(Object request)
  {
    try
    {
      // Converts the request object into JSON text
      String requestJson = gson.toJson(request);

      // Prints the JSON request for debugging
      System.out.println("Client> " + requestJson);

      // Sends the JSON request to the server
      out.println(requestJson);

      // Reads the JSON response from the server
      String replyJson = in.readLine();

      // Prints the received JSON response for debugging
      System.out.println("Server> " + replyJson);

      // Converts the JSON response into a Response object
      Response reply =
          gson.fromJson(replyJson, Response.class);

      return "OK".equals(reply.getStatus())
          || "APPROVED".equals(reply.getStatus())
          || "DECLINED".equals(reply.getStatus());
    }
    catch(IOException e)
    {
      // Prints the error if communication fails
      e.printStackTrace();

      // Returns false if an error occurs
      return false;
    }
  }

  // This helper method sends a request and returns the raw JSON response.
  // Used by methods that need to parse data from the response
  // (e.g., lists of sessions or tutors).
  private String sendRequestForResponse(Object request)
  {
    try
    {
      // Converts the request object into JSON text
      String requestJson = gson.toJson(request);

      // Prints the JSON request for debugging
      System.out.println("Client> " + requestJson);

      // Sends the JSON request to the server
      out.println(requestJson);

      // Reads the JSON response from the server
      String replyJson = in.readLine();

      // Prints the received JSON response for debugging
      System.out.println("Server> " + replyJson);

      return replyJson;
    }
    catch(IOException e)
    {
      // Prints the error if communication fails
      e.printStackTrace();

      return null;
    }
  }

  // Sends a booking request to the server
  @Override
  public boolean bookSession(int studentId, int sessionId)
  {
    // Creates the booking request object
    BookSessionRequest request =
        new BookSessionRequest(studentId, sessionId);

    // Sends the request to the server
    return sendRequest(request);
  }

  // Sends a create session request to the server
  @Override
  public boolean createSession(Session session)
  {
    // Creates the create session request object
    CreateSessionRequest request = new CreateSessionRequest(session);

    // Sends the request to the server
    return sendRequest(request);
  }

  // Sends an edit session request to the server
  @Override
  public boolean editSession(int tutorId, Session session)
  {
    // Creates the edit session request object
    EditSessionRequest request = new EditSessionRequest(tutorId, session);

    // Sends the request to the server
    return sendRequest(request);
  }

  // Sends a delete session request to the server
  @Override
  public boolean deleteSession(int sessionId, int tutorId)
  {
    // Creates the delete session request object
    DeleteSessionRequest request = new DeleteSessionRequest(sessionId, tutorId);

    // Sends the request to the server
    return sendRequest(request);
  }

  // Sends a create student profile request to the server
  @Override public boolean createStudentProfile(Student student,
      boolean rulesConfirmed)
  {
    // Creates the student profile request object
    CreateStudentProfileRequest request = new CreateStudentProfileRequest(student, rulesConfirmed);

    // Sends the request to the server
    return sendRequest(request);
  }

  // Sends a create tutor profile request to the server
  @Override public boolean createTutorProfile(Tutor tutor,
      boolean rulesConfirmed)
  {
    // Creates the tutor profile request object
    CreateTutorProfileRequest request = new CreateTutorProfileRequest(tutor, rulesConfirmed);

    // Sends the request to the server
    return sendRequest(request);
  }

  // Sends an approval/decline tutor registration request to the server
  @Override public boolean handleTutorRegistration(int tutorId, String action, int adminId)
  {
    // Creates the handle tutor registration request object
    HandleTutorRegistrationRequest request =
        new HandleTutorRegistrationRequest(tutorId, action, adminId);

    // Sends the request to the server
    return sendRequest(request);
  }

  // Retrieves all available (unbooked) sessions from the server
  @Override
  public ArrayList<Session> getAvailableSessions()
  {
    // Creates the request object
    GetAvailableSessionsRequest request = new GetAvailableSessionsRequest();

    // Sends the request and receives the raw JSON response
    String replyJson = sendRequestForResponse(request);

    if (replyJson == null) return new ArrayList<>();

    // Parses the response to extract the list of sessions
    SessionListResponse response = gson.fromJson(replyJson, SessionListResponse.class);

    if ("OK".equals(response.getStatus()) && response.getSessions() != null)
    {
      return response.getSessions();
    }

    return new ArrayList<>();
  }

  // Retrieves all sessions booked by a specific student from the server
  @Override
  public ArrayList<Session> getBookedSessions(int studentId)
  {
    // Creates the request object with the student ID
    GetBookedSessionsRequest request = new GetBookedSessionsRequest(studentId);

    // Sends the request and receives the raw JSON response
    String replyJson = sendRequestForResponse(request);

    if (replyJson == null) return new ArrayList<>();

    // Parses the response to extract the list of sessions
    SessionListResponse response = gson.fromJson(replyJson, SessionListResponse.class);

    if ("OK".equals(response.getStatus()) && response.getSessions() != null)
    {
      return response.getSessions();
    }

    return new ArrayList<>();
  }

  // Retrieves all pending tutor registrations from the server
  @Override
  public ArrayList<Tutor> getPendingTutors()
  {
    // Creates the request object
    GetPendingTutorsRequest request = new GetPendingTutorsRequest();

    // Sends the request and receives the raw JSON response
    String replyJson = sendRequestForResponse(request);

    if (replyJson == null) return new ArrayList<>();

    // Parses the response to extract the list of tutors
    TutorListResponse response = gson.fromJson(replyJson, TutorListResponse.class);

    if ("OK".equals(response.getStatus()) && response.getTutors() != null)
    {
      return response.getTutors();
    }

    return new ArrayList<>();
  }
}
