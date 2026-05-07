package server.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public interface SessionModel {
    void bookSession(Student student, Tutor tutor, Session sessionId);

    ArrayList<Session> getAllSessions();

    Session getSession(int index);

    Session getSessionById(int index);

    void removeSessionById(int id);

    int getSize();

    void removeSession(int index);

    void addPropertyChangeListener(PropertyChangeListener listener);
}
