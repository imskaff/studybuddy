package client.viewmodel.tutor;

import client.model.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TutorMainPageViewModel implements PropertyChangeListener
{
    private final Model model;

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
    private final StringProperty description;

    public TutorMainPageViewModel(Model model) {
        this.model = model;
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
      loadBookedSessions();
      Student student = model.getLoggedInStudent();
      if (student != null) loadProfile(student);
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

  @Override public void propertyChange(PropertyChangeEvent evt)
  {

  }
}
