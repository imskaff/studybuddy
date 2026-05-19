package server.model;

import java.time.LocalDateTime;

public class SubjectProposal {
    private int id;
    private String tutorEmail;
    private String subjectName;
    private String reason;
    private String status;
    private LocalDateTime requestedAt;

    public SubjectProposal(int id, String tutorEmail, String subjectName, String reason, String status, LocalDateTime requestedAt) {
        this.id = id;
        this.tutorEmail = tutorEmail;
        this.subjectName = subjectName;
        this.reason = reason;
        this.status = status;
        this.requestedAt = requestedAt;
    }

    public int getId() { return id; }
    public String getTutorEmail() { return tutorEmail; }
    public String getSubjectName() { return subjectName; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
}
