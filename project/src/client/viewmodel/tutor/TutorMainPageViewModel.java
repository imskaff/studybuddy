package client.viewmodel.tutor;

import client.model.TutorModel;
import client.viewmodel.ViewState;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Tutor;

public class TutorMainPageViewModel implements PropertyChangeListener
{
  private final TutorModel model;
  private final ViewState viewState;

    private final ObservableList<Session> sessions;

  private final StringProperty firstName;
  private final StringProperty lastName;
  private final StringProperty dateOfBirth;
  private final StringProperty email;
  private final StringProperty phone;
  private final StringProperty gender;
  private final StringProperty languages;
  private final StringProperty education;
  private final StringProperty description;

  public TutorMainPageViewModel(TutorModel model, ViewState viewState)
  {
    this.model = model;
      this.viewState = viewState;
      sessions = FXCollections.observableArrayList();
      firstName = new SimpleStringProperty("");
      lastName = new SimpleStringProperty("");
      dateOfBirth = new SimpleStringProperty("");
      email = new SimpleStringProperty("");
      phone = new SimpleStringProperty("");
      gender = new SimpleStringProperty("");
      languages = new SimpleStringProperty("");
      education = new SimpleStringProperty("");
      description = new SimpleStringProperty("");
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
        sessions.setAll(model.getTutorSessions());
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

    public ObservableList<Session> getSessions() { return sessions; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty dateOfBirthProperty() { return dateOfBirth; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty languagesProperty() { return languages; }
    public StringProperty educationProperty() { return education; }


    public boolean saveProfile() {
        Tutor current = model.getLoggedInTutor();
        if (current == null) return false;

        LocalDate dob;
        try {
            dob = LocalDate.parse(dateOfBirth.get());
        } catch (Exception e) {
            return false;
        }

        ArrayList<String> langList = new ArrayList<>(
            Arrays.asList(languages.get().split("\\s*,\\s*")));

        Tutor updated = new Tutor(
            firstName.get().trim(), lastName.get().trim(), dob,
            email.get().trim(), phone.get().trim(),
            current.getPassword(), gender.get().trim(),
            langList, education.get().trim(), current.getType(), current.getId());

        return model.editTutorProfile(updated);
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
