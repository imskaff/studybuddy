package model;

import viewmodel.SessionViewModel;

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

    // method used by client servermodel in the future
    String convert(String convert);

    void addPropertyChangeListener(PropertyChangeListener listener);
}
