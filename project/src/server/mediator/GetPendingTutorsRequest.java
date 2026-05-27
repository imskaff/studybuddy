package server.mediator;

/**
 * Request sent from the client to retrieve all tutor registrations
 * awaiting administrator approval.
 *
 * @author
 */
public class GetPendingTutorsRequest
{
    private String type = "GET_PENDING_TUTORS";

    /**
     * Constructs a {@code GetPendingTutorsRequest}.
     */
    public GetPendingTutorsRequest() {}

    /**
     * Returns the request type identifier.
     *
     * @return {@code "GET_PENDING_TUTORS"}
     */
    public String getType() { return type; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a fixed string {@code "GET_PENDING_TUTORS{}"}
     */
    @Override
    public String toString()
    {
        return "GET_PENDING_TUTORS{}";
    }
}
