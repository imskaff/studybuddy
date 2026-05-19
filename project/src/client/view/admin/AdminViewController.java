package client.view.admin;

import client.view.ViewHandler;
import client.viewmodel.admin.AdminViewModel;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import server.model.SubjectProposal;
import server.model.Tutor;

public class AdminViewController {
    @FXML private TableView<Tutor> pendingTutors_tableView;
    @FXML private TableColumn<Tutor, String> name_column;
    @FXML private TableColumn<Tutor, String> email_column;
    @FXML private TableColumn<Tutor, String> education_column;
    @FXML private TableColumn<Tutor, String> languages_column;
    @FXML private Button approve_btn;
    @FXML private Button decline_btn;

    @FXML private TableView<SubjectProposal> subjectProposals_tableView;
    @FXML private TableColumn<SubjectProposal, String> proposalTutor_column;
    @FXML private TableColumn<SubjectProposal, String> proposalSubject_column;
    @FXML private TableColumn<SubjectProposal, String> proposalReason_column;
    @FXML private TableColumn<SubjectProposal, String> proposalStatus_column;
    @FXML private TableColumn<SubjectProposal, String> proposalDate_column;
    @FXML private Button approveProposal_btn;
    @FXML private Button rejectProposal_btn;

    private Region root;
    private ViewHandler viewHandler;
    private AdminViewModel viewModel;

    public AdminViewController() {}

    public void init(ViewHandler viewHandler, AdminViewModel viewModel, Region root) {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;

        // tutor registrations tab
        name_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getFirst_name() + " " + data.getValue().getLast_name()));
        email_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getEmail()));
        education_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getEducation_level()));
        languages_column.setCellValueFactory(data ->
            new SimpleStringProperty(String.join(", ", data.getValue().getLanguages())));

        pendingTutors_tableView.setItems(viewModel.getPendingTutors());
        approve_btn.disableProperty().bind(pendingTutors_tableView.getSelectionModel().selectedItemProperty().isNull());
        decline_btn.disableProperty().bind(pendingTutors_tableView.getSelectionModel().selectedItemProperty().isNull());
        viewModel.loadPendingTutors();

        // subject requests tab
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        proposalTutor_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getTutorEmail()));
        proposalSubject_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getSubjectName()));
        proposalReason_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getReason()));
        proposalStatus_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getStatus()));
        proposalDate_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getRequestedAt().format(formatter)));

        subjectProposals_tableView.setItems(viewModel.getSubjectProposals());
        approveProposal_btn.disableProperty().bind(subjectProposals_tableView.getSelectionModel().selectedItemProperty().isNull());
        rejectProposal_btn.disableProperty().bind(subjectProposals_tableView.getSelectionModel().selectedItemProperty().isNull());
        viewModel.loadSubjectProposals();
    }

    public Region getRoot() {
        return root;
    }

    @FXML private void onApproveButtonClicked() {
        Tutor selected = pendingTutors_tableView.getSelectionModel().getSelectedItem();
        if (selected != null)
            viewModel.approveTutor(selected);
    }

    @FXML private void onDeclineButtonClicked() {
        Tutor selected = pendingTutors_tableView.getSelectionModel().getSelectedItem();
        if (selected != null)
            viewModel.declineTutor(selected);
    }

    @FXML private void onApproveProposalClicked() {
        SubjectProposal selected = subjectProposals_tableView.getSelectionModel().getSelectedItem();
        if (selected != null)
            viewModel.approveProposal(selected);
    }

    @FXML private void onRejectProposalClicked() {
        SubjectProposal selected = subjectProposals_tableView.getSelectionModel().getSelectedItem();
        if (selected != null)
            viewModel.rejectProposal(selected);
    }
}
