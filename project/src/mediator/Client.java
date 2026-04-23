package mediator;

import model.Session;
import model.Student;
import model.Tutor;
import java.util.ArrayList;

public class Client implements ServerModel {

    @Override
    public void connect() {
        // TODO: open socket connection to server
    }

    @Override
    public void disconnect() {
        // TODO: close connection
    }

    @Override
    public Session bookSession(Student student, Tutor tutor, Session session) {
        // TODO: send request to server, return created session
        return null;
    }

    @Override
    public ArrayList<Session> getAllSessions() {
        // TODO: fetch from server
        return new ArrayList<>();
    }

    @Override
    public String convert(String convert) {
        return "";
    }
}
