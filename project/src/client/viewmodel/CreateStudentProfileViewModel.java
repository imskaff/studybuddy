package client.viewmodel;

import client.model.Model;
import server.model.Student;
import server.model.User;

import java.time.LocalDate;
import java.util.ArrayList;

public class CreateStudentProfileViewModel extends CreateProfileViewModel
{
  public CreateStudentProfileViewModel(Model model){
    super(model, UserType.STUDENT);
  }

  @Override
  public void register() {
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
}
