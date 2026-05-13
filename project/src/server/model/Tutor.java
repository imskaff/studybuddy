package server.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Tutor extends User {
    private String education_level;
    private String status;

    public Tutor(String first_name, String last_name, LocalDate date_of_birth, String email, String phone,
                 String password, String gender, ArrayList<String> languages, String education_level, String type, int id) {
        super(id, first_name, last_name, date_of_birth, email, phone, password, gender, languages, type);
        this.education_level = education_level;
        this.status = status;
    }

    public String getEducation_level() {
        return education_level;
    }

    public void setEducation_level(String education_level) {
      this.education_level = education_level;
    }

  public void setStatus(String status)
  {
    this.status = status;
  }

  @Override public String toString()
  {
    return "Tutor{" + "education_level='" + education_level + '\''
        + ", status='" + status + '\'' + '}' + super.toString();
  }
}