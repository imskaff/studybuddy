package client.model;

import client.mediator.ServerModel;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

public class ModelManager implements Model {
    private ServerModel client;

    @Override
    public void connect() throws Exception {
        client.connect("localhost", 4025);
    }

    @Override
    public void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession) throws Exception {

    }

    @Override
    public Session getAllSessions() {
        return null;
    }
}
