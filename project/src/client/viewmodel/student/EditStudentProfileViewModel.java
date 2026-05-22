package client.viewmodel.student;

import client.model.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.model.Student;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class EditStudentProfileViewModel
{
  private Model model;


  private final StringProperty firstName = new SimpleStringProperty("");
  private final StringProperty lastName = new SimpleStringProperty("");
  private final StringProperty email = new SimpleStringProperty("");
  private final StringProperty password = new SimpleStringProperty("");
  private final StringProperty phone = new SimpleStringProperty("");
  private final StringProperty gender = new SimpleStringProperty("");
  private final StringProperty dateOfBirth = new SimpleStringProperty("");
  private final StringProperty languages = new SimpleStringProperty("");

  public EditStudentProfileViewModel(Model model)
  {
    this.model = model;
  }
  public StringProperty firstNameProperty() { return firstName; }
  public StringProperty lastNameProperty() { return lastName; }
  public StringProperty emailProperty() { return email; }
  public StringProperty passwordProperty() {return password;}
  public StringProperty phoneProperty() { return phone; }
  public StringProperty genderProperty() { return gender; }
  public StringProperty dateOfBirthProperty() { return dateOfBirth; }
  public StringProperty languagesProperty() { return languages; }

  public void loadStudentData()
  {
    Student currentStudent = model.getLoggedInStudent();

    if(currentStudent != null)
    {
      firstName.set(currentStudent.getFirst_name());
      lastName.set(currentStudent.getLast_name());
      email.set(currentStudent.getEmail());
      password.set(currentStudent.getPassword());
      phone.set(currentStudent.getPhone());
      gender.set(currentStudent.getGender());
      languages.set(currentStudent.getLanguages().toString());

      if(currentStudent.getDate_of_birth() != null)
      {
        dateOfBirth.set(currentStudent.getDate_of_birth().toString());
      }
    }
  }

  public boolean saveChanges()
  {
    try{
      Student student = model.getLoggedInStudent();

      if(student == null)
      {
        return false;
      }

      student.setFirst_name(firstName.get());
      student.setLast_name(lastName.get());
      student.setEmail(email.get());
      student.setPassword(password.get());
      student.setPhone(phone.get());
      student.setGender(gender.get());

      String languagesText = languages.get();
      ArrayList<String> languagesList = new ArrayList<>(Arrays.asList(languagesText.split("\\s*,\\s*")));

      student.setLanguages(languagesList);

      try {
        student.setDateOfBirth(LocalDate.parse(dateOfBirth.get()));
      } catch (DateTimeParseException e) {
        System.out.println("Invalid date format. Expected YYYY-MM-DD.");
        return false;
      }

      return model.editStudentProfile(student);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return false;
  }
}
