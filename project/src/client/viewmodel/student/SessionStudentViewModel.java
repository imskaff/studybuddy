package client.viewmodel.student;

import client.model.StudentModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Tutor;
import server.model.Student;

public class SessionStudentViewModel implements PropertyChangeListener
{
    private StudentModel model;
    private ObservableList<Session> sessions;
    private Session selectedSession;
    private final PropertyChangeSupport support;

    private final StringProperty description;
    private final StringProperty tutorName;
    private final StringProperty subject;
    private final StringProperty location;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final StringProperty price;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SessionStudentViewModel(StudentModel model)
    {
        this.model = model;
        this.sessions = FXCollections.observableArrayList();
        this.support = new PropertyChangeSupport(this);

        this.description = new SimpleStringProperty("");
        this.tutorName   = new SimpleStringProperty("");
        this.subject     = new SimpleStringProperty("");
        this.location    = new SimpleStringProperty("");
        this.startDate   = new SimpleStringProperty("");
        this.endDate     = new SimpleStringProperty("");
        this.price       = new SimpleStringProperty("");
        model.addPropertyChangeListener(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void loadSession(Session session)
    {
        this.selectedSession = session;
        if (session != null)
        {
            description.set(session.getDescription() != null ? session.getDescription() : "");
            tutorName.set(session.getTutor() != null
                ? session.getTutor().getFirst_name() + " " + session.getTutor().getLast_name()
                : "");
            subject.set(session.getSubject() != null ? session.getSubject() : "");
            location.set(session.getLocation() != null ? session.getLocation() : "");
            startDate.set(session.getStart_date() != null ? session.getStart_date().format(FMT) : "");
            endDate.set(session.getEnd_date()   != null ? session.getEnd_date().format(FMT)   : "");
            price.set(String.valueOf(session.getPrice()));
        }
    }

    public ObservableList<Session> getSessions() { return sessions; }

    public Session getSelectedSession() { return selectedSession; }

    public Tutor getSelectedTutor()
    {
        if (selectedSession == null) return null;
        return selectedSession.getTutor();
    }

    public StringProperty descriptionProperty() { return description; }
    public StringProperty tutorProperty()        { return tutorName;   }
    public StringProperty subjectProperty()      { return subject;     }
    public StringProperty locationProperty()     { return location;    }
    public StringProperty startDateProperty()    { return startDate;   }
    public StringProperty endDateProperty()      { return endDate;     }
    public StringProperty priceProperty()        { return price;       }

    public void bookSession()
    {
        if (selectedSession == null || model.getLoggedInStudent() == null) return;

        Student loggedInStudent = model.getLoggedInStudent();
        Thread t = new Thread(() -> {
            try {
                model.bookSession(loggedInStudent, selectedSession.getTutor(), selectedSession);
                support.firePropertyChange("bookingSucceeded", false, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
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
