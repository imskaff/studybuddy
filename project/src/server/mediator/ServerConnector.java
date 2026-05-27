package server.mediator;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

import server.model.ModelManager;

/**
 * Listens for incoming TCP client connections and spawns a dedicated
 * {@link ClientHandler} thread for each one.
 * 
 * Implements the Mediator pattern on the server side: it decouples the
 * network layer from the business logic by passing every connected socket
 * to a {@link ClientHandler} together with the shared {@link ModelManager}.
 * 
 *
 * @author
 */
public class ServerConnector implements Runnable {
    private static final int PORT = 4025;
    private ModelManager model;

    /**
     * Constructs a {@code ServerConnector} with the given business-logic model.
     *
     * @param model the server-side model shared across all client handlers
     */
    public ServerConnector(ModelManager model)
    {
        this.model = model;
    }

    /**
     * Starts the server socket on port {@value #PORT} and enters an infinite
     * accept-loop. For every connecting client a new {@link ClientHandler} is
     * created and executed in a separate daemon thread, allowing the server to
     * serve multiple clients simultaneously.
     */
    public void run()
    {
        System.out.println("Starting server...");
        try
        {
            ServerSocket listenSocket = new ServerSocket(PORT);
            System.out.println("Running - waiting for a client...");

            while (true)
            {
                Socket socket = listenSocket.accept();

                String clientIp = socket.getInetAddress().getHostAddress();

                LocalDateTime now = LocalDateTime.now();
                String date = String.valueOf(now.getDayOfMonth()) + "." + String.valueOf(now.getMonthValue()) + "." + String.valueOf(now.getYear());
                String time = String.valueOf(now.getHour()) + ":" + String.valueOf(now.getMinute()) + ":" + String.valueOf(now.getSecond());

                System.out.println("[" + clientIp + "] Client connected at " + date + " " + time);

                ClientHandler handler = new ClientHandler(socket, model);
                Thread thread = new Thread(handler);
                thread.start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
