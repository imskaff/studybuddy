package server.mediator;

import java.util.List;

public class SubjectsResponse {
    private String status;
    private List<String> subjects;

    public SubjectsResponse(String status, List<String> subjects) {
        this.status = status;
        this.subjects = subjects;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getSubjects() {
        return subjects;
    }
}
