package client.viewmodel;

import client.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Tutor;
import server.model.Student;


public class SessionViewModel implements PropertyChangeListener {
    private Model model;
    private ObservableList<Session> sessions;
    private Session selectedSession;
    private Student loggedInStudent;

    private final StringProperty description;
    private final StringProperty tutorName;
    private final StringProperty subject;
    private final StringProperty location;
    private final StringProperty duration;
    private final StringProperty price;

    public SessionViewModel(Model model) {
        this.model = model;
        this.sessions = FXCollections.observableArrayList();

        this.description = new SimpleStringProperty("");
        this.tutorName = new SimpleStringProperty("");
        this.subject = new SimpleStringProperty("");
        this.location = new SimpleStringProperty("");
        this.duration = new SimpleStringProperty("");
        this.price = new SimpleStringProperty("");
        model.addPropertyChangeListener(this);
        loadFromModel();
    }

    private void loadFromModel() {
        sessions.clear();
        sessions.addAll(model.getAllSessions());
    }

    public void loadSession(Session session) {
       this.selectedSession = session;
        if (session != null) {
            description.set(session.getDescription());
            tutorName.set(session.getTutor() != null
                ? session.getTutor().getFirst_name() + " " + session.getTutor().getLast_name()
                : "");
            subject.set(session.getSubject());
            location.set(session.getLocation());
            if (session.getStart_date() != null && session.getEnd_date() != null) {
                long minutes = Duration.between(session.getStart_date(), session.getEnd_date()).toMinutes();
                duration.set(minutes + " min");
            } else {
                duration.set("");
            }
            price.set(String.valueOf(session.getPrice()));
        }
    }

    public ObservableList<Session> getSessions() {
        return sessions;
    }

  public Session getSelectedSession()
  {
    return selectedSession;
  }

  public void setLoggedInStudent(Student loggedInStudent) {
    this.loggedInStudent = loggedInStudent;
  }

  public Tutor getSelectedTutor() {
    if (selectedSession == null) {
      return null;
    }

    return selectedSession.getTutor();
  }

  public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty tutorProperty() {
        return tutorName;
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public StringProperty locationProperty() {
        return location;
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public StringProperty priceProperty() {
        return price;
    }

    public void bookSession() {
      if (selectedSession == null) {
        System.out.println("No session selected");
        return;
      }

      Student loggedInStudent = model.getLoggedInStudent();

      if (loggedInStudent == null) {
        System.out.println("No student logged in");
        return;
      }

      try {
        Tutor sessionTutor = selectedSession.getTutor();

        model.bookSession(loggedInStudent, sessionTutor, selectedSession);

        System.out.println("Session booked");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "SessionAdded":
                sessions.add((Session) evt.getNewValue());
                break;
            case "SessionRemoved":
                sessions.remove((Session) evt.getNewValue());
                break;
        }
    }

}
