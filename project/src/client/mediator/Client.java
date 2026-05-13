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
import server.mediator.*;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

public class Client implements ClientModel
{

  // Address of the server
  private final String HOST = "localhost";

  // Port number used to connect to the server
  // Must match the port in BookingServer
  private final int PORT = 4025;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final PropertyChangeSupport support;

    public Client() {
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

  @Override public void bookSession(Student loggedInStudent, Tutor sessionTutor,
      Session bookedSession)
  {

  }

  @Override
    public void connect(String host, int port) throws Exception {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connected to server " + host + ":" + port);
    }

    @Override
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Disconnected from server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  // This helper method is used to send any request object to the server.
  // It converts the request into JSON, sends it through the socket,
  // receives the server response, converts the response from JSON,
  // and returns true if the operation was successful.
  private boolean sendRequest(Object request)
  {
    // Gson is used to convert Java objects <-> JSON
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
        new LocalDateAdapter()).registerTypeAdapter(LocalDateTime.class,
        new LocalDateTimeAdapter()).create();

    try
    {
      // Creates a connection to the server
      Socket socket = new Socket(HOST, PORT);

      // Input stream used to read the server response
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      // Output stream used to send data to the server
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

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

      // Closes the socket connection
      socket.close();

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
  @Override public boolean handleTutorRegistration(int tutorId, String action)
  {
    // Creates the handle tutor registration request object
    HandleTutorRegistrationRequest request =
        new HandleTutorRegistrationRequest(tutorId, action);

    // Sends the request to the server
    return sendRequest(request);
  }
}
