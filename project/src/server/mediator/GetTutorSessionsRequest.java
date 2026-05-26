package server.mediator;

public class GetTutorSessionsRequest
{
  private String type = "GET_TUTOR_SESSIONS";
  private int tutorId;

  public GetTutorSessionsRequest(int tutorId)
  {
    this.tutorId = tutorId;
  }

  public String getType() { return type; }
  public int getTutorId() { return tutorId; }
}
