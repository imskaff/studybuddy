package client.mediator;

import server.model.Student;
import server.model.Tutor;

public class LoginResult
{
  private final String userType;
  private final Student student;
  private final Tutor tutor;
  private final int adminId;

  private LoginResult(String userType, Student student, Tutor tutor, int adminId)
  {
    this.userType = userType;
    this.student = student;
    this.tutor = tutor;
    this.adminId = adminId;
  }

  public static LoginResult student(Student student)
  {
    return new LoginResult("student", student, null, -1);
  }

  public static LoginResult tutor(Tutor tutor)
  {
    return new LoginResult("tutor", null, tutor, -1);
  }

  public static LoginResult tutorPending(Tutor tutor)
  {
    return new LoginResult("tutor_pending", null, tutor, -1);
  }

  public static LoginResult admin(int adminId)
  {
    return new LoginResult("admin", null, null, adminId);
  }

  public static LoginResult error()
  {
    return new LoginResult(null, null, null, -1);
  }

  public String getUserType() { return userType; }
  public Student getStudent() { return student; }
  public Tutor getTutor() { return tutor; }
  public int getAdminId() { return adminId; }
}
