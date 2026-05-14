package server.mediator;

// This request is sent by the client to retrieve
// all available (unbooked) sessions from the server.
public class GetAvailableSessionsRequest
{
  // Identifies the request type
  private String type = "GET_AVAILABLE_SESSIONS";

  public GetAvailableSessionsRequest() {}

  public String getType() { return type; }

  @Override
  public String toString()
  {
    return "GET_AVAILABLE_SESSIONS{}";
  }
}
