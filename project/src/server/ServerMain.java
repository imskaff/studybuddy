package server;

import server.mediator.ServerConnector;
import server.model.ModelManager;

public class ServerMain {
    public static void main(String[] args) {
        ModelManager model = new ModelManager();
        ServerConnector server = new ServerConnector(model);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
