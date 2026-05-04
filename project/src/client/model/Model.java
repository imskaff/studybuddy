package client.model;

import server.model.Session;
import server.model.Student;
import server.model.Tutor;

public interface Model {

    void connect() throws Exception;

    void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession) throws Exception;

    Session getAllSessions();
}
