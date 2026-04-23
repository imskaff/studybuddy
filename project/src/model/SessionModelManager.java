package model;

import viewmodel.SessionViewModel;
import mediator.ServerModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class SessionModelManager implements SessionModel, PropertyChangeListener {
    private final SessionList list;
    private ServerModel serverModel;
    private PropertyChangeSupport support;

    public SessionModelManager() {
        this.list = new SessionList();
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
    public String convert(String convert) {
        return "";
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }
}
