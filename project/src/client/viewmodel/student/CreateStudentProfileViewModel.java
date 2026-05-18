package client.viewmodel.student;

import client.model.Model;
import client.viewmodel.CreateProfileViewModel;
import client.viewmodel.UserType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.model.Student;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CreateStudentProfileViewModel extends CreateProfileViewModel
{
  private final StringProperty firstName;
  private final StringProperty lastName;
  private final StringProperty dateOfBirth;
  private final StringProperty email;
  private final StringProperty phone;
  private final StringProperty gender;
  private final StringProperty languages;


  public CreateStudentProfileViewModel(Model model)
  {
    super(model, UserType.STUDENT);

    firstName = new SimpleStringProperty("");
    lastName = new SimpleStringProperty("");
    dateOfBirth = new SimpleStringProperty("");
    email = new SimpleStringProperty("");
    phone = new SimpleStringProperty("");
    gender = new SimpleStringProperty("");
    languages = new SimpleStringProperty("");
  }

  @Override
  public void register() throws SQLException
  {

    Student student = new Student(
        firstNameProperty().get().trim(),
        lastNameProperty().get().trim(),
        LocalDate.parse(dateOfBirthProperty().get()),
        emailProperty().get().trim(),
        phoneNoProperty().get().trim(),
        passwordProperty().get(),
        genderProperty().get().trim(),
        new ArrayList<>(getLanguages()),
        0,
        UserType.STUDENT.toString()
   );

    model.registerStudent(student);
  }

  public StringProperty firstNameProperty() { return firstName; }
  public StringProperty lastNameProperty() { return lastName; }
  public StringProperty dateOfBirthProperty() { return dateOfBirth; }
  public StringProperty emailProperty() { return email; }
  public StringProperty phoneProperty() { return phone; }
  public StringProperty genderProperty() { return gender; }
  public StringProperty languagesProperty() { return languages; }


}
