package client.mediator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

// This class is the client-side implementation of the SessionModel interface.
// It is responsible for communicating with the server using sockets.
// It converts BookSessionRequest objects into JSON, sends them to the server,
// receives a JSON response, converts it into a Response object,
// and returns true or false based on the result.
public class Client implements ServerModel {

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

    @Override
    public void connect(String host, int port) throws Exception {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connected to server " + host + ":" + port);

        // TODO: gotta check how we actually did this part, commented for now - @adam
        // this is for receiving messages from the server asynchronously (e.g. for updates)

        // Thread receiverThread = new Thread(new ReceiverThread(in));
        // receiverThread.setDaemon(true);
        // receiverThread.start();
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
    public boolean bookSession(int studentId, int sessionId, String date, String time) {
        // // Used to convert Java objects <-> JSON
        // Gson gson = new Gson();
        //
        // try
        // {
        // // Creates a connection to the server
        // Socket socket = new Socket(HOST, PORT);
        //
        // // Input stream to read the server's reply
        // BufferedReader in = new BufferedReader(new
        // InputStreamReader(socket.getInputStream()));
        //
        // // Output stream to send data to the server
        // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //
        // // Create request object with booking data
        // BookSessionRequest request = new BookSessionRequest(studentId, sessionId,
        // date, time);
        //
        // // Convert request object into JSON
        // String requestJson = gson.toJson(request);
        //
        // // Print request for debugging
        // System.out.println("Client> " + requestJson);
        //
        // // Send JSON request to the server
        // out.println(requestJson);
        //
        // // Read JSON response from the server
        // String replyJson = in.readLine();
        //
        // // Print response for debugging
        // System.out.println("Server> " + replyJson);
        //
        // // Convert JSON response into Response object
        // Response reply = gson.fromJson(replyJson, Response.class);
        //
        // // Close the connection after request/reply
        // socket.close();
        //
        // // Return true if booking was successful, false otherwise
        // return "OK".equals(reply.getStatus());
        // }
        // catch (IOException e)
        // {
        // e.printStackTrace();
        // return false;
        // }
        return false;
    }

    @Override
    public Session bookSession(Student student, Tutor tutor, Session session) {
        // TODO: send request to server, return created session
        return null;
    }

    @Override
    public ArrayList<Session> getAllSessions() {
        // TODO: fetch from server
        return new ArrayList<>();
    }
}
