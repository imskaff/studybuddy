package server.mediator;

/**
 * Request sent from the client when a student books a tutoring session.
 *
 * @author
 */
public class BookSessionRequest {
    private String type = "BOOK_SESSION";
    private int studentId;
    private int sessionId;

    /**
     * Constructs a {@code BookSessionRequest} for the given student and session.
     *
     * @param studentId the ID of the student booking the session
     * @param sessionId the ID of the session to book
     */
    public BookSessionRequest(int studentId, int sessionId)
    {
        this.studentId = studentId;
        this.sessionId = sessionId;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "BOOK_SESSION"}
     */
    public String getType() { return type; }

    /**
     * Returns the ID of the student booking the session.
     *
     * @return the student ID
     */
    public int getStudentId() { return studentId; }

    /**
     * Returns the ID of the session to book.
     *
     * @return the session ID
     */
    public int getSessionId() { return sessionId; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with student and session IDs
     */
    @Override
    public String toString()
    {
        return "BOOK_SESSION{" +
            "studentId=" + studentId +
            ", sessionId=" + sessionId +
            '}';
    }
}
