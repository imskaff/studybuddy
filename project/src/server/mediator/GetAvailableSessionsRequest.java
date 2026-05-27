package server.mediator;

/**
 * Request sent from the client to retrieve all available (unbooked, future) sessions.
 *
 * @author
 */
public class GetAvailableSessionsRequest
{
    private String type = "GET_AVAILABLE_SESSIONS";

    /**
     * Constructs a {@code GetAvailableSessionsRequest}.
     */
    public GetAvailableSessionsRequest() {}

    /**
     * Returns the request type identifier.
     *
     * @return {@code "GET_AVAILABLE_SESSIONS"}
     */
    public String getType() { return type; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a fixed string {@code "GET_AVAILABLE_SESSIONS{}"}
     */
    @Override
    public String toString()
    {
        return "GET_AVAILABLE_SESSIONS{}";
    }
}
