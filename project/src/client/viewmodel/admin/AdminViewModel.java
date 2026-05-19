package client.viewmodel.admin;

import client.model.Model;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.SubjectProposal;
import server.model.Tutor;

public class AdminViewModel {
    private final Model model;
    private final ObservableList<Tutor> pendingTutors;
    private final ObservableList<SubjectProposal> subjectProposals;

    public AdminViewModel(Model model) {
        this.model = model;
        this.pendingTutors = FXCollections.observableArrayList();
        this.subjectProposals = FXCollections.observableArrayList();
    }

    public void loadPendingTutors() {
        pendingTutors.setAll(model.getPendingTutors());
    }

    public void loadSubjectProposals() {
        subjectProposals.setAll(model.getSubjectProposals());
    }

    public void approveTutor(Tutor tutor) {
        Thread t = new Thread(() -> {
            boolean success = model.approveTutor(tutor.getId());
            if (success)
                Platform.runLater(() -> pendingTutors.remove(tutor));
        });
        t.setDaemon(true);
        t.start();
    }

    public void declineTutor(Tutor tutor) {
        Thread t = new Thread(() -> {
            boolean success = model.declineTutor(tutor.getId());
            if (success)
                Platform.runLater(() -> pendingTutors.remove(tutor));
        });
        t.setDaemon(true);
        t.start();
    }

    public void approveProposal(SubjectProposal proposal) {
        Thread t = new Thread(() -> {
            boolean success = model.handleSubjectProposal(proposal.getId(), "APPROVE");
            if (success)
                Platform.runLater(() -> subjectProposals.remove(proposal));
        });
        t.setDaemon(true);
        t.start();
    }

    public void rejectProposal(SubjectProposal proposal) {
        Thread t = new Thread(() -> {
            boolean success = model.handleSubjectProposal(proposal.getId(), "REJECT");
            if (success)
                Platform.runLater(() -> subjectProposals.remove(proposal));
        });
        t.setDaemon(true);
        t.start();
    }

    public ObservableList<Tutor> getPendingTutors() {
        return pendingTutors;
    }

    public ObservableList<SubjectProposal> getSubjectProposals() {
        return subjectProposals;
    }
}
