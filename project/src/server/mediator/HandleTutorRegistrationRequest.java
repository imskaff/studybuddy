package server.mediator;

// This request is sent when the administrator
// approves or declines a pending tutor registration.
public class HandleTutorRegistrationRequest
{
  // Identifies the request type
  private String type = "HANDLE_TUTOR_REGISTRATION";

  // ID of the tutor registration selected by the administrator
  private int tutorId;

  // Action selected by the administrator: APPROVE or DECLINE
  private String action;

  public HandleTutorRegistrationRequest(int tutorId, String action)
  {
    this.tutorId = tutorId;
    this.action = action;
  }

  public String getType()
  {
    return type;
  }

  public int getTutorId()
  {
    return tutorId;
  }

  public String getAction()
  {
    return action;
  }

  @Override
  public String toString()
  {
    return "HANDLE_TUTOR_REGISTRATION{" +
        "tutorId=" + tutorId +
        ", action='" + action + '\'' +
        '}';
  }
}
