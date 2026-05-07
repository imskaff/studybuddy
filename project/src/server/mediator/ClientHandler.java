package server.mediator;

import com.google.gson.Gson;
import server.model.SessionModel;
import server.model.*;
import server.temp.BookSessionRequest;
import server.temp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    // Used to read text coming from the client through the socket
    private final BufferedReader in;

    // Used to send text back to the client through the socket
    private final PrintWriter out;

    // Used to access the server-side booking logic
    private final SessionModel model;
    private final Socket socket;

    private Gson gson;

    public ClientHandler(Socket socket, SessionModel model) throws IOException {
        this.socket = socket;
        this.model = model;
        // Creates the input stream attached to the socket
        // It allows the server to receive data from the client
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Creates the output stream attached to the socket
        // It allows the server to send data back to the client
        this.out = new PrintWriter(socket.getOutputStream());

        this.gson = new Gson();
    }

    @Override
    public void run() {
        try {
            // Reads one line of text sent by the client --> JSON
            String clientText = in.readLine();

            // Prints the received JSON for debugging
            System.out.println("Client> " + clientText);

            // Converts the JSON text into a BookSessionRequest object
            BookSessionRequest request = gson.fromJson(clientText, BookSessionRequest.class);

            // Prints the converted request object for debugging
            System.out.println("Request: " + request);

            // This object will store the server's answer
            Response reply;

            if (request.getDate() == null || request.getTime() == null
                    || request.getDate().isEmpty() || request.getTime().isEmpty()) {
                // If date or time is missing, create an error response
                reply = new Response("ERROR", "Invalid date or/and time");
            } else {
                // If the request looks valid, create a success response
                reply = new Response("OK", "Booking confirmed");
            }

            System.out.println("Reply: " + reply);

            // Converts the Response object into JSON text
            String replyJson = gson.toJson(reply);

            // Prints the JSON response for debugging
            System.out.println("Server> " + replyJson);

            // Sends the JSON response back to the client
            out.println(replyJson);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
