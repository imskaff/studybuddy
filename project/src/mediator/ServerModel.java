package mediator;

import java.util.ArrayList;

import model.Session;
import model.Student;
import model.Tutor;

// client-side
public interface ServerModel {
    void connect();

    void disconnect();

    Session bookSession(Student student, Tutor tutor, Session session);

    ArrayList<Session> getAllSessions();

    String convert(String convert);
}
