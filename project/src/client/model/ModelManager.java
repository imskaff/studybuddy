package client.model;

import java.beans.PropertyChangeSupport;

import client.mediator.Client;
import client.mediator.ServerModel;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class ModelManager implements Model, PropertyChangeListener {
    private final ServerModel client;
    private final PropertyChangeSupport support;

    public ModelManager() {
        client = new Client();
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void connect() throws Exception {
        client.connect("localhost", 4025);
        client.addPropertyChangeListener(this);
    }

    @Override
    public void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession) throws Exception {
        client.bookSession(loggedInStudent, sessionTutor, bookedSession);
    }

    @Override
    public ArrayList<Session> getAllSessions() {
        return client.getAllSessions();
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
