package server.mediator;

import server.model.Session;

// CreateSessionRequest represents the data sent
// from the client to the server when a tutor
// creates a new tutoring session.
public class CreateSessionRequest
{
  // Identifies the type of request sent to the server
  private String type = "CREATE_SESSION";
  private Session session;

  // Constructor used to create the request
  public CreateSessionRequest(Session session)
  {
    this.session = session;
  }

  // Getter needed so the server can read
  // the session after converting from JSON
  public Session getSession()
  {
    return session;
  }

  public String getType()
  {
    return type;
  }

  @Override
  public String toString()
  {
    return "CREATE_SESSION{" +
        "session=" + session +
        '}';
  }
}
