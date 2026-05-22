package client.viewmodel.student;

import client.model.Model;
import client.viewmodel.CreateProfileViewModel;
import client.viewmodel.UserType;
import server.model.Student;

import java.time.LocalDate;
import java.util.ArrayList;

public class CreateStudentProfileViewModel extends CreateProfileViewModel
{
  public CreateStudentProfileViewModel(Model model)
  {
    super(model, UserType.STUDENT);
  }

  @Override
  public boolean register()
  {
    String dob = dateOfBirthProperty().get();
    if (firstNameProperty().get().isBlank() || lastNameProperty().get().isBlank()
        || dob.isBlank() || emailProperty().get().isBlank()
        || phoneNoProperty().get().isBlank() || passwordProperty().get().isBlank()
        || genderProperty().get().isBlank() || languageInputProperty().get().isBlank()) {
      return false;
    }
    try {
      ArrayList<String> langs = new ArrayList<>();
      for (String l : languageInputProperty().get().split(",")) {
        if (!l.isBlank()) langs.add(l.trim());
      }
      Student student = new Student(
          firstNameProperty().get().trim(),
          lastNameProperty().get().trim(),
          LocalDate.parse(dob.trim()),
          emailProperty().get().trim(),
          phoneNoProperty().get().trim(),
          passwordProperty().get(),
          genderProperty().get().trim(),
          langs,
          0,
          UserType.STUDENT.toString()
      );
      return model.registerStudent(student);
    } catch (Exception e) {
      return false;
    }
  }
}
