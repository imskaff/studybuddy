package server.mediator;

import server.model.SessionModel;
import server.model.SessionModelManager;

import java.net.ServerSocket;
import java.net.Socket;
// The BookingServer class acts as the connecting class.
// It creates a server socket that listens on a specific port
// and waits for incoming client connections. When a client connects,
// a new ClientHandler is created and executed in a separate thread,
// allowing the server to handle multiple clients simultaneously.
public class ServerConnector implements Runnable{
    private static final int PORT = 4025;
    private SessionModelManager model;

    public ServerConnector(SessionModelManager model)
    {
        // Initializes the server-side model that contains the booking logic
        this.model = model;
    }

    // The run() method initializes and runs the server.
    // It opens a ServerSocket and continuously listens for client connections.
    // For each connected client, a new ClientHandler is created and executed in a separate thread,
    // allowing multiple clients to communicate with the server at the same time.
    public void run()
    {

        // Just prints a message so we know the server has started.
        System.out.println("Starting server...");
        try
        {
            // Creates the server socket and binds it to the port.
            ServerSocket listenSocket = new ServerSocket(PORT);

            // Infinite loop so the server keeps running and can accept multiple clients.
            while (true)
            {
                // Message to indicate the server is waiting for a connection.
                System.out.println("Waiting for a client...");

                // This method blocks (waits) until a client connects.
                Socket socket = listenSocket.accept();

                // Confirmation that a client has successfully connected.
                System.out.println("Client connected!");

                // Creates a new ClientHandler object to handle this specific client.
                // We pass the socket and the shared model.
                ClientHandler handler = new ClientHandler(socket, model);

                // Creates a new thread so multiple clients can be handled at the same time.
                Thread thread = new Thread(handler);

                // Starts the thread and runs the ClientHandler.
                thread.start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
