package server.mediator;

import server.model.Session;
import java.util.ArrayList;

// Response that carries a list of sessions back to the client.
// Used by GET_AVAILABLE_SESSIONS and GET_BOOKED_SESSIONS handlers.
public class SessionListResponse
{
  private String status;
  private String message;
  private ArrayList<Session> sessions;

  public SessionListResponse(String status, String message, ArrayList<Session> sessions)
  {
    this.status = status;
    this.message = message;
    this.sessions = sessions;
  }

  public String getStatus() { return status; }
  public String getMessage() { return message; }
  public ArrayList<Session> getSessions() { return sessions; }

  @Override
  public String toString()
  {
    return "SessionListResponse{status='" + status + "', sessions=" +
        (sessions != null ? sessions.size() : 0) + "}";
  }
}
