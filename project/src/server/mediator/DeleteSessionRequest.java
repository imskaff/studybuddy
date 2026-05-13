package server.mediator;
// DeleteSessionRequest represents the data
// sent from the client to the server
// when deleting a session.
public class DeleteSessionRequest
{
  // Identifies the type of request sent to the server
  private String type = "DELETE_SESSION";
  private int sessionId;
  private int tutorId;


  // Constructor used to create the request
  public DeleteSessionRequest(int sessionId, int tutorId)
  {
    this.sessionId = sessionId;
    this.tutorId = tutorId;
  }

  // Getters needed so the server can read
  // the values after converting from JSON
  public int getSessionId()
  {
    return sessionId;
  }

  public int getTutorId()
  {
    return tutorId;
  }

  public String getType()
  {
    return type;
  }

  @Override
  public String toString()
  {
    return "DELETE_SESSION{" +
        "sessionId=" + sessionId +
        ", tutorId=" + tutorId +
        '}';
  }
}
