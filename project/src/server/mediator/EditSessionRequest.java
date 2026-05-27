package server.mediator;

import server.model.Session;

/**
 * Request sent from the client when a tutor edits an existing tutoring session.
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class EditSessionRequest
{
    private String type = "EDIT_SESSION";
    private int tutorId;
    private Session session;

    /**
     * Constructs an {@code EditSessionRequest} with the given tutor ID and updated session.
     *
     * @param tutorId the ID of the tutor making the edit
     * @param session the session containing the updated field values
     */
    public EditSessionRequest(int tutorId, Session session)
    {
        this.tutorId = tutorId;
        this.session = session;
    }

    /**
     * Returns the ID of the tutor making the edit.
     *
     * @return the tutor ID
     */
    public int getTutorId() { return tutorId; }

    /**
     * Returns the session with the updated field values.
     *
     * @return the updated session
     */
    public Session getSession() { return session; }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "EDIT_SESSION"}
     */
    public String getType() { return type; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with the tutor ID and session
     */
    @Override
    public String toString()
    {
        return "EDIT_SESSION{" +
            "tutorId=" + tutorId +
            ", session=" + session +
            '}';
    }
}
