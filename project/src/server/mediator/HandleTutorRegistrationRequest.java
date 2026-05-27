package server.mediator;

/**
 * Request sent from the client when an administrator approves or declines
 * a pending tutor registration.
 *
 * @author
 */
public class HandleTutorRegistrationRequest
{
    private String type = "HANDLE_TUTOR_REGISTRATION";
    private int tutorId;
    private String action;
    private int adminId;

    /**
     * Constructs a {@code HandleTutorRegistrationRequest}.
     *
     * @param tutorId the ID of the tutor whose registration is being processed
     * @param action  the action to perform: {@code "APPROVE"} or {@code "DECLINE"}
     * @param adminId the ID of the administrator performing the action
     */
    public HandleTutorRegistrationRequest(int tutorId, String action, int adminId)
    {
        this.tutorId = tutorId;
        this.action = action;
        this.adminId = adminId;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "HANDLE_TUTOR_REGISTRATION"}
     */
    public String getType() { return type; }

    /**
     * Returns the ID of the tutor being processed.
     *
     * @return the tutor ID
     */
    public int getTutorId() { return tutorId; }

    /**
     * Returns the action to perform on the registration.
     *
     * @return {@code "APPROVE"} or {@code "DECLINE"}
     */
    public String getAction() { return action; }

    /**
     * Returns the ID of the administrator performing the action.
     *
     * @return the admin ID
     */
    public int getAdminId() { return adminId; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with tutor ID, action and admin ID
     */
    @Override
    public String toString()
    {
        return "HANDLE_TUTOR_REGISTRATION{" +
            "tutorId=" + tutorId +
            ", action='" + action + '\'' +
            ", adminId=" + adminId +
            '}';
    }
}
