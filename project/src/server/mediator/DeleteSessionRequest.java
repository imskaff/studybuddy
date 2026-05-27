package server.mediator;

/**
 * Request sent from the client when a tutor permanently deletes a session.
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class DeleteSessionRequest
{
    private String type = "DELETE_SESSION";
    private int sessionId;
    private int tutorId;

    /**
     * Constructs a {@code DeleteSessionRequest} for the given session and tutor.
     *
     * @param sessionId the ID of the session to delete
     * @param tutorId   the ID of the tutor requesting the deletion
     */
    public DeleteSessionRequest(int sessionId, int tutorId)
    {
        this.sessionId = sessionId;
        this.tutorId = tutorId;
    }

    /**
     * Returns the ID of the session to delete.
     *
     * @return the session ID
     */
    public int getSessionId() { return sessionId; }

    /**
     * Returns the ID of the tutor requesting the deletion.
     *
     * @return the tutor ID
     */
    public int getTutorId() { return tutorId; }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "DELETE_SESSION"}
     */
    public String getType() { return type; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with session and tutor IDs
     */
    @Override
    public String toString()
    {
        return "DELETE_SESSION{" +
            "sessionId=" + sessionId +
            ", tutorId=" + tutorId +
            '}';
    }
}
