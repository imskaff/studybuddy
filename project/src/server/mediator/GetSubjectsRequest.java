package server.mediator;

/**
 * Request sent from the client to retrieve all available subject names from the server.
 *
 * @author
 */
public class GetSubjectsRequest {
    private String type = "GET_SUBJECTS";

    /**
     * Returns the request type identifier.
     *
     * @return {@code "GET_SUBJECTS"}
     */
    public String getType() {
        return type;
    }
}
