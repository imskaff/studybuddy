package server.mediator;

public class HandleSubjectProposalRequest {
    private final String type = "HANDLE_SUBJECT_PROPOSAL";
    private int proposalId;
    private String action;
    private int adminId;

    public HandleSubjectProposalRequest(int proposalId, String action, int adminId) {
        this.proposalId = proposalId;
        this.action = action;
        this.adminId = adminId;
    }

    public int getProposalId() { return proposalId; }
    public String getAction() { return action; }
    public int getAdminId() { return adminId; }
}