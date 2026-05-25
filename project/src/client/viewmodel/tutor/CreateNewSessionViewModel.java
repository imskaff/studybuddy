package client.viewmodel.tutor;

import client.model.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

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

  public CreateNewSessionViewModel(Model model)
  {
    this.model = model;

    this.subjectOptions = FXCollections.observableArrayList();

    subject = new SimpleStringProperty("");
    price = new SimpleStringProperty("");
    startDate = new SimpleStringProperty("");
    endDate = new SimpleStringProperty("");
    location = new SimpleStringProperty("");
    description = new SimpleStringProperty("");

    model.addPropertyChangeListener(this);
  }

  public void loadSubjects()
  {
    List<String> subjectsFromDatabase = model.getSubjects();

    if (subjectsFromDatabase != null)
    {
      subjectOptions.clear();
      subjectOptions.addAll(subjectsFromDatabase);
    }
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

  public Tutor getCurrentTutor()
  {
    return model.getLoggedInTutor();
  }
}
