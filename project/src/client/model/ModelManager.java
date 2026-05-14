package client.model;

import java.beans.PropertyChangeSupport;

import client.mediator.Client;
import client.mediator.ClientModel;
import server.dao.SessionDAOImpl;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModelManager implements Model, PropertyChangeListener {
    private final ClientModel client;
    private final PropertyChangeSupport support;
    private Student loggedInStudent;

    public ModelManager() {
        client = new Client();
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void connect() throws Exception {
        client.addPropertyChangeListener(this);
    }

    @Override
    public void bookSession(Student loggedInStudent, Tutor sessionTutor, Session bookedSession) throws Exception {
        boolean success = client.bookSession(loggedInStudent.getId(), bookedSession.getId());
        if (success) {
            bookedSession.setStudent(loggedInStudent);
            support.firePropertyChange("SessionBooked", null, bookedSession);
        }
    }

    @Override
    public ArrayList<Session> getAllSessions() {
        try {
            return new ArrayList<>(SessionDAOImpl.getInstance().readAvailable());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load sessions: " + e.getMessage(), e);
        }
    }

    @Override
    public ArrayList<Session> getBookedSessions() {
        try {
            if (loggedInStudent == null) return new ArrayList<>();
            return new ArrayList<>(SessionDAOImpl.getInstance().readByStudent(loggedInStudent.getId()));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load booked sessions: " + e.getMessage(), e);
        }
    }

   @Override public void setLoggedInStudent(Student student)
   {
    this.loggedInStudent = student;
   }

   @Override public Student getLoggedInStudent()
   {
    return loggedInStudent;
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
