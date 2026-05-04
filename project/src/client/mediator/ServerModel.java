package client.mediator;

import java.util.ArrayList;

import server.model.Session;
import server.model.Student;
import server.model.Tutor;

// client-side
public interface ServerModel {
    void connect(String host, int port) throws Exception;

    void disconnect() throws Exception;

    Session bookSession(Student student, Tutor tutor, Session session);

    ArrayList<Session> getAllSessions();

    boolean bookSession(int studentId, int sessionId, String date, String time);
}
