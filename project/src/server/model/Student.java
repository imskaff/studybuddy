package server.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Student extends User {

    public Student(String first_name, String last_name, LocalDate date_of_birth, String email, String phone,
                   String password, String gender, ArrayList<String> languages, int id, String type) {
        super(id, first_name, last_name, date_of_birth, email, phone, password, gender, languages, type);
    }


  // Simplified constructor used when only the student ID is needed
  public Student(int id)
  {
    super(id, "", "", null,
        "", "", "", "",
        new ArrayList<>(), "Student");
  }

}
