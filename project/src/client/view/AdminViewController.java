package client.view;

import client.viewmodel.AdminViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import server.model.Tutor;

public class AdminViewController {
    @FXML private TableView<Tutor> pendingTutors_tableView;
    @FXML private TableColumn<Tutor, String> name_column;
    @FXML private TableColumn<Tutor, String> email_column;
    @FXML private TableColumn<Tutor, String> education_column;
    @FXML private TableColumn<Tutor, String> languages_column;
    @FXML private Button approve_btn;
    @FXML private Button decline_btn;

    private Region root;
    private ViewHandler viewHandler;
    private AdminViewModel viewModel;

    public AdminViewController() {}

    public void init(ViewHandler viewHandler, AdminViewModel viewModel, Region root) {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;

        name_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getFirst_name() + " " + data.getValue().getLast_name()));
        email_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getEmail()));
        education_column.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getEducation_level()));
        languages_column.setCellValueFactory(data ->
            new SimpleStringProperty(String.join(", ", data.getValue().getLanguages())));

        pendingTutors_tableView.setItems(viewModel.getPendingTutors());
        viewModel.loadPendingTutors();
    }

    public Region getRoot() {
        return root;
    }

    @FXML private void onApproveButtonClicked() {
        Tutor selected = pendingTutors_tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.approveTutor(selected);
        }
    }

    @FXML private void onDeclineButtonClicked() {
        Tutor selected = pendingTutors_tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.declineTutor(selected);
        }
    }
}
