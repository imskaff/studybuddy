package server.mediator;

import com.google.gson.*;
import server.dao.SessionDAOImpl;
import server.dao.UserDAOImpl;
import server.model.*;

import java.util.List;
import java.util.ArrayList;
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
  private final ModelManager model;
  private final Socket socket;
  private final Gson gson;

  public ClientHandler(Socket socket, ModelManager model) throws IOException {
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
      // grab ip
      String clientIp = socket.getInetAddress().getHostAddress();

      LocalDateTime now = LocalDateTime.now();
      String date = String.valueOf(now.getDayOfMonth()) + "." + String.valueOf(now.getMonthValue()) + "."
          + String.valueOf(now.getYear());
      String time = String.valueOf(now.getHour()) + ":" + String.valueOf(now.getMinute()) + ":"
          + String.valueOf(now.getSecond());

      System.out.println("[" + clientIp + "] Client disconnected at " + date + " " + time);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        socket.close();
      } catch (IOException ignored) {
      }
    }
  }

  private void handleRequest(String clientText) throws Exception {
    RequestType requestType = gson.fromJson(clientText, RequestType.class);

    if (requestType.getType().equals("BOOK_SESSION")) {
      handleBookSession(clientText);
    } else if (requestType.getType().equals("CREATE_SESSION")) {
      handleCreateSession(clientText);
    } else if (requestType.getType().equals("EDIT_SESSION")) {
      handleEditSession(clientText);
    } else if (requestType.getType().equals("DELETE_SESSION")) {
      handleDeleteSession(clientText);
    } else if (requestType.getType().equals("CREATE_STUDENT_PROFILE")) {
      handleCreateStudentProfile(clientText);
    } else if (requestType.getType().equals("CREATE_TUTOR_PROFILE")) {
      handleCreateTutorProfile(clientText);
    } else if (requestType.getType().equals("HANDLE_TUTOR_REGISTRATION")) {
      handleTutorRegistration(clientText);
    } else if (requestType.getType().equals("GET_AVAILABLE_SESSIONS")) {
      handleGetAvailableSessions();
    } else if (requestType.getType().equals("GET_BOOKED_SESSIONS")) {
      handleGetBookedSessions(clientText);
    } else if (requestType.getType().equals("GET_PENDING_TUTORS")) {
      handleGetPendingTutors();
    } else if (requestType.getType().equals("GET_SUBJECTS")) {
      handleGetSubjects();
    } else if (requestType.getType().equals("LOGIN")) {
      handleLogin(clientText);
    } else if(requestType.getType().equals("EDIT_STUDENT_PROFILE")){
      handleEditStudentProfile(clientText);
    }else if(requestType.getType().equals("EDIT_TUTOR_PROFILE")){
      handleEditTutorProfile(clientText);
    }else if(requestType.getType().equals("CANCEL_SESSION"))
    {
      handleCancelSession(clientText);
    }else if(requestType.getType().equals("GET_TUTOR_SESSIONS"))
    {
      handleGetTutorSessions(clientText);
    }
    else {
      sendResponse(new Response("ERROR", "Unknown request type"));
    }
  }

  private void handleBookSession(String clientText)
  {
    BookSessionRequest request =
        gson.fromJson(clientText, BookSessionRequest.class);

    model.bookSession(request.getStudentId(), request.getSessionId());

    sendResponse(new Response("OK", "Session booked"));
  }

  private void handleCreateSession(String clientText) {
    CreateSessionRequest request = gson.fromJson(clientText, CreateSessionRequest.class);

    boolean created = model.createSession(request.getSession());
    if (created) {
      sendResponse(new Response("OK", "Session created"));
    }
    else {
      sendResponse(new Response("ERROR", "Failed to create session"));
    }
  }

  private void handleEditSession(String clientText) {
    EditSessionRequest request = gson.fromJson(clientText, EditSessionRequest.class);

    boolean edited = model.editSession(request.getSession());

    if (edited) {
      sendResponse(new Response("OK", "Session updated"));
    }
    else {
      sendResponse(new Response("ERROR",
          "One or more edited fields are empty or invalid"));
    }
  }

  private void handleDeleteSession(String clientText) {
    DeleteSessionRequest request = gson.fromJson(clientText, DeleteSessionRequest.class);

    boolean deleted = model.deleteSession(request.getSessionId());
    if (deleted) {
      sendResponse(new Response("OK", "Session deleted"));
    }
    else {
      sendResponse(new Response("ERROR", "Deletion was not confirmed"));
    }
  }

  private void handleCreateStudentProfile(String clientText) {
    CreateStudentProfileRequest request = gson.fromJson(clientText, CreateStudentProfileRequest.class);

    if (!request.isRulesConfirmed()) {
      sendResponse(new Response("ERROR", "The rules must be confirmed before proceeding"));
    }
    else if (model.createStudentProfile(request.getStudent())) {
      sendResponse(new Response("OK", "Student profile created"));
    }
    else {
      sendResponse(new Response("ERROR", "One or more fields are wrong, missing, or already used"));
    }
  }

  private void handleCreateTutorProfile(String clientText) {
    CreateTutorProfileRequest request = gson.fromJson(clientText, CreateTutorProfileRequest.class);

    if (!request.isRulesConfirmed()){
      sendResponse(new Response("ERROR", "The rules must be confirmed before proceeding"));
    }
    else if (model.createTutorProfile(request.getTutor())) {
      sendResponse(new Response("OK", "Tutor registration request sent for approval"));
    } else {
      sendResponse(new Response("ERROR", "One or more fields are wrong, missing, or already used"));
    }
  }

  private void handleTutorRegistration(String clientText) throws Exception {
    HandleTutorRegistrationRequest request = gson.fromJson(clientText, HandleTutorRegistrationRequest.class);

    if ("APPROVE".equals(request.getAction())) {
      UserDAOImpl.getInstance().updateApprovalStatus(request.getTutorId(), true, request.getAdminId());
      sendResponse(new Response("APPROVED", "Tutor profile approved"));
    } else if ("DECLINE".equals(request.getAction())) {
      UserDAOImpl.getInstance().declineTutor(request.getTutorId());
      sendResponse(new Response("DECLINED", "Tutor profile declined"));
    } else {
      sendResponse(new Response("ERROR", "Unknown action"));
    }
  }

  private void handleEditStudentProfile(String clientText)
  {
    EditStudentProfileRequest request = gson.fromJson(clientText, EditStudentProfileRequest.class);

    boolean updated = model.editStudentProfile(request.getStudent());

    if(updated){
      sendResponse(new Response("OK", "Student profile updated"));
    }
    else{
      sendResponse(new Response("ERROR", "Missing or invalid Student information"));
    }
  }

  private void handleEditTutorProfile(String clientText){
    EditTutorProfileRequest request = gson.fromJson(clientText, EditTutorProfileRequest.class);

    boolean updated = model.editTutorProfile(request.getTutor());

    if(updated) {
      sendResponse(new Response("OK", "Tutor profile updated"));
    }
    else {
      sendResponse(new Response("ERROR", "Missing or invalid tutor information"));
    }
  }

  private void handleCancelSession(String clientText)
  {
    CancelSessionRequest request = gson.fromJson(clientText, CancelSessionRequest.class);

    boolean cancelled = model.cancelSession(request.getUserId(),
            request.getSessionId(), request.getUserType());

    if(cancelled && request.getUserType().equalsIgnoreCase("student"))
    {
      sendResponse(new Response("OK", "Student booking cancelled. Session is now available"));
    }
    else if(cancelled && request.getUserType().equalsIgnoreCase("tutor"))
    {
      sendResponse(new Response("OK", "Session status changed to cancelled"));
    }
    else
    {
      sendResponse(new Response("ERROR", "Session could not be cancelled"));
    }
  }

  // Handles a request to get all available (unbooked) sessions
  private void handleGetAvailableSessions() throws Exception {
    ArrayList<Session> sessions = new ArrayList<>(SessionDAOImpl.getInstance().readAvailable());
    SessionListResponse response = new SessionListResponse("OK", "Available sessions", sessions);
    sendJsonResponse(response);
  }

  // Handles a request to get all sessions booked by a specific student
  private void handleGetBookedSessions(String clientText) throws Exception {
    GetBookedSessionsRequest request = gson.fromJson(clientText, GetBookedSessionsRequest.class);
    ArrayList<Session> sessions = new ArrayList<>(
        SessionDAOImpl.getInstance().readByStudent(request.getStudentId()));
    SessionListResponse response = new SessionListResponse("OK", "Booked sessions", sessions);
    sendJsonResponse(response);
  }

  private void handleGetSubjects() throws Exception {
    List<String> subjects = model.getSubjects();
    SubjectsResponse response = new SubjectsResponse("OK", subjects);
    sendJsonResponse(response);
  }

  // Handles a request to get all pending tutor registrations
  private void handleGetPendingTutors() throws Exception {
    ArrayList<server.model.Tutor> tutors = UserDAOImpl.getInstance().readPendingTutors();
    TutorListResponse response = new TutorListResponse("OK", "Pending tutors", tutors);
    sendJsonResponse(response);
  }

  private void handleLogin(String clientText) throws Exception {
    LoginRequest request = gson.fromJson(clientText, LoginRequest.class);
    String email = request.getEmail();
    String password = request.getPassword();

    User user = UserDAOImpl.getInstance().readByEmailAndPassword(email, password);
    if (user instanceof Student student) {
      sendJsonResponse(LoginResponse.ok("student", gson.toJson(student)));
    } else if (user instanceof Tutor tutor) {
      if (!tutor.isApproved()) {
        sendJsonResponse(LoginResponse.ok("tutor_pending", gson.toJson(tutor)));
      } else {
        sendJsonResponse(LoginResponse.ok("tutor", gson.toJson(tutor)));
      }
    } else {
      int adminId = UserDAOImpl.getInstance().getAdminId(email, password);
      if (adminId != -1) {
        sendJsonResponse(LoginResponse.ok("admin", String.valueOf(adminId)));
      } else {
        sendJsonResponse(LoginResponse.error("Invalid email or password"));
      }
    }
  }

  private void handleGetTutorSessions(String clientText) throws Exception {
    GetTutorSessionsRequest request = gson.fromJson(clientText, GetTutorSessionsRequest.class);
    ArrayList<Session> sessions = new ArrayList<>(SessionDAOImpl.getInstance().readByTutor(request.getTutorId()));
    SessionListResponse response = new SessionListResponse("OK", "Tutor sessions", sessions);
    sendJsonResponse(response);
  }

  private void sendResponse(Response response) {
    System.out.println("Reply: " + response);

    String replyJson = gson.toJson(response);

    System.out.println("Server> " + replyJson);

    out.println(replyJson);
  }

  // Sends any object as a JSON response to the client.
  // Used for responses that are not the standard Response class
  // (e.g., SessionListResponse, TutorListResponse).
  private void sendJsonResponse(Object response) {
    System.out.println("Reply: " + response);

    String replyJson = gson.toJson(response);

    System.out.println("Server> " + replyJson);

    out.println(replyJson);
  }
}