package server;

import server.mediator.ServerConnector;
import server.model.SessionModel;
import server.model.SessionModelManager;

public class ServerMain {
    public static void main(String[] args) {
        SessionModel model = new SessionModelManager();
        ServerConnector server = new ServerConnector(
            (SessionModelManager) model);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
