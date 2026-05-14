package server.mediator;

// This request is sent by the client to retrieve
// all sessions booked by a specific student.
public class GetBookedSessionsRequest
{
  // Identifies the request type
  private String type = "GET_BOOKED_SESSIONS";

  // ID of the student whose booked sessions are being retrieved
  private int studentId;

  public GetBookedSessionsRequest(int studentId)
  {
    this.studentId = studentId;
  }

  public String getType() { return type; }
  public int getStudentId() { return studentId; }

  @Override
  public String toString()
  {
    return "GET_BOOKED_SESSIONS{studentId=" + studentId + "}";
  }
}
