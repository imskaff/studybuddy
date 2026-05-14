package server.mediator;

import server.model.Tutor;
import java.util.ArrayList;

// Response that carries a list of tutors back to the client.
// Used by the GET_PENDING_TUTORS handler.
public class TutorListResponse
{
  private String status;
  private String message;
  private ArrayList<Tutor> tutors;

  public TutorListResponse(String status, String message, ArrayList<Tutor> tutors)
  {
    this.status = status;
    this.message = message;
    this.tutors = tutors;
  }

  public String getStatus() { return status; }
  public String getMessage() { return message; }
  public ArrayList<Tutor> getTutors() { return tutors; }

  @Override
  public String toString()
  {
    return "TutorListResponse{status='" + status + "', tutors=" +
        (tutors != null ? tutors.size() : 0) + "}";
  }
}
