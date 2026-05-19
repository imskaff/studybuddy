package server.mediator;

import server.model.SubjectProposal;
import java.util.List;

public class SubjectProposalListResponse {
    private String status;
    private List<SubjectProposal> proposals;

    public SubjectProposalListResponse(String status, List<SubjectProposal> proposals) {
        this.status = status;
        this.proposals = proposals;
    }

    public String getStatus() { return status; }
    public List<SubjectProposal> getProposals() { return proposals; }
}