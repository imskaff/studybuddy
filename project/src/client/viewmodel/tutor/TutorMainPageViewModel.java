package client.viewmodel.tutor;

import client.model.Model;
import client.viewmodel.ViewState;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Tutor;

/**
 * ViewModel for the tutor main page.
 * 
 * Manages the observable list of the logged-in tutor's sessions and the
 * editable profile properties. Provides actions to save profile changes,
 * complete a session, and cancel a session. Registers as a model listener
 * for future reactive updates.
 * 
 *
 * @author
 */
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

  /**
   * Constructs a {@code TutorMainPageViewModel}, initialises all observable
   * properties, and registers as a model property-change listener.
   *
   * @param model     the client-side model
   * @param viewState shared navigation state
   */
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

  /**
   * Loads the tutor's sessions and profile data from the model.
   */
  public void loadData()
  {
      loadSessions();
      Tutor tutor = model.getLoggedInTutor();
      if (tutor != null) loadProfile(tutor);
  }

  /**
   * Replaces the sessions list with the current tutor's sessions from the server.
   */
  public void loadSessions()
  {
      sessions.setAll(model.getTutorSessions());
  }

  /**
   * Populates the editable profile properties from the given tutor object.
   *
   * @param tutor the tutor whose data should be loaded
   */
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

  /**
   * Populates all editable properties (including password) from the currently
   * logged-in tutor, using {@link Tutor#getPassword()} for the password field.
   */
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

  /**
   * Validates the current property values and, if valid, sends a profile
   * update to the server. Uses the password already stored on the logged-in
   * tutor object (does not allow changing the password via this method).
   *
   * @return {@code true} if the update succeeded; {@code false} if no tutor
   *         is logged in, the date format is invalid, or the server rejects
   *         the update
   */
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

  /**
   * Returns the observable list of the tutor's sessions.
   *
   * @return the sessions list
   */
  public ObservableList<Session> getSessions() { return sessions; }

  /** @return the first-name property */
  public StringProperty firstNameProperty() { return firstName; }
  /** @return the last-name property */
  public StringProperty lastNameProperty() { return lastName; }
  /** @return the date-of-birth property (ISO-8601 string) */
  public StringProperty dateOfBirthProperty() { return dateOfBirth; }
  /** @return the email property */
  public StringProperty emailProperty() { return email; }
  /** @return the phone-number property */
  public StringProperty phoneProperty() { return phone; }
  /** @return the gender property */
  public StringProperty genderProperty() { return gender; }
  /** @return the languages property (comma-separated string) */
  public StringProperty languagesProperty() { return languages; }
  /** @return the education-level property */
  public StringProperty educationProperty() { return education; }
  /** @return the password property */
  public StringProperty passwordProperty() { return password; }

  /**
   * Builds a {@link Tutor} from the current property values and sends a
   * profile-update request to the server, preserving the existing password
   * and type.
   *
   * @return {@code true} if the update succeeded; {@code false} if no tutor
   *         is logged in, the date cannot be parsed, or the server rejects
   *         the update
   */
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

  /**
   * Marks the given session as complete. If the operation succeeds, the
   * sessions list is refreshed on the JavaFX application thread.
   *
   * @param session the session to complete; ignored if {@code null}
   */
  public void completeSession(Session session) {
      if (session != null) {
          boolean success = model.completeSession(session.getId());
          if (success) {
              Platform.runLater(this::loadSessions);
          }
      }
  }

  /**
   * Cancels the given session on behalf of the logged-in tutor. If the
   * operation succeeds, the sessions list is refreshed on the JavaFX
   * application thread.
   *
   * @param session the session to cancel; ignored if {@code null}
   */
  public void cancelSession(Session session) {
    Tutor tutor = model.getLoggedInTutor();
    if (session != null && tutor != null) {
      boolean success = model.cancelSession(tutor.getId(), session.getId(), "tutor");
      if (success) {
        Platform.runLater(this::loadSessions);
      }
    }
  }

  /**
   * No-op implementation — this ViewModel does not currently react to
   * model property-change events.
   *
   * @param evt the property-change event (ignored)
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
  }
}
