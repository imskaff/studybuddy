package server.mediator;

public class CancelSessionRequest
{
  private String type = "CANCEL_SESSION";
  private int userId;
  private int sessionId;
  private String userType;

  public CancelSessionRequest(int userId, int sessionId, String userType)
  {
    this.userId = userId;
    this.sessionId = sessionId;
    this.userType = userType;
  }

  public String getType() { return type; }
  public int getUserId() { return userId; }
  public int getSessionId() { return sessionId; }
  public String getUserType() { return userType; }
}
