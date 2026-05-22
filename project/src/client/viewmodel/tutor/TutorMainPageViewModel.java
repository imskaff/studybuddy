package client.viewmodel.tutor;

import client.model.Model;
import client.viewmodel.ViewState;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class TutorMainPageViewModel implements PropertyChangeListener
{
  private final Model model;
  private final ViewState viewState;

    private final ObservableList<Session> sessions;

  private final StringProperty firstName;
  private final StringProperty lastName;
  private final StringProperty dateOfBirth;
  private final StringProperty email;
  private final StringProperty password;
  private final StringProperty phone;
  private final StringProperty gender;
  private final StringProperty languages;
  private final StringProperty education;

  public TutorMainPageViewModel(Model model, ViewState viewState) {
      this.model = model;
      this.viewState = viewState;
      sessions = FXCollections.observableArrayList();
      firstName = new SimpleStringProperty("");
      lastName = new SimpleStringProperty("");
      dateOfBirth = new SimpleStringProperty("");
      email = new SimpleStringProperty("");
      password = new SimpleStringProperty("");
      phone = new SimpleStringProperty("");
      gender = new SimpleStringProperty("");
      languages = new SimpleStringProperty("");
      education = new SimpleStringProperty("");
      model.addPropertyChangeListener(this);
  }

    public void loadData()
    {
        loadSessions();
        Tutor tutor = model.getLoggedInTutor();
        if (tutor != null) loadProfile(tutor);
    }

    public void loadSessions()
    {
        sessions.setAll(model.getAllSessions());
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
        education.set(tutor.getEducation_level());
    }

  public void loadTutorData()
  {
    Tutor currentTutor = model.getLoggedInTutor();

    if(currentTutor != null)
    {
      firstName.set(currentTutor.getFirst_name());
      lastName.set(currentTutor.getLast_name());
      email.set(currentTutor.getEmail());
      password.set(currentTutor.getPassword());
      phone.set(currentTutor.getPhone());
      gender.set(currentTutor.getGender());
      languages.set(currentTutor.getLanguages().toString());
      education.set(currentTutor.getEducation_level());

      if(currentTutor.getDate_of_birth() != null)
      {
        dateOfBirth.set(currentTutor.getDate_of_birth().toString());
      }
    }
  }

  public boolean saveChanges()
  {
    try{
      Tutor tutor = model.getLoggedInTutor();

      if(tutor == null)
      {
        return false;
      }

      tutor.setFirst_name(firstName.get());
      tutor.setLast_name(lastName.get());
      tutor.setEmail(email.get());
      tutor.setPassword(password.get());
      tutor.setPhone(phone.get());
      tutor.setGender(gender.get());
      tutor.setEducation_level(education.get());

      String languagesText = languages.get();
      ArrayList<String> languagesList = new ArrayList<>(Arrays.asList(languagesText.split("\\s*,\\s*")));

      tutor.setLanguages(languagesList);

      try {
        tutor.setDateOfBirth(LocalDate.parse(dateOfBirth.get()));
      } catch (DateTimeParseException e) {
        System.out.println("Invalid date format. Expected YYYY-MM-DD.");
        return false;
      }

      return model.editTutorProfile(tutor);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return false;
  }

    public ObservableList<Session> getSessions() { return sessions; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty dateOfBirthProperty() { return dateOfBirth; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty languagesProperty() { return languages; }
    public StringProperty educationProperty() { return education; }
    public StringProperty passwordProperty()
    {
      return password;
    }

  public void completeSession(Session session) {
        if (session != null) {
            model.complete(session);
        }
    }

    public void bookSession(Session session) {
        if (session != null) {
            model.book(session);
        }
    }

    public void cancelSession(Session session) {
        if (session != null) {
            model.cancel(session);
        }
    }

    @Override public void propertyChange(PropertyChangeEvent evt)
    {
    }
}
