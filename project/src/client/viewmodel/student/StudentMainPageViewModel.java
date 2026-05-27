package client.viewmodel.student;

import client.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import server.model.Session;
import server.model.Student;

/**
 * ViewModel for the student main page.
 * 
 * Manages observable lists of available and booked sessions, subject filter
 * options and the logged-in student's editable profile properties.
 * Listens for {@code SessionBooked} model events and updates the lists on the
 * JavaFX application thread accordingly.
 * 
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class StudentMainPageViewModel implements PropertyChangeListener
{
  /** Formatter used to parse date filter strings entered as {@code dd/MM/yyyy}. */
  private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(
      "dd/MM/yyyy");

  private final Model model;
  private final ObservableList<Session> sessions;
  private final FilteredList<Session> filteredSessions;
  private final ObservableList<Session> bookedSessions;
  private final ObservableList<String> subjectOptions;

  private final StringProperty subjectFilter;
  private final StringProperty minPrice;
  private final StringProperty maxPrice;
  private final StringProperty fromDate;
  private final StringProperty toDate;

  private final StringProperty firstName;
  private final StringProperty lastName;
  private final StringProperty dateOfBirth;
  private final StringProperty email;
  private final StringProperty password;
  private final StringProperty phone;
  private final StringProperty gender;
  private final StringProperty languages;

  /**
   * Constructs a {@code StudentMainPageViewModel}, initialises all observable
   * properties, wires filter-change listeners so that any filter modification
   * immediately re-evaluates the predicate and registers as a model listener.
   *
   * @param model the client-side model
   */
  public StudentMainPageViewModel(Model model)
  {
    this.model = model;
    this.sessions = FXCollections.observableArrayList();
    this.filteredSessions = new FilteredList<>(sessions, s -> true);
    this.bookedSessions = FXCollections.observableArrayList();
    this.subjectOptions = FXCollections.observableArrayList();

    subjectFilter = new SimpleStringProperty("All");
    minPrice = new SimpleStringProperty("");
    maxPrice = new SimpleStringProperty("");
    fromDate = new SimpleStringProperty("");
    toDate = new SimpleStringProperty("");

    firstName = new SimpleStringProperty("");
    lastName = new SimpleStringProperty("");
    dateOfBirth = new SimpleStringProperty("");
    email = new SimpleStringProperty("");
    password = new SimpleStringProperty("");
    phone = new SimpleStringProperty("");
    gender = new SimpleStringProperty("");
    languages = new SimpleStringProperty("");

    subjectFilter.addListener((obs, o, n) -> applyFilter());
    minPrice.addListener((obs, o, n) -> applyFilter());
    maxPrice.addListener((obs, o, n) -> applyFilter());
    fromDate.addListener((obs, o, n) -> applyFilter());
    toDate.addListener((obs, o, n) -> applyFilter());

    model.addPropertyChangeListener(this);
  }

  /**
   * Loads all data needed for the main page: subjects, available sessions,
   * booked sessions and the logged-in student's profile.
   */
  public void loadData()
  {
    loadSubjects();
    loadSessions();
    loadBookedSessions();
    Student student = model.getLoggedInStudent();
    if (student != null)
      loadProfile(student);
  }

  /**
   * Refreshes the subject options list from the server, always prepending
   * an {@code "All"} entry so the filter can be reset.
   */
  public void loadSubjects()
  {
    subjectOptions.clear();
    subjectOptions.add("All");
    subjectOptions.addAll(model.getSubjects());
  }

  /**
   * Replaces the available-sessions list with the current server data.
   */
  public void loadSessions()
  {
    sessions.setAll(model.getAllSessions());
  }

  /**
   * Replaces the booked-sessions list with the sessions the logged-in student
   * has already booked.
   */
  public void loadBookedSessions()
  {
    bookedSessions.setAll(model.getBookedSessions());
  }

  /**
   * Populates the editable profile properties from the given student object.
   *
   * @param student the student whose data should be loaded into the properties
   */
  public void loadProfile(Student student) {
    firstName.set(student.getFirst_name());
    lastName.set(student.getLast_name());
    dateOfBirth.set(student.getDate_of_birth().toString());
    email.set(student.getEmail());
    phone.set(student.getPhone());
    gender.set(student.getGender());
    languages.set(String.join(", ", student.getLanguages()));
  }

  /**
   * Validates the current profile properties and, if valid, sends an update
   * to the server.
   *
   * @return {@code true} if the profile was saved successfully; {@code false}
   *         if no student is logged in, the date cannot be parsed, or the
   *         server rejects the update
   */
  public boolean saveProfile() {
    Student current = model.getLoggedInStudent();
    if (current == null) return false;

    LocalDate date;
    try {
      date = LocalDate.parse(dateOfBirth.get());
    } catch (Exception e) {
      return false;
    }

    ArrayList<String> langList = new ArrayList<>(
        Arrays.asList(languages.get().split("\\s*,\\s*")));

    Student updated = new Student(
        firstName.get().trim(), lastName.get().trim(), date,
        email.get().trim(), phone.get().trim(),
        current.getPassword(), gender.get().trim(),
        langList, current.getId(), "Student");

    return model.editStudentProfile(updated);
  }

  /**
   * Resets all filter properties to their default (empty / {@code "All"}) values,
   * which causes {@link #applyFilter()} to show every session.
   */
  public void resetFilters()
  {
    subjectFilter.set("All");
    minPrice.set("");
    maxPrice.set("");
    fromDate.set("");
    toDate.set("");
  }

  /**
   * Rebuilds the {@link FilteredList} predicate based on the current values of
   * the five filter properties. Invalid or blank filter values are silently
   * ignored, allowing partial filtering.
   */
  private void applyFilter()
  {
    filteredSessions.setPredicate(session -> {
      String subject = subjectFilter.get();
      if (subject != null && !subject.equals("All") && !subject.isBlank())
      {
        if (!subject.equals(session.getSubject()))
          return false;
      }

      String minP = minPrice.get();
      if (minP != null && !minP.isBlank())
      {
        try
        {
          if (session.getPrice() < Double.parseDouble(minP))
            return false;
        }
        catch (NumberFormatException ignored)
        {
        }
      }

      String maxP = maxPrice.get();
      if (maxP != null && !maxP.isBlank())
      {
        try
        {
          if (session.getPrice() > Double.parseDouble(maxP))
            return false;
        }
        catch (NumberFormatException ignored)
        {
        }
      }

      String from = fromDate.get();
      if (from != null && !from.isBlank())
      {
        try
        {
          LocalDate fd = LocalDate.parse(from, DATE_FMT);
          if (session.getStart_date().toLocalDate().isBefore(fd))
            return false;
        }
        catch (DateTimeParseException ignored)
        {
        }
      }

      String to = toDate.get();
      if (to != null && !to.isBlank())
      {
        try
        {
          LocalDate td = LocalDate.parse(to, DATE_FMT);
          if (session.getStart_date().toLocalDate().isAfter(td))
            return false;
        }
        catch (DateTimeParseException ignored)
        {
        }
      }

      return true;
    });
  }

  /**
   * Cancels the given session on behalf of the logged-in student. If the
   * cancellation succeeds, the available and booked session lists are refreshed
   * on the JavaFX application thread.
   *
   * @param session the session to cancel
   */
  public void cancelSession(Session session)
  {
    Student student = model.getLoggedInStudent();
    if(student != null && session != null) {
      boolean success = model.cancelSession(student.getId(), session.getId(), "student");
      if(success)
      {
        Platform.runLater(() -> {
          loadSessions();
          loadBookedSessions();
        });
      }
    }
  }

  /**
   * Returns the filtered view of the available-sessions list.
   *
   * @return the filtered sessions list
   */
  public FilteredList<Session> getFilteredSessions()
  {
    return filteredSessions;
  }

  /**
   * Returns the full (unfiltered) observable list of available sessions.
   *
   * @return the sessions list
   */
  public ObservableList<Session> getSessions()
  {
    return sessions;
  }

  /**
   * Returns the observable list of sessions the logged-in student has booked.
   *
   * @return the booked-sessions list
   */
  public ObservableList<Session> getBookedSessions()
  {
    return bookedSessions;
  }

  /**
   * Returns the observable list of subject names for the filter combo-box.
   *
   * @return the subject-options list
   */
  public ObservableList<String> getSubjectOptions()
  {
    return subjectOptions;
  }

  /** @return the first-name property */
  public StringProperty firstNameProperty() { return firstName; }
  /** @return the last-name property */
  public StringProperty lastNameProperty() { return lastName; }
  /** @return the date-of-birth property ( string) */
  public StringProperty dateOfBirthProperty() { return dateOfBirth; }
  /** @return the email property */
  public StringProperty emailProperty() { return email; }
  /** @return the password property */
  public StringProperty passwordProperty() { return password; }
  /** @return the phone-number property */
  public StringProperty phoneProperty() { return phone; }
  /** @return the gender property */
  public StringProperty genderProperty() { return gender; }
  /** @return the languages property (comma-separated string) */
  public StringProperty languagesProperty() { return languages; }
  /** @return the selected-subject filter property */
  public StringProperty subjectFilterProperty() { return subjectFilter; }
  /** @return the minimum-price filter property */
  public StringProperty minPriceProperty() { return minPrice; }
  /** @return the maximum-price filter property */
  public StringProperty maxPriceProperty() { return maxPrice; }
  /** @return the from-date filter property (format {@code dd/MM/yyyy}) */
  public StringProperty fromDateProperty() { return fromDate; }
  /** @return the to-date filter property (format {@code dd/MM/yyyy}) */
  public StringProperty toDateProperty() { return toDate; }

  /**
   * Handles model property-change events fired by the client-side
   * {@link client.model.ModelManager}.
   * 
   * On a {@code SessionBooked} event the newly booked session is removed from
   * the available list and added to the booked list on the JavaFX application
   * thread.
   * 
   *
   * @param evt the property-change event
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt)
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
}
