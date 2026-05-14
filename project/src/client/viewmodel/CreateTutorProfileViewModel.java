package client.viewmodel;

import client.model.Model;
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
  public StringProperty educationLevelProperty() { return educationLevel; }
}
