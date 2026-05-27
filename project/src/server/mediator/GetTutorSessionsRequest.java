package server.mediator;

/**
 * Request sent from the client to retrieve all sessions belonging to a specific tutor.
 *
 * @author
 */
public class GetTutorSessionsRequest
{
    private String type = "GET_TUTOR_SESSIONS";
    private int tutorId;

    /**
     * Constructs a {@code GetTutorSessionsRequest} for the given tutor.
     *
     * @param tutorId the ID of the tutor whose sessions to retrieve
     */
    public GetTutorSessionsRequest(int tutorId)
    {
        this.tutorId = tutorId;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "GET_TUTOR_SESSIONS"}
     */
    public String getType() { return type; }

    /**
     * Returns the ID of the tutor whose sessions are being retrieved.
     *
     * @return the tutor ID
     */
    public int getTutorId() { return tutorId; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with the tutor ID
     */
    @Override
    public String toString()
    {
        return "GET_TUTOR_SESSIONS{tutorId=" + tutorId + "}";
    }
}
