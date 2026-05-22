package server;

import server.mediator.ServerConnector;
import server.model.SessionModel;
import server.model.ModelManager;

public class ServerMain {
    public static void main(String[] args) {
        SessionModel model = new ModelManager();
        ServerConnector server = new ServerConnector(
            (ModelManager) model);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
