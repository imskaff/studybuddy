package server.mediator;

import server.model.Session;
import java.util.ArrayList;

/**
 * Server response that carries a list of {@link Session} objects back to the client.
 * Used by the {@code GET_AVAILABLE_SESSIONS}, {@code GET_BOOKED_SESSIONS},
 * and {@code GET_TUTOR_SESSIONS} handlers.
 *
 * @author
 */
public class SessionListResponse
{
    private String status;
    private String message;
    private ArrayList<Session> sessions;

    /**
     * Constructs a {@code SessionListResponse}.
     *
     * @param status   the response status (e.g. {@code "OK"})
     * @param message  a human-readable description of the result
     * @param sessions the list of sessions to return to the client
     */
    public SessionListResponse(String status, String message, ArrayList<Session> sessions)
    {
        this.status = status;
        this.message = message;
        this.sessions = sessions;
    }

    /**
     * Returns the response status.
     *
     * @return the status string
     */
    public String getStatus() { return status; }

    /**
     * Returns the response message.
     *
     * @return the message string
     */
    public String getMessage() { return message; }

    /**
     * Returns the list of sessions included in this response.
     *
     * @return the session list
     */
    public ArrayList<Session> getSessions() { return sessions; }

    /**
     * Returns a string representation of this response for debugging purposes.
     *
     * @return a formatted string with status and session count
     */
    @Override
    public String toString()
    {
        return "SessionListResponse{status='" + status + "', sessions=" +
            (sessions != null ? sessions.size() : 0) + "}";
    }
}
