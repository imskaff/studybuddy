package client.model;

import server.model.Session;
import server.model.Tutor;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public interface TutorModel {
    Tutor getLoggedInTutor();
    boolean editTutorProfile(Tutor tutor);
    boolean createSession(Session session);
    boolean editSession(Session session);
    boolean deleteSession(int sessionId);
    ArrayList<Session> getAllSessions();
    ArrayList<Session> getTutorSessions();
    List<String> getSubjects();
    
    // Add missing property change listeners();
    void addPropertyChangeListener(PropertyChangeListener listener);
    
    void complete(Session session);
    void book(Session session);
    void cancel(Session session);
    void remove(Session session);
}
