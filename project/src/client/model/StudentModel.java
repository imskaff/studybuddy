package client.model;

import server.model.Session;
import server.model.Student;
import server.model.Tutor;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public interface StudentModel {
    Student getLoggedInStudent();
    boolean editStudentProfile(Student student);
    ArrayList<Session> getBookedSessions();
    void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession) throws Exception;
    ArrayList<Session> getAllSessions();
    List<String> getSubjects();
    void addPropertyChangeListener(PropertyChangeListener listener);
}
