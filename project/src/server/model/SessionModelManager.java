package server.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class SessionModelManager implements SessionModel, PropertyChangeListener {
    private final SessionList list;
    private PropertyChangeSupport support;

    public SessionModelManager() {
        list = new SessionList();
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void bookSession(Student student, Tutor tutor, Session sessionId) {
        list.addSession(sessionId);
    }

    @Override
    public ArrayList<Session> getAllSessions() {
        return list.getAllSessions();
    }

    @Override
    public Session getSession(int index) {
        return list.getSession(index);
    }

    @Override
    public Session getSessionById(int id) {
        return list.getSessionById(id);
    }

    @Override
    public int getSize() {
        return list.getSize();
    }

    @Override
    public void removeSession(int index) {
        list.removeSession(index);
    }

    @Override
    public void removeSessionById(int id) {
        list.removeSessionById(id);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }
}
