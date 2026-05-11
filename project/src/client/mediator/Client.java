package client.mediator;

import com.google.gson.Gson;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import server.mediator.BookSessionRequest;
import server.mediator.Response;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

public class Client implements ServerModel {

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

    @Override
    public Session bookSession(Student student, Tutor tutor, Session session) {
        if (socket == null || socket.isClosed()) {
            throw new IllegalStateException("Not connected to server");
        }
        try {
            Gson gson = new Gson();
            BookSessionRequest req = new BookSessionRequest(student.getId(), session.getId(), null, null);
            out.println(gson.toJson(req));

            String replyJson = in.readLine();
            Response reply = gson.fromJson(replyJson, Response.class);

            if ("OK".equals(reply.getStatus())) {
                session.setStudent(student);
                support.firePropertyChange("SessionBooked", null, session);
            } else {
                System.out.println("Booking failed: " + reply.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    @Override
    public boolean bookSession(int studentId, int sessionId, String date, String time) {
        return false;
    }

    @Override
    public ArrayList<Session> getAllSessions() {
        return new ArrayList<>();
    }
}
