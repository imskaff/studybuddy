package client.view.admin;

import client.view.ViewHandler;
import client.viewmodel.admin.AdminViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import server.model.Tutor;

/**
 * JavaFX controller for the administrator panel.
 * 
 * Displays a table of pending tutor registrations and provides approve /
 * decline actions. Column cell-value factories format the tutor data directly
 * from the {@link Tutor} model; the observable list is owned by
 * {@link AdminViewModel}.
 * 
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
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

    /** No-arg constructor required by the JavaFX FXML loader. */
    public AdminViewController() {}

    /**
     * Initialises the controller: configures column cell-value factories,
     * binds the table to the ViewModel's pending-tutors list and triggers
     * an initial data load.
     *
     * @param viewHandler the application navigation handler
     * @param viewModel   the admin ViewModel
     * @param root        the root layout node
     */
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

    /**
     * Returns the root layout node.
     *
     * @return the root {@link Region}
     */
    public Region getRoot() {
        return root;
    }

    /**
     * Approves the currently selected tutor by delegating to
     * {@link AdminViewModel#approveTutor(Tutor)}. Does nothing if no row is
     * selected.
     */
    @FXML private void onApproveButtonClicked() {
        Tutor selected = pendingTutors_tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.approveTutor(selected);
        }
    }

    /**
     * Declines the currently selected tutor by delegating to
     * {@link AdminViewModel#declineTutor(Tutor)}. Does nothing if no row is
     * selected.
     */
    @FXML private void onDeclineButtonClicked() {
        Tutor selected = pendingTutors_tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.declineTutor(selected);
        }
    }
}
