package server;

import server.dao.SessionDAOImpl;
import server.dao.UserDAOImpl;
import server.mediator.ServerConnector;
import server.model.SessionModel;
import server.model.SessionModelManager;

import java.sql.SQLException;

public class ServerMain {
    public static void main(String[] args) {
        try {
            UserDAOImpl userDAO = new UserDAOImpl();
            SessionDAOImpl sessionDAO = new SessionDAOImpl(userDAO);
            SessionModel model = new SessionModelManager(sessionDAO, userDAO);
            ServerConnector server = new ServerConnector(model);
            Thread serverThread = new Thread(server);
            serverThread.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
