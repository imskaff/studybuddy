package server.mediator;

// This request is sent by the client to retrieve
// all pending tutor registrations from the server.
public class GetPendingTutorsRequest
{
  // Identifies the request type
  private String type = "GET_PENDING_TUTORS";

  public GetPendingTutorsRequest() {}

  public String getType() { return type; }

  @Override
  public String toString()
  {
    return "GET_PENDING_TUTORS{}";
  }
}
