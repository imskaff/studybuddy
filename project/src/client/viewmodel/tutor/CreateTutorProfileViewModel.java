package client.viewmodel.tutor;

import client.model.Model;
import client.viewmodel.CreateProfileViewModel;
import client.viewmodel.UserType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.model.Tutor;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * ViewModel for the tutor registration form.
 * Extends {@link CreateProfileViewModel} and adds an education-level field.
 *
 * @author
 */
public class CreateTutorProfileViewModel extends CreateProfileViewModel
{
  private final StringProperty educationLevel = new SimpleStringProperty("");

  /**
   * Constructs the ViewModel for the given model.
   *
   * @param model the client-side model
   */
  public CreateTutorProfileViewModel(Model model)
  {
    super(model, UserType.TUTOR);
  }

  /**
   * Validates all required fields including education level, then registers
   * the tutor via the model.
   *
   * @return {@code true} if registration succeeded, {@code false} if any field
   *         is blank or the server rejected the request
   */
  @Override
  public boolean register() {
    String dob = dateOfBirthProperty().get();
    if (firstNameProperty().get().isBlank() || lastNameProperty().get().isBlank()
        || dob.isBlank() || emailProperty().get().isBlank()
        || phoneNoProperty().get().isBlank() || passwordProperty().get().isBlank()
        || genderProperty().get().isBlank() || educationLevelProperty().get().isBlank()) {
      return false;
    }
    try {
      ArrayList<String> langs = new ArrayList<>();
      for (String l : languageInputProperty().get().split(",")) {
        if (!l.isBlank()) langs.add(l.trim());
      }
      Tutor tutor = new Tutor(
          firstNameProperty().get().trim(),
          lastNameProperty().get().trim(),
          LocalDate.parse(dob.trim()),
          emailProperty().get().trim(),
          phoneNoProperty().get().trim(),
          passwordProperty().get(),
          genderProperty().get().trim(),
          langs,
          educationLevelProperty().get().trim(),
          UserType.TUTOR.toString(),
          0
      );
      return model.registerTutor(tutor);
    } catch (Exception e) {
      return false;
    }
  }

  /** Returns the first-name property. */
  @Override public StringProperty firstNameProperty()
  {
    return super.firstNameProperty();
  }

  /** Returns the last-name property. */
  @Override public StringProperty lastNameProperty()
  {
    return super.lastNameProperty();
  }

  /** Returns the date-of-birth property. */
  @Override public StringProperty dateOfBirthProperty()
  {
    return super.dateOfBirthProperty();
  }

  /** Returns the email property. */
  @Override public StringProperty emailProperty()
  {
    return super.emailProperty();
  }

  /** Returns the gender property. */
  @Override public StringProperty genderProperty()
  {
    return super.genderProperty();
  }

  /** Returns the phone-number property. */
  @Override public StringProperty phoneNoProperty()
  {
    return super.phoneNoProperty();
  }

  /** Returns the password property. */
  @Override public StringProperty passwordProperty()
  {
    return super.passwordProperty();
  }

  /** Returns the language-input property. */
  @Override public StringProperty languageInputProperty()
  {
    return super.languageInputProperty();
  }

  /**
   * Returns the education-level property.
   *
   * @return the education-level property
   */
  public StringProperty educationLevelProperty() { return educationLevel; }
}
