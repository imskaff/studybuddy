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

/**
 * Handles all communication with a single connected client over a persistent TCP socket.
 * 
 * Runs in its own thread. Each incoming JSON line is dispatched to the appropriate
 * private handler method via a {@code switch} on the request-type string. All responses
 * are written back to the client as JSON using Gson.
 * 
 *
 * @author
 */
public class ClientHandler implements Runnable {
    private final BufferedReader in;
    private final PrintWriter out;
    private final ModelManager model;
    private final Socket socket;
    private final Gson gson;

    /**
     * Constructs a {@code ClientHandler} for the given socket and shared model.
     *
     * @param socket the client's connected socket
     * @param model  the server-side business-logic model
     * @throws IOException if the input or output streams cannot be opened
     */
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

    /**
     * Reads lines from the client socket in a loop and dispatches each one to
     * {@link #handleRequest(String)}. Logs a disconnect message when the client
     * closes the connection.
     */
    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Client> " + line);
                handleRequest(line);
            }
        } catch (SocketException e) {
            String clientIp = socket.getInetAddress().getHostAddress();

            LocalDateTime now = LocalDateTime.now();
            String date = String.valueOf(now.getDayOfMonth()) + "." + String.valueOf(now.getMonthValue())
                + "." + String.valueOf(now.getYear());
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

    /**
     * Parses the request-type field from the incoming JSON and delegates to the
     * corresponding private handler method.
     *
     * @param clientText the raw JSON string received from the client
     * @throws Exception if an unhandled error occurs inside a handler
     */
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
            case "GET_TUTOR_SESSIONS":        handleGetTutorSessions(clientText);     break;
            default: sendResponse(new Response("ERROR", "Unknown request type"));
        }
    }

    /**
     * Handles a {@code BOOK_SESSION} request.
     *
     * @param clientText the raw JSON request
     */
    private void handleBookSession(String clientText)
    {
        BookSessionRequest request = gson.fromJson(clientText, BookSessionRequest.class);
        model.bookSession(request.getStudentId(), request.getSessionId());
        sendResponse(new Response("OK", "Session booked"));
    }

    /**
     * Handles a {@code CREATE_SESSION} request.
     *
     * @param clientText the raw JSON request
     */
    private void handleCreateSession(String clientText) {
        CreateSessionRequest request = gson.fromJson(clientText, CreateSessionRequest.class);
        boolean created = model.createSession(request.getSession());
        if (created) {
            sendResponse(new Response("OK", "Session created"));
        } else {
            sendResponse(new Response("ERROR", "Failed to create session"));
        }
    }

    /**
     * Handles an {@code EDIT_SESSION} request.
     *
     * @param clientText the raw JSON request
     */
    private void handleEditSession(String clientText) {
        EditSessionRequest request = gson.fromJson(clientText, EditSessionRequest.class);
        boolean edited = model.editSession(request.getSession());
        if (edited) {
            sendResponse(new Response("OK", "Session updated"));
        } else {
            sendResponse(new Response("ERROR", "One or more edited fields are empty or invalid"));
        }
    }

    /**
     * Handles a {@code DELETE_SESSION} request.
     *
     * @param clientText the raw JSON request
     */
    private void handleDeleteSession(String clientText) {
        DeleteSessionRequest request = gson.fromJson(clientText, DeleteSessionRequest.class);
        boolean deleted = model.deleteSession(request.getSessionId());
        if (deleted) {
            sendResponse(new Response("OK", "Session deleted"));
        } else {
            sendResponse(new Response("ERROR", "Deletion was not confirmed"));
        }
    }

    /**
     * Handles a {@code CREATE_STUDENT_PROFILE} request.
     * Registration is rejected if the rules have not been confirmed.
     *
     * @param clientText the raw JSON request
     */
    private void handleCreateStudentProfile(String clientText) {
        CreateStudentProfileRequest request = gson.fromJson(clientText, CreateStudentProfileRequest.class);
        if (!request.isRulesConfirmed()) {
            sendResponse(new Response("ERROR", "The rules must be confirmed before proceeding"));
        } else if (model.createStudentProfile(request.getStudent())) {
            sendResponse(new Response("OK", "Student profile created"));
        } else {
            sendResponse(new Response("ERROR", "One or more fields are wrong, missing, or already used"));
        }
    }

    /**
     * Handles a {@code CREATE_TUTOR_PROFILE} request.
     * Registration is rejected if the rules have not been confirmed.
     *
     * @param clientText the raw JSON request
     */
    private void handleCreateTutorProfile(String clientText) {
        CreateTutorProfileRequest request = gson.fromJson(clientText, CreateTutorProfileRequest.class);
        if (!request.isRulesConfirmed()) {
            sendResponse(new Response("ERROR", "The rules must be confirmed before proceeding"));
        } else if (model.createTutorProfile(request.getTutor())) {
            sendResponse(new Response("OK", "Tutor registration request sent for approval"));
        } else {
            sendResponse(new Response("ERROR", "One or more fields are wrong, missing, or already used"));
        }
    }

    /**
     * Handles a {@code HANDLE_TUTOR_REGISTRATION} request (approve or decline).
     *
     * @param clientText the raw JSON request
     */
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

    /**
     * Handles an {@code EDIT_STUDENT_PROFILE} request.
     *
     * @param clientText the raw JSON request
     */
    private void handleEditStudentProfile(String clientText)
    {
        EditStudentProfileRequest request = gson.fromJson(clientText, EditStudentProfileRequest.class);
        boolean updated = model.editStudentProfile(request.getStudent());
        if (updated) {
            sendResponse(new Response("OK", "Student profile updated"));
        } else {
            sendResponse(new Response("ERROR", "Missing or invalid Student information"));
        }
    }

    /**
     * Handles an {@code EDIT_TUTOR_PROFILE} request.
     *
     * @param clientText the raw JSON request
     */
    private void handleEditTutorProfile(String clientText) {
        EditTutorProfileRequest request = gson.fromJson(clientText, EditTutorProfileRequest.class);
        boolean updated = model.editTutorProfile(request.getTutor());
        if (updated) {
            sendResponse(new Response("OK", "Tutor profile updated"));
        } else {
            sendResponse(new Response("ERROR", "Missing or invalid tutor information"));
        }
    }

    /**
     * Handles a {@code CANCEL_SESSION} request.
     * Sends different success messages depending on whether the cancelling user is
     * a student (session reverts to Available) or a tutor (session moves to Cancelled).
     *
     * @param clientText the raw JSON request
     */
    private void handleCancelSession(String clientText)
    {
        CancelSessionRequest request = gson.fromJson(clientText, CancelSessionRequest.class);
        boolean cancelled = model.cancelSession(request.getUserId(), request.getSessionId(), request.getUserType());
        if (cancelled && request.getUserType().equalsIgnoreCase("student"))
        {
            sendResponse(new Response("OK", "Student booking cancelled. Session is now available"));
        }
        else if (cancelled && request.getUserType().equalsIgnoreCase("tutor"))
        {
            sendResponse(new Response("OK", "Session status changed to cancelled"));
        }
        else
        {
            sendResponse(new Response("ERROR", "Session could not be cancelled"));
        }
    }

    /**
     * Handles a {@code GET_AVAILABLE_SESSIONS} request and returns the list
     * of currently available sessions as a JSON response.
     */
    private void handleGetAvailableSessions() {
        ArrayList<Session> sessions = new ArrayList<>(model.getAvailableSessions());
        sendJsonResponse(new SessionListResponse("OK", "Available sessions", sessions));
    }

    /**
     * Handles a {@code GET_BOOKED_SESSIONS} request and returns the sessions
     * booked by the specified student.
     *
     * @param clientText the raw JSON request containing the student ID
     */
    private void handleGetBookedSessions(String clientText) {
        GetBookedSessionsRequest request = gson.fromJson(clientText, GetBookedSessionsRequest.class);
        ArrayList<Session> sessions = new ArrayList<>(model.getBookedSessions(request.getStudentId()));
        sendJsonResponse(new SessionListResponse("OK", "Booked sessions", sessions));
    }

    /**
     * Handles a {@code GET_SUBJECTS} request and returns all available subject names.
     *
     * @throws Exception if the subject query fails
     */
    private void handleGetSubjects() throws Exception {
        List<String> subjects = model.getSubjects();
        sendJsonResponse(new SubjectsResponse("OK", subjects));
    }

    /**
     * Handles a {@code GET_PENDING_TUTORS} request and returns all tutors
     * awaiting administrator approval.
     */
    private void handleGetPendingTutors() {
        ArrayList<Tutor> tutors = new ArrayList<>(model.getPendingTutors());
        sendJsonResponse(new TutorListResponse("OK", "Pending tutors", tutors));
    }

    /**
     * Handles a {@code LOGIN} request. Authenticates the user and returns either
     * a student, tutor, or admin login response with the appropriate user data.
     *
     * @param clientText the raw JSON request containing email and password
     * @throws Exception if an error occurs during authentication
     */
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

    /**
     * Handles a {@code COMPLETE_SESSION} request.
     *
     * @param clientText the raw JSON request containing the session ID
     */
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

    /**
     * Handles a {@code GET_TUTOR_SESSIONS} request and returns all sessions
     * belonging to the specified tutor.
     *
     * @param clientText the raw JSON request containing the tutor ID
     */
    private void handleGetTutorSessions(String clientText) {
        GetTutorSessionsRequest request = gson.fromJson(clientText, GetTutorSessionsRequest.class);
        ArrayList<Session> sessions = new ArrayList<>(model.getTutorSessions(request.getTutorId()));
        sendJsonResponse(new SessionListResponse("OK", "Tutor sessions", sessions));
    }

    /**
     * Serialises a standard {@link Response} to JSON and sends it to the client.
     *
     * @param response the response to send
     */
    private void sendResponse(Response response) {
        System.out.println("Reply: " + response);
        String replyJson = gson.toJson(response);
        System.out.println("Server> " + replyJson);
        out.println(replyJson);
    }

    /**
     * Serialises any object to JSON and sends it to the client.
     * Used for richer response types such as {@link SessionListResponse}
     * and {@link TutorListResponse}.
     *
     * @param response the object to serialise and send
     */
    private void sendJsonResponse(Object response) {
        System.out.println("Reply: " + response);
        String replyJson = gson.toJson(response);
        System.out.println("Server> " + replyJson);
        out.println(replyJson);
    }
}
