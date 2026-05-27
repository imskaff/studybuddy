package server.mediator;

/**
 * Request sent from the client when a student or tutor cancels a session.
 * 
 * A student cancellation reverts the session to Available; a tutor cancellation
 * moves the session to the Cancelled state.
 * 
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 * @author Volodymyr Neka
 */
public class CancelSessionRequest
{
    private String type = "CANCEL_SESSION";
    private int userId;
    private int sessionId;
    private String userType;

    /**
     * Constructs a {@code CancelSessionRequest}.
     *
     * @param userId    the ID of the user requesting the cancellation
     * @param sessionId the ID of the session to cancel
     * @param userType  the type of the cancelling user ({@code "student"} or {@code "tutor"})
     */
    public CancelSessionRequest(int userId, int sessionId, String userType)
    {
        this.userId = userId;
        this.sessionId = sessionId;
        this.userType = userType;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "CANCEL_SESSION"}
     */
    public String getType() { return type; }

    /**
     * Returns the ID of the user requesting the cancellation.
     *
     * @return the user ID
     */
    public int getUserId() { return userId; }

    /**
     * Returns the ID of the session to cancel.
     *
     * @return the session ID
     */
    public int getSessionId() { return sessionId; }

    /**
     * Returns the type of the cancelling user.
     *
     * @return {@code "student"} or {@code "tutor"}
     */
    public String getUserType() { return userType; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with user ID, session ID and user type
     */
    @Override
    public String toString()
    {
        return "CANCEL_SESSION{userId=" + userId +
            ", sessionId=" + sessionId +
            ", userType='" + userType + "'}";
    }
}
