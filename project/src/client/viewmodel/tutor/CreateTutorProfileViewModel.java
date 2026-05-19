package client.viewmodel.tutor;

import client.model.Model;
import client.viewmodel.CreateProfileViewModel;
import client.viewmodel.UserType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.model.Tutor;

import java.time.LocalDate;
import java.util.ArrayList;

public class CreateTutorProfileViewModel extends CreateProfileViewModel
{
  private final StringProperty educationLevel = new SimpleStringProperty("");

  public CreateTutorProfileViewModel(Model model)
  {
    super(model, UserType.TUTOR);
  }

  public void register(){
    Tutor tutor = new Tutor(
            firstNameProperty().get().trim(),
            lastNameProperty().get().trim(),
            LocalDate.parse(dateOfBirthProperty().get()),
            emailProperty().get().trim(),
            phoneNoProperty().get().trim(),
            passwordProperty().get(),
            genderProperty().get().trim(),
            new ArrayList<>(getLanguages()),
            educationLevelProperty().get().trim(),
            UserType.TUTOR.toString(),
            0
        );
        model.registerTutor(tutor);
  }

  @Override public StringProperty firstNameProperty()
  {
    return super.firstNameProperty();
  }

  @Override public StringProperty lastNameProperty()
  {
    return super.lastNameProperty();
  }

  @Override public StringProperty dateOfBirthProperty()
  {
    return super.dateOfBirthProperty();
  }

  @Override public StringProperty emailProperty()
  {
    return super.emailProperty();
  }

  @Override public StringProperty genderProperty()
  {
    return super.genderProperty();
  }

  @Override public StringProperty phoneNoProperty()
  {
    return super.phoneNoProperty();
  }


  @Override public StringProperty passwordProperty()
  {
    return super.passwordProperty();
  }

  @Override public StringProperty languageInputProperty()
  {
    return super.languageInputProperty();
  }

  public StringProperty educationLevelProperty() { return educationLevel; }
}
