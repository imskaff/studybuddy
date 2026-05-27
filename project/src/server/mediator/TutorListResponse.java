package server.mediator;

import server.model.Tutor;
import java.util.ArrayList;

/**
 * Server response that carries a list of {@link Tutor} objects back to the client.
 * Used by the {@code GET_PENDING_TUTORS} handler.
 *
 * @author
 */
public class TutorListResponse
{
    private String status;
    private String message;
    private ArrayList<Tutor> tutors;

    /**
     * Constructs a {@code TutorListResponse}.
     *
     * @param status  the response status (e.g. {@code "OK"})
     * @param message a human-readable description of the result
     * @param tutors  the list of tutors to return to the client
     */
    public TutorListResponse(String status, String message, ArrayList<Tutor> tutors)
    {
        this.status = status;
        this.message = message;
        this.tutors = tutors;
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
     * Returns the list of tutors included in this response.
     *
     * @return the tutor list
     */
    public ArrayList<Tutor> getTutors() { return tutors; }

    /**
     * Returns a string representation of this response for debugging purposes.
     *
     * @return a formatted string with status and tutor count
     */
    @Override
    public String toString()
    {
        return "TutorListResponse{status='" + status + "', tutors=" +
            (tutors != null ? tutors.size() : 0) + "}";
    }
}
