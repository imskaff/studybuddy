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
  private final String HOST = "localhost";
  private final int PORT = 4025;
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

 /*@Override
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
  */

  // This helper method is used to send any request object to the server.
  // It converts the request into JSON, sends it through the socket,
  // receives the server response, converts the response from JSON,
  // and returns true if the operation was successful.
  private boolean sendRequest(Object request)
  {
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
        new LocalDateAdapter()).registerTypeAdapter(LocalDateTime.class,
        new LocalDateTimeAdapter()).create();

    try
    {
      Socket socket = new Socket(HOST, PORT);

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

      String requestJson = gson.toJson(request);

      System.out.println("Client> " + requestJson);

      out.println(requestJson);

      String replyJson = in.readLine();

      System.out.println("Server> " + replyJson);

      Response reply = gson.fromJson(replyJson, Response.class);

      socket.close();

      return "OK".equals(reply.getStatus())
          || "APPROVED".equals(reply.getStatus())
          || "DECLINED".equals(reply.getStatus());
    }
    catch(IOException e)
    {
      e.printStackTrace();

      return false;
    }
  }

  // Sends a booking request to the server
  @Override
  public boolean bookSession(int studentId, int sessionId)
  {
    BookSessionRequest request = new BookSessionRequest(studentId, sessionId);

    return sendRequest(request);
  }

  @Override
  public boolean createSession(Session session)
  {
    CreateSessionRequest request = new CreateSessionRequest(session);

    return sendRequest(request);
  }

  @Override
  public boolean editSession(int tutorId, Session session)
  {
    EditSessionRequest request = new EditSessionRequest(tutorId, session);

    return sendRequest(request);
  }

  @Override
  public boolean deleteSession(int sessionId, int tutorId)
  {
    DeleteSessionRequest request = new DeleteSessionRequest(sessionId, tutorId);

    return sendRequest(request);
  }

  @Override public boolean createStudentProfile(Student student,
      boolean rulesConfirmed)
  {
    CreateStudentProfileRequest request = new CreateStudentProfileRequest(student, rulesConfirmed);

    return sendRequest(request);
  }

  @Override public boolean createTutorProfile(Tutor tutor,
      boolean rulesConfirmed)
  {
    CreateTutorProfileRequest request = new CreateTutorProfileRequest(tutor, rulesConfirmed);

    return sendRequest(request);
  }

  @Override public boolean handleTutorRegistration(int tutorId, String action)
  {
    HandleTutorRegistrationRequest request =
        new HandleTutorRegistrationRequest(tutorId, action);

    return sendRequest(request);
  }
}
