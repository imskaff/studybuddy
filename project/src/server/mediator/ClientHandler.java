package server.mediator;

import com.google.gson.*;
import server.dao.SessionDAOImpl;
import server.dao.UserDAOImpl;
import server.model.Session;
import server.model.SessionModel;
import server.model.SessionModelManager;
import server.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable {
    private final BufferedReader in;
    private final PrintWriter out;
    private final SessionModelManager model;
    private final Socket socket;
    private final Gson gson;

    public ClientHandler(Socket socket, SessionModelManager model) throws IOException {
        this.socket = socket;
        this.model = model;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
      gson = new GsonBuilder()
          .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
          .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
          .create();
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Client> " + line);
                handleRequest(line);
            }
        } catch (SocketException e) {
            System.out.println("Client disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

  private void handleRequest(String clientText) throws Exception
  {
    RequestType requestType =
        gson.fromJson(clientText, RequestType.class);

    if(requestType.getType().equals("BOOK_SESSION"))
    {
      handleBookSession(clientText);
    }
    else if(requestType.getType().equals("CREATE_SESSION"))
    {
      handleCreateSession(clientText);
    }
    else if(requestType.getType().equals("EDIT_SESSION"))
    {
      handleEditSession(clientText);
    }
    else if(requestType.getType().equals("DELETE_SESSION"))
    {
      handleDeleteSession(clientText);
    }
    else if(requestType.getType().equals("CREATE_STUDENT_PROFILE"))
    {
      handleCreateStudentProfile(clientText);
    }
    else if(requestType.getType().equals("CREATE_TUTOR_PROFILE"))
    {
      handleCreateTutorProfile(clientText);
    }
    else if(requestType.getType().equals("HANDLE_TUTOR_REGISTRATION"))
    {
      handleTutorRegistration(clientText);
    }
    else
    {
      sendResponse(new Response("ERROR", "Unknown request type"));
    }
  }

  private void handleBookSession(String clientText) throws Exception {
    BookSessionRequest request = gson.fromJson(clientText, BookSessionRequest.class);
    System.out.println("Request: " + request);

    Session session = SessionDAOImpl.getInstance().readByInt(request.getSessionId());
    Student student = (Student) UserDAOImpl.getInstance().readByInt(request.getStudentId());
    session.setStudent(student);
    SessionDAOImpl.getInstance().update(session);

    Response reply = new Response("OK", "Session booked");
    System.out.println("Reply: " + reply);
    sendResponse(reply);
  }

  private void handleCreateSession(String clientText)
  {
    CreateSessionRequest request =
        gson.fromJson(clientText, CreateSessionRequest.class);

    boolean created =
        model.createSession(request.getSession());

    if(created)
    {
      sendResponse(new Response("OK", "Session created"));
    }
    else
    {
      sendResponse(new Response("ERROR",
          "One or more required fields are empty or invalid"));
    }
  }

  private void handleEditSession(String clientText)
  {
    EditSessionRequest request =
        gson.fromJson(clientText, EditSessionRequest.class);

    boolean edited =
        model.editSession(request.getSession());

    if(edited)
    {
      sendResponse(new Response("OK", "Session updated"));
    }
    else
    {
      sendResponse(new Response("ERROR",
          "One or more edited fields are empty or invalid"));
    }
  }

  private void handleDeleteSession(String clientText)
  {
    DeleteSessionRequest request =
        gson.fromJson(clientText, DeleteSessionRequest.class);

    boolean deleted =
        model.deleteSession(request.getSessionId());

    if(deleted)
    {
      sendResponse(new Response("OK", "Session deleted"));
    }
    else
    {
      sendResponse(new Response("ERROR", "Deletion was not confirmed"));
    }
  }

  private void handleCreateStudentProfile(String clientText)
  {
    CreateStudentProfileRequest request =
        gson.fromJson(clientText, CreateStudentProfileRequest.class);

    if(!request.isRulesConfirmed())
    {
      sendResponse(new Response("ERROR",
          "The rules must be confirmed before proceeding"));
    }
    else if(model.createStudentProfile(request.getStudent()))
    {
      sendResponse(new Response("OK", "Student profile created"));
    }
    else
    {
      sendResponse(new Response("ERROR",
          "One or more fields are wrong, missing, or already used"));
    }
  }

  private void handleCreateTutorProfile(String clientText)
  {
    CreateTutorProfileRequest request =
        gson.fromJson(clientText, CreateTutorProfileRequest.class);

    if(!request.isRulesConfirmed())
    {
      sendResponse(new Response("ERROR",
          "The rules must be confirmed before proceeding"));
    }
    else if(model.createTutorProfile(request.getTutor()))
    {
      sendResponse(new Response("OK",
          "Tutor registration request sent for approval"));
    }
    else
    {
      sendResponse(new Response("ERROR",
          "One or more fields are wrong, missing, or already used"));
    }
  }

  private void handleTutorRegistration(String clientText)
  {
    HandleTutorRegistrationRequest request =
        gson.fromJson(clientText,
            HandleTutorRegistrationRequest.class);

    boolean handled =
        model.handleTutorRegistration(
            request.getTutorId(),
            request.getAction());

    if(handled && request.getAction().equals("APPROVE"))
    {
      sendResponse(new Response("APPROVED",
          "Tutor profile approved"));
    }
    else if(handled && request.getAction().equals("DECLINE"))
    {
      sendResponse(new Response("DECLINED",
          "Tutor profile declined"));
    }
    else
    {
      sendResponse(new Response("ERROR",
          "No pending tutor registration request exists"));
    }
  }


  private void sendResponse(Response response)
  {
    System.out.println("Reply: " + response);

    String replyJson = gson.toJson(response);

    System.out.println("Server> " + replyJson);

    out.println(replyJson);
  }

}
