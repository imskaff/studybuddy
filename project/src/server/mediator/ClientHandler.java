package server.mediator;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.dao.SessionDAOImpl;
import server.dao.UserDAOImpl;
import server.model.Session;
import server.model.SessionModel;
import server.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private final BufferedReader in;
    private final PrintWriter out;
    private final SessionModel model;
    private final Socket socket;
    private final Gson gson;

    public ClientHandler(Socket socket, SessionModel model) throws IOException {
        this.socket = socket;
        this.model = model;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.gson = new Gson();
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

    private void handleRequest(String line) {
        try {
            JsonObject obj = gson.fromJson(line, JsonObject.class);
            String type = obj.has("type") ? obj.get("type").getAsString() : "";

            switch (type) {
                case "BOOK_SESSION" -> handleBookSession(line);
                default -> sendResponse(new Response("ERROR", "Unknown request type: " + type));
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(new Response("ERROR", "Server error: " + e.getMessage()));
        }
    }

    private void handleBookSession(String line) throws Exception {
        BookSessionRequest request = gson.fromJson(line, BookSessionRequest.class);
        System.out.println("Request: " + request);

        Session session = SessionDAOImpl.getInstance().readByInt(request.getSessionId());
        Student student = (Student) UserDAOImpl.getInstance().readByInt(request.getStudentId());
        session.setStudent(student);
        SessionDAOImpl.getInstance().update(session);

        Response reply = new Response("OK", "Session booked");
        System.out.println("Reply: " + reply);
        sendResponse(reply);
    }

    private void sendResponse(Response response) {
        out.println(gson.toJson(response));
    }
}
