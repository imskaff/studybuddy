package server;

import server.dao.SessionDAO;
import server.dao.SessionDAOImpl;
import server.dao.UserDAO;
import server.dao.UserDAOImpl;
import server.mediator.ServerConnector;
import server.model.ModelManager;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        UserDAO userDAO = UserDAOImpl.getInstance();
        SessionDAO sessionDAO = new SessionDAOImpl(userDAO);
        ModelManager model = new ModelManager(sessionDAO, userDAO);
        ServerConnector server = new ServerConnector(model);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
