package server;

import server.dao.SessionDAO;
import server.dao.SessionDAOImpl;
import server.dao.UserDAO;
import server.dao.UserDAOImpl;
import server.mediator.ServerConnector;
import server.model.ModelManager;

/**
 * Application entry point for the server.
 * 
 * Wires the dependency graph: creates the DAO instances, injects them into
 * {@link ModelManager}, wraps the model in a {@link ServerConnector}, and
 * starts the server on a dedicated thread.
 * 
 *
 * @author
 */
public class ServerMain {

    /**
     * Starts the server by constructing the full object graph and launching
     * the {@link ServerConnector} thread.
     *
     * @param args command-line arguments (not used)
     * @throws Exception if the database connection or server socket cannot be opened
     */
    public static void main(String[] args) throws Exception {
        UserDAO userDAO = UserDAOImpl.getInstance();
        SessionDAO sessionDAO = new SessionDAOImpl(userDAO);
        ModelManager model = new ModelManager(sessionDAO, userDAO);
        ServerConnector server = new ServerConnector(model);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
