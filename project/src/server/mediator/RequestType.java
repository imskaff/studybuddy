package server.mediator;

// RequestType is a small helper class used by the server
// to identify which type of request was received.
// The server first converts the incoming JSON into this class
// in order to read the "type" field before converting
// the JSON into the correct request class
// (e.g., BookSessionRequest, CreateSessionRequest, etc.).
public class RequestType
{
  private String type;

  // Getter used by the server to read the request type
  public String getType()
  {
    return type;
  }
}
