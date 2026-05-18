package server.mediator;

import server.model.Session;

public class EditSessionRequest
{
  // Identifies the type of request sent to the server
  private String type = "EDIT_SESSION";
  private int tutorId;
  private Session session;

  // Constructor used to create the request
  public EditSessionRequest(int tutorId,
      Session session)
  {
    this.tutorId = tutorId;
    this.session = session;
  }

  // Getter used by the server
  // to read the tutor ID
  public int getTutorId()
  {
    return tutorId;
  }

  // Getter used by the server
  // to read the updated session
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
    return "EDIT_SESSION{" +
        "tutorId=" + tutorId +
        ", session=" + session +
        '}';
  }
}
