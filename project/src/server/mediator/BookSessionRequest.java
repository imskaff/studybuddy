package server.mediator;

public class BookSessionRequest {
  // Identifies the type of request sent to the server
  private String type = "BOOK_SESSION";

  private int studentId;
  // ID of the student who is booking the session

  private int sessionId;
  // ID of the session the student wants to book



  public BookSessionRequest(int studentId, int sessionId)
  {
    this.studentId = studentId;
    this.sessionId = sessionId;
  }

  // Getter used by the server to identify the request type
  public String getType()
  {
    return type;
  }

  // Getters are needed so the server can read the values after converting from JSON
  public int getStudentId()
  {
    return studentId;
  }

  public int getSessionId()
  {
    return sessionId;
  }

  @Override
  public String toString()
  {
    return "BOOK_SESSION{" +
        "studentId=" + studentId +
        ", sessionId=" + sessionId +
        '}';
  }
}
