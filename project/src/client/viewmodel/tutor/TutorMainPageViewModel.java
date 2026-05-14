package client.viewmodel.tutor;

import client.model.Model;
import client.viewmodel.ViewState;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TutorMainPageViewModel implements PropertyChangeListener
{
  private final Model model;
  private final ViewState viewState;

  private final ObservableList<Session> sessions;
  private final ObservableList<Session> bookedSessions;

  private final StringProperty firstName;
  private final StringProperty lastName;
  private final StringProperty dateOfBirth;
  private final StringProperty email;
  private final StringProperty phone;
  private final StringProperty gender;
  private final StringProperty languages;
  private final StringProperty education;

  public TutorMainPageViewModel(Model model, ViewState viewState)
  {
    this.model = model;
    this.viewState = viewState;

    this.sessions = FXCollections.observableArrayList();
    this.bookedSessions = FXCollections.observableArrayList();

    firstName = new SimpleStringProperty("");
    lastName = new SimpleStringProperty("");
    dateOfBirth = new SimpleStringProperty("");
    email = new SimpleStringProperty("");
    phone = new SimpleStringProperty("");
    gender = new SimpleStringProperty("");
    languages = new SimpleStringProperty("");
    education = new SimpleStringProperty("");

    model.addPropertyChangeListener(this);
  }

  public void loadData()
  {
    loadSessions();
    loadBookedSessions();
    Tutor tutor = model.getLoggedInTutor();
    if (tutor != null)
      loadProfile(tutor);
  }

  public void loadSessions()
  {
    sessions.setAll(model.getAllSessions());
  }

  public void loadBookedSessions()
  {
    bookedSessions.setAll(model.getBookedSessions());
  }

  public void loadProfile(Tutor tutor)
  {
    firstName.set(tutor.getFirst_name());
    lastName.set(tutor.getLast_name());
    dateOfBirth.set(tutor.getDate_of_birth().toString());
    email.set(tutor.getEmail());
    phone.set(tutor.getPhone());
    gender.set(tutor.getGender());
    languages.set(String.join(", ", tutor.getLanguages()));
    education.set(String.join("\n", tutor.getEducation_level()));

  }

  public StringProperty firstNameProperty()
  {
    return firstName;
  }

  public StringProperty lastNameProperty()
  {
    return lastName;
  }

  public StringProperty dateOfBirthProperty()
  {
    return dateOfBirth;
  }

  public StringProperty emailProperty()
  {
    return email;
  }

  public StringProperty phoneProperty()
  {
    return phone;
  }

  public StringProperty genderProperty()
  {
    return gender;
  }

  public StringProperty languagesProperty()
  {
    return languages;
  }

  public StringProperty educationProperty()
  {
    return education;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if ("SessionBooked".equals(evt.getPropertyName()))
    {
      Session session = (Session) evt.getNewValue();
      Platform.runLater(() -> {
        sessions.remove(session);
        bookedSessions.add(session);
      });
    }
  }

  public ObservableList<Session> getSessions()
  {
    return sessions;
  }

 public void completeSession(Session session)
 {
   if(session != null)
   {
     model.complete(session);
   }
 }

 public void bookSession(Session session)
 {
   if(session != null)
   {
     model.book(session);
   }
 }

 public void cancelSession(Session session)
 {
   if(session != null)
   {
     model.cancel(session);
   }
 }

 public void removeSession(Session session)
 {
   if(session != null)
   {
     model.remove(session);
   }
 }
}
