package server.mediator;

public class CompleteSessionRequest
{
  // Identifies the request type
  private String type = "COMPLETE_SESSION";

  // ID of the session to complete
  private int sessionId;

  public CompleteSessionRequest(int sessionId)
  {
    this.sessionId = sessionId;
  }

  public String getType() { return type; }
  public int getSessionId() { return sessionId; }

  @Override
  public String toString()
  {
    return "COMPLETE_SESSION{sessionId=" + sessionId + "}";
  }
}
