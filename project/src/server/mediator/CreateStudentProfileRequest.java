package server.mediator;

import server.model.Student;

// This request is sent when a user creates a new student profile.
public class CreateStudentProfileRequest
{
  // Identifies the request type
  private String type = "CREATE_STUDENT_PROFILE";

  // Student object containing the registration information
  private Student student;

  // Indicates whether the user confirmed the rules
  private boolean rulesConfirmed;

  public CreateStudentProfileRequest(Student student,
      boolean rulesConfirmed)
  {
    this.student = student;
    this.rulesConfirmed = rulesConfirmed;
  }

  public String getType()
  {
    return type;
  }

  public Student getStudent()
  {
    return student;
  }

  public boolean isRulesConfirmed()
  {
    return rulesConfirmed;
  }

  @Override
  public String toString()
  {
    return "CREATE_STUDENT_PROFILE{" +
        "student=" + student +
        ", rulesConfirmed=" + rulesConfirmed +
        '}';
  }
}
