package client.viewmodel.student;

import client.model.Model;
import client.viewmodel.CreateProfileViewModel;
import client.viewmodel.UserType;
import javafx.beans.property.StringProperty;
import server.model.Student;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CreateStudentProfileViewModel extends CreateProfileViewModel
{
  public CreateStudentProfileViewModel(Model model)
  {
    super(model, UserType.STUDENT);
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

  @Override public StringProperty passwordProperty()
  {
    return super.passwordProperty();
  }

  @Override public StringProperty genderProperty()
  {
    return super.genderProperty();
  }

  @Override public StringProperty phoneNoProperty()
  {
    return super.phoneNoProperty();
  }

  @Override public StringProperty languageInputProperty()
  {
    return super.languageInputProperty();
  }
}
