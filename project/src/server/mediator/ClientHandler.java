package server.mediator;

import com.google.gson.*;
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
    switch (requestType.getType()) {
      case "BOOK_SESSION":              handleBookSession(clientText);          break;
      case "CREATE_SESSION":            handleCreateSession(clientText);        break;
      case "EDIT_SESSION":              handleEditSession(clientText);          break;
      case "DELETE_SESSION":            handleDeleteSession(clientText);        break;
      case "CREATE_STUDENT_PROFILE":    handleCreateStudentProfile(clientText); break;
      case "CREATE_TUTOR_PROFILE":      handleCreateTutorProfile(clientText);   break;
      case "HANDLE_TUTOR_REGISTRATION": handleTutorRegistration(clientText);    break;
      case "GET_AVAILABLE_SESSIONS":    handleGetAvailableSessions();           break;
      case "GET_BOOKED_SESSIONS":       handleGetBookedSessions(clientText);    break;
      case "GET_PENDING_TUTORS":        handleGetPendingTutors();               break;
      case "GET_SUBJECTS":              handleGetSubjects();                    break;
      case "LOGIN":                     handleLogin(clientText);                break;
      case "EDIT_STUDENT_PROFILE":      handleEditStudentProfile(clientText);   break;
      case "EDIT_TUTOR_PROFILE":        handleEditTutorProfile(clientText);     break;
      case "CANCEL_SESSION":            handleCancelSession(clientText);        break;
      case "COMPLETE_SESSION":           handleCompleteSession(clientText);      break;
      case "GET_TUTOR_SESSIONS":        handleGetTutorSessions(clientText);     break;
      default: sendResponse(new Response("ERROR", "Unknown request type"));
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

  private void handleTutorRegistration(String clientText) {
    HandleTutorRegistrationRequest request = gson.fromJson(clientText, HandleTutorRegistrationRequest.class);
    boolean ok = model.handleTutorRegistration(request.getTutorId(), request.getAction(), request.getAdminId());
    if ("APPROVE".equals(request.getAction()))
      sendResponse(new Response(ok ? "APPROVED" : "ERROR", ok ? "Tutor profile approved" : "Failed to approve"));
    else if ("DECLINE".equals(request.getAction()))
      sendResponse(new Response(ok ? "DECLINED" : "ERROR", ok ? "Tutor profile declined" : "Failed to decline"));
    else
      sendResponse(new Response("ERROR", "Unknown action"));
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

  private void handleGetAvailableSessions() {
    ArrayList<Session> sessions = new ArrayList<>(model.getAvailableSessions());
    sendJsonResponse(new SessionListResponse("OK", "Available sessions", sessions));
  }

  private void handleGetBookedSessions(String clientText) {
    GetBookedSessionsRequest request = gson.fromJson(clientText, GetBookedSessionsRequest.class);
    ArrayList<Session> sessions = new ArrayList<>(model.getBookedSessions(request.getStudentId()));
    sendJsonResponse(new SessionListResponse("OK", "Booked sessions", sessions));
  }

  private void handleGetSubjects() throws Exception {
    List<String> subjects = model.getSubjects();
    sendJsonResponse(new SubjectsResponse("OK", subjects));
  }

  private void handleGetPendingTutors() {
    ArrayList<Tutor> tutors = new ArrayList<>(model.getPendingTutors());
    sendJsonResponse(new TutorListResponse("OK", "Pending tutors", tutors));
  }

  private void handleLogin(String clientText) throws Exception {
    LoginRequest request = gson.fromJson(clientText, LoginRequest.class);
    User user = model.login(request.getEmail(), request.getPassword());
    if (user instanceof Student student) {
      sendJsonResponse(LoginResponse.ok("student", gson.toJson(student)));
    } else if (user instanceof Tutor tutor) {
      sendJsonResponse(LoginResponse.ok(tutor.isApproved() ? "tutor" : "tutor_pending", gson.toJson(tutor)));
    } else {
      int adminId = model.getAdminId(request.getEmail(), request.getPassword());
      sendJsonResponse(adminId != -1
          ? LoginResponse.ok("admin", String.valueOf(adminId))
          : LoginResponse.error("Invalid email or password"));
    }
  }

  private void handleCompleteSession(String clientText)
  {
    CompleteSessionRequest request = gson.fromJson(clientText, CompleteSessionRequest.class);

    boolean completed = model.completeSession(request.getSessionId());

    if (completed)
    {
      sendResponse(new Response("OK", "Session completed"));
    }
    else
    {
      sendResponse(new Response("ERROR", "Session could not be completed"));
    }
  }

  private void handleGetTutorSessions(String clientText) {
    GetTutorSessionsRequest request = gson.fromJson(clientText, GetTutorSessionsRequest.class);
    ArrayList<Session> sessions = new ArrayList<>(model.getTutorSessions(request.getTutorId()));
    sendJsonResponse(new SessionListResponse("OK", "Tutor sessions", sessions));
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