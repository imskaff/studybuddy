package server.mediator;

/**
 * Request sent from the client to retrieve all sessions booked by a specific student.
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class GetBookedSessionsRequest
{
    private String type = "GET_BOOKED_SESSIONS";
    private int studentId;

    /**
     * Constructs a {@code GetBookedSessionsRequest} for the given student.
     *
     * @param studentId the ID of the student whose booked sessions to retrieve
     */
    public GetBookedSessionsRequest(int studentId)
    {
        this.studentId = studentId;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "GET_BOOKED_SESSIONS"}
     */
    public String getType() { return type; }

    /**
     * Returns the ID of the student whose booked sessions are being retrieved.
     *
     * @return the student ID
     */
    public int getStudentId() { return studentId; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with the student ID
     */
    @Override
    public String toString()
    {
        return "GET_BOOKED_SESSIONS{studentId=" + studentId + "}";
    }
}
