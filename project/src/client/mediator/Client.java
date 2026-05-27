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
import java.util.List;

import com.google.gson.GsonBuilder;
import server.mediator.*;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

/**
 * TCP socket implementation of {@link ClientModel}.
 * Keeps a persistent connection to the server on port 4025 and handles
 * all request/response communication over JSON.
 * 
 * @author Daniela Vihovanet
 */
public class Client implements ClientModel
{
    private final String HOST = "localhost";
    private final int PORT = 4025;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson;

    private final PropertyChangeSupport support;

    /**
     * Constructs a Client. Call {@link #connect(String, int)} before using any other method.
     */
    public Client() {
        support = new PropertyChangeSupport(this);
    }

    /**
     * Registers a listener for property change events.
     *
     * @param listener the listener to add
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Opens the socket connection and initialises the JSON serializer.
     *
     * @param host the server hostname
     * @param port the server port
     * @throws Exception if the connection cannot be established
     */
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

    /**
     * Closes the socket connection.
     */
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

    /**
     * Serialises the request to JSON, sends it to the server and returns
     * {@code true} if the response status is {@code "OK"}, {@code "APPROVED"},
     * or {@code "DECLINED"}.
     *
     * @param request the request object to send
     * @return {@code true} if the server returned a success status
     */
    private boolean sendRequest(Object request)
    {
        try
        {
            String requestJson = gson.toJson(request);
            System.out.println("Client> " + requestJson);
            out.println(requestJson);

            String replyJson = in.readLine();
            System.out.println("Server> " + replyJson);

            Response reply = gson.fromJson(replyJson, Response.class);

            return "OK".equals(reply.getStatus())
                || "APPROVED".equals(reply.getStatus())
                || "DECLINED".equals(reply.getStatus());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Serialises the request to JSON, sends it and returns the raw JSON
     * response string. Used when the response needs to be deserialised into
     * a list or structured object.
     *
     * @param request the request object to send
     * @return the raw JSON response, or {@code null} if communication fails
     */
    private String sendRequestForResponse(Object request)
    {
        try
        {
            String requestJson = gson.toJson(request);
            System.out.println("Client> " + requestJson);
            out.println(requestJson);

            String replyJson = in.readLine();
            System.out.println("Server> " + replyJson);

            return replyJson;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sends a book-session request to the server.
     *
     * @param studentId the student's ID
     * @param sessionId the session's ID
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean bookSession(int studentId, int sessionId)
    {
        return sendRequest(new BookSessionRequest(studentId, sessionId));
    }

    /**
     * Sends a create-session request to the server.
     *
     * @param session the session to create
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean createSession(Session session)
    {
        return sendRequest(new CreateSessionRequest(session));
    }

    /**
     * Sends an edit-session request to the server.
     *
     * @param tutorId the ID of the tutor making the edit
     * @param session the updated session
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean editSession(int tutorId, Session session)
    {
        return sendRequest(new EditSessionRequest(tutorId, session));
    }

    /**
     * Sends a delete-session request to the server.
     *
     * @param sessionId the ID of the session to delete
     * @param tutorId   the ID of the tutor requesting the deletion
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean deleteSession(int sessionId, int tutorId)
    {
        return sendRequest(new DeleteSessionRequest(sessionId, tutorId));
    }

    /**
     * Sends a student registration request to the server.
     *
     * @param student        the student to register
     * @param rulesConfirmed whether the student accepted the platform rules
     * @return {@code true} if the server accepted the request
     */
    @Override public boolean createStudentProfile(Student student, boolean rulesConfirmed)
    {
        return sendRequest(new CreateStudentProfileRequest(student, rulesConfirmed));
    }

    /**
     * Sends a tutor registration request to the server.
     *
     * @param tutor          the tutor to register
     * @param rulesConfirmed whether the tutor accepted the platform rules
     * @return {@code true} if the server accepted the request
     */
    @Override public boolean createTutorProfile(Tutor tutor, boolean rulesConfirmed)
    {
        return sendRequest(new CreateTutorProfileRequest(tutor, rulesConfirmed));
    }

    /**
     * Sends an approve or decline request for the given tutor.
     *
     * @param tutorId the ID of the tutor
     * @param action  {@code "APPROVE"} or {@code "DECLINE"}
     * @param adminId the ID of the admin making the decision
     * @return {@code true} if the server accepted the request
     */
    @Override public boolean handleTutorRegistration(int tutorId, String action, int adminId)
    {
        return sendRequest(new HandleTutorRegistrationRequest(tutorId, action, adminId));
    }

    /**
     * Sends a student profile update request to the server.
     *
     * @param student the updated student
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean editStudentProfile(Student student)
    {
        return sendRequest(new EditStudentProfileRequest(student));
    }

    /**
     * Sends a tutor profile update request to the server.
     *
     * @param tutor the updated tutor
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean editTutorProfile(Tutor tutor)
    {
        return sendRequest(new EditTutorProfileRequest(tutor));
    }

    /**
     * Sends a login request and returns the server's response.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return the login response from the server
     */
    @Override
    public LoginResponse login(String email, String password)
    {
        String replyJson = sendRequestForResponse(new LoginRequest(email, password));
        if (replyJson == null) return LoginResponse.error("Connection error");
        return gson.fromJson(replyJson, LoginResponse.class);
    }

    /**
     * Sends a cancel-session request to the server.
     *
     * @param userId    the ID of the user cancelling
     * @param sessionId the ID of the session to cancel
     * @param userType  {@code "student"} or {@code "tutor"}
     * @return {@code true} if the server accepted the request
     */
    @Override
    public boolean cancelSession(int userId, int sessionId, String userType)
    {
        return sendRequest(new CancelSessionRequest(userId, sessionId, userType));
    }

    /**
     * Fetches all sessions belonging to the given tutor.
     *
     * @param tutorId the tutor's ID
     * @return list of sessions, or an empty list on failure
     */
    @Override
    public ArrayList<Session> getTutorSessions(int tutorId)
    {
        String replyJson = sendRequestForResponse(new GetTutorSessionsRequest(tutorId));
        if (replyJson == null) return new ArrayList<>();
        SessionListResponse response = gson.fromJson(replyJson, SessionListResponse.class);
        if ("OK".equals(response.getStatus()) && response.getSessions() != null)
        {
            return response.getSessions();
        }
        return new ArrayList<>();
    }

    /**
     * Fetches all available sessions from the server.
     *
     * @return list of available sessions, or an empty list on failure
     */
    @Override
    public ArrayList<Session> getAvailableSessions()
    {
        String replyJson = sendRequestForResponse(new GetAvailableSessionsRequest());
        if (replyJson == null) return new ArrayList<>();
        SessionListResponse response = gson.fromJson(replyJson, SessionListResponse.class);
        if ("OK".equals(response.getStatus()) && response.getSessions() != null)
        {
            return response.getSessions();
        }
        return new ArrayList<>();
    }

    /**
     * Fetches all sessions booked by the given student.
     *
     * @param studentId the student's ID
     * @return list of booked sessions, or an empty list on failure
     */
    @Override
    public ArrayList<Session> getBookedSessions(int studentId)
    {
        String replyJson = sendRequestForResponse(new GetBookedSessionsRequest(studentId));
        if (replyJson == null) return new ArrayList<>();
        SessionListResponse response = gson.fromJson(replyJson, SessionListResponse.class);
        if ("OK".equals(response.getStatus()) && response.getSessions() != null)
        {
            return response.getSessions();
        }
        return new ArrayList<>();
    }

    /**
     * Fetches all available subjects from the server.
     *
     * @return list of subject names, or an empty list on failure
     */
    @Override
    public List<String> getSubjects()
    {
        String replyJson = sendRequestForResponse(new GetSubjectsRequest());
        if (replyJson == null) return new ArrayList<>();
        SubjectsResponse response = gson.fromJson(replyJson, SubjectsResponse.class);
        if ("OK".equals(response.getStatus()) && response.getSubjects() != null)
        {
            return response.getSubjects();
        }
        return new ArrayList<>();
    }

    /**
     * Fetches all tutors waiting for admin approval.
     *
     * @return list of pending tutors, or an empty list on failure
     */
    @Override
    public ArrayList<Tutor> getPendingTutors()
    {
        String replyJson = sendRequestForResponse(new GetPendingTutorsRequest());
        if (replyJson == null) return new ArrayList<>();
        TutorListResponse response = gson.fromJson(replyJson, TutorListResponse.class);
        if ("OK".equals(response.getStatus()) && response.getTutors() != null)
        {
            return response.getTutors();
        }
        return new ArrayList<>();
    }
}
