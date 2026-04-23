package viewmodel;

import model.Session;
import model.SessionModel;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SessionViewModel implements PropertyChangeListener
{
    private SessionModel model;
    private ObservableList<Session>sessions;

    public SessionViewModel(SessionModel model)
    {
        this.model = model;
        this.sessions = FXCollections.observableArrayList();

        this.model.addPropertyChangeListener(this);
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
                sessions.add((Session) evt.getNewValue());
                break;
        }
    }

}
