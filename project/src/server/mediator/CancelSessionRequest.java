package server.mediator;

public class CancelSessionRequest
{
  // Identifies the request type
  private String type = "CANCEL_SESSION";

  // ID of the user who wants to cancel the session
  private int userId;

  // ID of the selected session
  private int sessionId;

  // Type of user: "student" or "tutor"
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

  @Override
  public String toString()
  {
    return "CANCEL_SESSION{userId=" + userId +
        ", sessionId=" + sessionId +
        ", userType='" + userType + "'}";
  }
}
