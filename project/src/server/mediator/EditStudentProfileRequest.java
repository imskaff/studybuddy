package server.mediator;

import server.model.Student;

public class EditStudentProfileRequest
{
  private String type = "EDIT_STUDENT_PROFILE";
  private Student student;

  public EditStudentProfileRequest(Student student){
    this.student = student;
  }

  public String getType(){
    return type;
  }

  public Student getStudent(){
    return student;
  }

  @Override
  public String toString()
  {
    return "EDIT_STUDENT_PROFILE{" +
        "student=" + student + '}';
  }
}
