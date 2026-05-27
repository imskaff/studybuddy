package server.mediator;

/**
 * Request sent from the client to mark a booked session as complete.
 * 
 * The server will only allow the transition if the session is in the
 * Booked state and its end date has already passed.
 * 
 *
 * @author
 */
public class CompleteSessionRequest
{
    private String type = "COMPLETE_SESSION";
    private int sessionId;

    /**
     * Constructs a {@code CompleteSessionRequest} for the given session.
     *
     * @param sessionId the ID of the session to complete
     */
    public CompleteSessionRequest(int sessionId)
    {
        this.sessionId = sessionId;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "COMPLETE_SESSION"}
     */
    public String getType() { return type; }

    /**
     * Returns the ID of the session to complete.
     *
     * @return the session ID
     */
    public int getSessionId() { return sessionId; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with the session ID
     */
    @Override
    public String toString()
    {
        return "COMPLETE_SESSION{sessionId=" + sessionId + "}";
    }
}
