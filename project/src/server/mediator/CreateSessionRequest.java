package server.mediator;

import server.model.Session;

/**
 * Request sent from the client when a tutor creates a new tutoring session.
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class CreateSessionRequest
{
    private String type = "CREATE_SESSION";
    private Session session;

    /**
     * Constructs a {@code CreateSessionRequest} wrapping the given session.
     *
     * @param session the session to create
     */
    public CreateSessionRequest(Session session)
    {
        this.session = session;
    }

    /**
     * Returns the session to be created.
     *
     * @return the session
     */
    public Session getSession() { return session; }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "CREATE_SESSION"}
     */
    public String getType() { return type; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with the session
     */
    @Override
    public String toString()
    {
        return "CREATE_SESSION{" +
            "session=" + session +
            '}';
    }
}
