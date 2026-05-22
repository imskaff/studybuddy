package client.viewmodel.tutor;

import client.model.TutorModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CreateNewSessionViewModel implements PropertyChangeListener
{
  private final TutorModel model;
  private Tutor currentTutor;

  private final ObservableList<String> subjectOptions;

  private final StringProperty subject;
  private final StringProperty price;
  private final StringProperty startDate;
  private final StringProperty endDate;
  private final StringProperty location;
  private final StringProperty description;
  private final StringProperty error;

  public CreateNewSessionViewModel(TutorModel model)
  {
    this.model = model;

    subjectOptions = FXCollections.observableArrayList();

    subject = new SimpleStringProperty("");
    price = new SimpleStringProperty("");
    startDate = new SimpleStringProperty("");
    endDate = new SimpleStringProperty("");
    location = new SimpleStringProperty("");
    description = new SimpleStringProperty("");
    error = new SimpleStringProperty("");

    model.addPropertyChangeListener(this);
  }

  public void loadSubjects()
  {
    subjectOptions.clear();
    subjectOptions.addAll(model.getSubjects());
  }

  public ObservableList<String> getSubjectOptions()
  {
    return subjectOptions;
  }


  public StringProperty subjectProperty()
  {
    return subject;
  }

  public StringProperty priceProperty()
  {
    return price;
  }

  public StringProperty startDateProperty()
  {
    return startDate;
  }

  public StringProperty endDateProperty()
  {
    return endDate;
  }

  public StringProperty locationProperty()
  {
    return location;
  }

  public StringProperty descriptionProperty()
  {
    return description;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {

  }

  public boolean createSession(Session session)
  {
    return model.createSession(session);
  }

  public StringProperty errorProperty()
  {
    return error;
  }

  public boolean createSessionFromInput()
  {
    try
    {
      String subjectValue = subject.get();
      String descriptionValue = description.get();
      String locationValue = location.get();
      String priceText = price.get();
      String startDateText = startDate.get();
      String endDateText = endDate.get();

      if (subjectValue == null || subjectValue.isEmpty()
          || descriptionValue == null || descriptionValue.isEmpty()
          || locationValue == null || locationValue.isEmpty()
          || priceText == null || priceText.isEmpty()
          || startDateText == null || startDateText.isEmpty()
          || endDateText == null || endDateText.isEmpty())
      {
        error.set("Please fill in all fields.");
        return false;
      }

      double parsedPrice = Double.parseDouble(priceText);

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      LocalDateTime parsedStartDate = LocalDateTime.parse(startDateText, formatter);
      LocalDateTime parsedEndDate = LocalDateTime.parse(endDateText, formatter);

      Tutor currentTutor = model.getLoggedInTutor();
      Session session = new Session(descriptionValue, parsedPrice, subjectValue,
          locationValue, parsedStartDate, parsedEndDate, currentTutor);

      boolean success = model.createSession(session);
      if (!success) {
        error.set("Database error: Could not create session.");
      }
      return success;
    }
    catch (NumberFormatException e)
    {
      error.set("Please enter a valid price.");
      return false;
    }
    catch (DateTimeParseException e)
    {
      error.set("Invalid date format! Please use: yyyy-MM-dd HH:mm (e.g., 2024-05-20 14:30)");
      return false;
    }
    catch (Exception e)
    {
      error.set("An unexpected error occurred.");
      e.printStackTrace();
      return false;
    }
  }

  public Tutor getCurrentTutor()
  {
    return model.getLoggedInTutor();
  }
}
