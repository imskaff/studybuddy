package client.viewmodel;

import client.model.Model;
import server.model.Session;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.beans.PropertyChangeEvent;

public class SessionViewModel implements PropertyChangeListener
{
    private Model model;
    private ObservableList<Session>sessions;
    private PropertyChangeSupport support;

    public SessionViewModel(Model model)
    {
        this.model = model;
        this.sessions = FXCollections.observableArrayList();

        support.addPropertyChangeListener(this);
    }

    private void loadFromModel()
    {
        sessions.clear();
        sessions.addAll(model.getAllSessions());
    }

    public ObservableList<Session> getSessions()
    {
        return sessions;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        switch (evt.getPropertyName())
        {
            case "SessionAdded":
                sessions.add((Session) evt.getNewValue());
                break;
            case "SessionRemoved":
                sessions.remove((Session) evt.getNewValue());
                break;
        }
    }

}
