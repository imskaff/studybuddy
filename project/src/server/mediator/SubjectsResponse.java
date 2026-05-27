package server.mediator;

import java.util.List;

/**
 * Server response that carries the list of available subject names back to the client.
 * Used by the {@code GET_SUBJECTS} handler.
 *
 * @author
 */
public class SubjectsResponse {
    private String status;
    private List<String> subjects;

    /**
     * Constructs a {@code SubjectsResponse}.
     *
     * @param status   the response status (e.g. {@code "OK"})
     * @param subjects the list of subject names to return to the client
     */
    public SubjectsResponse(String status, List<String> subjects) {
        this.status = status;
        this.subjects = subjects;
    }

    /**
     * Returns the response status.
     *
     * @return the status string
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the list of subject names included in this response.
     *
     * @return the subject list
     */
    public List<String> getSubjects() {
        return subjects;
    }
}
