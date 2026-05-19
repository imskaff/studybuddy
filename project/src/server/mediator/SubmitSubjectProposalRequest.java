package server.mediator;

public class SubmitSubjectProposalRequest {
    private final String type = "SUBMIT_SUBJECT_PROPOSAL";
    private String tutorEmail;
    private String subjectName;
    private String reason;

    public SubmitSubjectProposalRequest(String tutorEmail, String subjectName, String reason) {
        this.tutorEmail = tutorEmail;
        this.subjectName = subjectName;
        this.reason = reason;
    }

    public String getTutorEmail() { return tutorEmail; }
    public String getSubjectName() { return subjectName; }
    public String getReason() { return reason; }
}
