package server.mediator;

/**
 * A generic server response sent back to the client after processing a request.
 * 
 * Contains a status string (e.g. {@code "OK"}, {@code "ERROR"},
 * {@code "APPROVED"}, {@code "DECLINED"}) and a human-readable message.
 * 
 * @author Adam Terelak
 */
public class Response {
    private String status;
    private String message;

    /**
     * Constructs a {@code Response} with the given status and message.
     *
     * @param status  the outcome status (e.g. {@code "OK"} or {@code "ERROR"})
     * @param message a human-readable description of the outcome
     */
    public Response(String status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Returns the status of this response.
     *
     * @return the status string
     */
    public String getStatus()  { return status; }

    /**
     * Returns the message of this response.
     *
     * @return the message string
     */
    public String getMessage() { return message; }

    /**
     * Returns a string representation of this response for debugging purposes.
     *
     * @return a formatted string with status and message
     */
    @Override
    public String toString() {
        return "Response{status='" + status + "', message='" + message + "'}";
    }
}
