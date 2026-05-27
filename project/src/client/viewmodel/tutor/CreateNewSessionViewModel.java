package client.viewmodel.tutor;

import client.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Tutor;

/**
 * ViewModel for the create-new-session form.
 * 
 * Holds the editable properties for a new tutoring session, exposes a
 * subject-options list populated from the server, and delegates session
 * creation to the model. Registers as a model listener for future reactive
 * updates.
 * 
 *
 * @author
 */
public class CreateNewSessionViewModel implements PropertyChangeListener
{
  private final Model model;
  private Tutor currentTutor;

  private final ObservableList<String> subjectOptions;

  private final StringProperty subject;
  private final StringProperty price;
  private final StringProperty startDate;
  private final StringProperty endDate;
  private final StringProperty location;
  private final StringProperty description;

  /**
   * Constructs a {@code CreateNewSessionViewModel} and registers it as a
   * model property-change listener.
   *
   * @param model the client-side model
   */
  public CreateNewSessionViewModel(Model model)
  {
    this.model = model;

    this.subjectOptions = FXCollections.observableArrayList();

    subject     = new SimpleStringProperty("");
    price       = new SimpleStringProperty("");
    startDate   = new SimpleStringProperty("");
    endDate     = new SimpleStringProperty("");
    location    = new SimpleStringProperty("");
    description = new SimpleStringProperty("");

    model.addPropertyChangeListener(this);
  }

  /**
   * Fetches the list of available subjects from the server and replaces the
   * current subject-options list.
   */
  public void loadSubjects()
  {
    List<String> subjectsFromDatabase = model.getSubjects();

    if (subjectsFromDatabase != null)
    {
      subjectOptions.clear();
      subjectOptions.addAll(subjectsFromDatabase);
    }
  }

  /**
   * Returns the observable list of subject names for the subject combo-box.
   *
   * @return the subject-options list
   */
  public ObservableList<String> getSubjectOptions()
  {
    return subjectOptions;
  }

  /** @return the subject property */
  public StringProperty subjectProperty()
  {
    return subject;
  }

  /** @return the price property */
  public StringProperty priceProperty()
  {
    return price;
  }

  /** @return the start-date property */
  public StringProperty startDateProperty()
  {
    return startDate;
  }

  /** @return the end-date property */
  public StringProperty endDateProperty()
  {
    return endDate;
  }

  /** @return the location property */
  public StringProperty locationProperty()
  {
    return location;
  }

  /** @return the session description property */
  public StringProperty descriptionProperty()
  {
    return description;
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

  /**
   * Delegates session creation to the model.
   *
   * @param session the fully constructed {@link Session} to create
   * @return {@code true} if the server accepted the new session
   */
  public boolean createSession(Session session)
  {
    return model.createSession(session);
  }

  /**
   * Returns the currently logged-in tutor from the model.
   *
   * @return the logged-in {@link Tutor}, or {@code null} if none is logged in
   */
  public Tutor getCurrentTutor()
  {
    return model.getLoggedInTutor();
  }
}
