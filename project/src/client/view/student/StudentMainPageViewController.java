package client.view.student;

import client.view.ViewHandler;
import client.viewmodel.student.StudentMainPageViewModel;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import server.model.Session;
import server.model.Tutor;

/**
 * JavaFX controller for the student main page.
 * 
 * Manages three tabs:
 * <ol>
 *   <li><b>Available sessions</b> — filterable table bound to
 *       {@link StudentMainPageViewModel#getFilteredSessions()}</li>
 *   <li><b>Booked sessions</b> — table bound to
 *       {@link StudentMainPageViewModel#getBookedSessions()} with a cancel action</li>
 *   <li><b>Profile</b> — editable fields toggled by the Edit / Save button</li>
 * </ol>
 * All data operations are delegated to {@link StudentMainPageViewModel}.
 * 
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class StudentMainPageViewController {
    @FXML private TableView<Session> main_tableView;

    @FXML private TableColumn<Session, String> main_column_title;
    @FXML private TableColumn<Session, String> main_column_price;
    @FXML private TableColumn<Session, String> main_column_date;
    @FXML private TableColumn<Session, String> main_column_tutor;

    @FXML private TableView<Session> bookedSessions_tableView;
    @FXML private TableColumn<Session, String> title_column_bookedS;
    @FXML private TableColumn<Session, String> price_column_bookedS;
    @FXML private TableColumn<Session, String> date_column_bookedS;
    @FXML private TableColumn<Session, String> tutor_column_bookedS;
    @FXML private TableColumn<Session, String> status_column_bookedS;

    @FXML private Tab main_tab;
    @FXML private Tab profile_tab;
    @FXML private Tab bookedSessions_tab;
    @FXML private TextField fname_textArea;
    @FXML private TextField lname_textArea;
    @FXML private TextField email_textArea;
    @FXML private TextField phone_textArea;
    @FXML private ComboBox<String> gender_comboBox;
    @FXML private TextField dateOfBirth_textArea;
    @FXML private TextField language_textArea;

    @FXML private Button viewDetails_button;
    @FXML private Button viewDetails_btn_bookedS;
    @FXML private Button refreshBookedSessions_btn;
    @FXML private Button refresh_btn;
    @FXML private Button cancel_btn;
    @FXML private Button edit_button;

    @FXML private ComboBox<String> subject_comboBox;
    @FXML private TextField minPrice_textField;
    @FXML private TextField maxPrice_textField;
    @FXML private TextField fromDate_textField;
    @FXML private TextField toDate_textField;
    @FXML private Button reset_btn;

    private Region root;
    private ViewHandler viewHandler;
    private StudentMainPageViewModel viewModel;

    /** No-arg constructor required by the JavaFX FXML loader. */
    public StudentMainPageViewController() {}

    /**
     * Initialises the controller: configures all table columns, binds profile
     * fields and filter controls to ViewModel properties, wires buttons and
     * loads the initial data.
     *
     * @param viewHandler the application navigation handler
     * @param viewModel   the student main-page ViewModel
     * @param root        the root layout node
     */
    public void init(ViewHandler viewHandler, StudentMainPageViewModel viewModel, Region root) {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;

        main_column_title.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getSubject()));
        main_column_price.setCellValueFactory(data ->
            new SimpleStringProperty(String.format("%.2f", data.getValue().getPrice())));
        main_column_date.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getStart_date()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        main_column_tutor.setCellValueFactory(data -> {
            Tutor tutor = data.getValue().getTutor();
            return new SimpleStringProperty(tutor.getFirst_name() + " " + tutor.getLast_name());
        });
        main_tableView.setItems(viewModel.getFilteredSessions());

        title_column_bookedS.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getSubject()));
        price_column_bookedS.setCellValueFactory(data ->
            new SimpleStringProperty(String.format("%.2f", data.getValue().getPrice())));
        date_column_bookedS.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getStart_date()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        tutor_column_bookedS.setCellValueFactory(data -> {
            Tutor tutor = data.getValue().getTutor();
            return new SimpleStringProperty(tutor.getFirst_name() + " " + tutor.getLast_name());
        });
        status_column_bookedS.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getStatus() != null
                ? data.getValue().getStatus() : ""));
        bookedSessions_tableView.setItems(viewModel.getBookedSessions());

        fname_textArea.textProperty().bindBidirectional(viewModel.firstNameProperty());
        lname_textArea.textProperty().bindBidirectional(viewModel.lastNameProperty());
        dateOfBirth_textArea.textProperty().bindBidirectional(viewModel.dateOfBirthProperty());
        email_textArea.textProperty().bindBidirectional(viewModel.emailProperty());
        phone_textArea.textProperty().bindBidirectional(viewModel.phoneProperty());
        gender_comboBox.getItems().addAll("male", "female");
        gender_comboBox.valueProperty().bindBidirectional(viewModel.genderProperty());
        gender_comboBox.setDisable(true);
        language_textArea.textProperty().bindBidirectional(viewModel.languagesProperty());

        subject_comboBox.setItems(viewModel.getSubjectOptions());
        subject_comboBox.valueProperty().bindBidirectional(viewModel.subjectFilterProperty());
        minPrice_textField.textProperty().bindBidirectional(viewModel.minPriceProperty());
        maxPrice_textField.textProperty().bindBidirectional(viewModel.maxPriceProperty());
        fromDate_textField.textProperty().bindBidirectional(viewModel.fromDateProperty());
        toDate_textField.textProperty().bindBidirectional(viewModel.toDateProperty());

        cancel_btn.disableProperty().bind(
            bookedSessions_tableView.getSelectionModel().selectedItemProperty().isNull());

        cancel_btn.setOnAction(event -> {
            Session selected = bookedSessions_tableView.getSelectionModel().getSelectedItem();
            if(selected != null)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to cancel this session?",
                    ButtonType.YES, ButtonType.NO);
                alert.setTitle("Cancel Session");
                alert.setHeaderText("Confirm Cancellation");

                alert.showAndWait().ifPresent(response -> {
                    if(response == ButtonType.YES) {
                        viewModel.cancelSession(selected);
                        bookedSessions_tableView.getSelectionModel().clearSelection();
                    }
                });
            }
        });

        setFieldsReadOnly();
        setUpButtons();
        viewModel.loadData();
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
     * Makes all profile text fields and the gender combo-box non-editable.
     */
    private void setFieldsReadOnly() {
        fname_textArea.setEditable(false);
        lname_textArea.setEditable(false);
        dateOfBirth_textArea.setEditable(false);
        email_textArea.setEditable(false);
        phone_textArea.setEditable(false);
        gender_comboBox.setDisable(true);
        language_textArea.setEditable(false);
    }

    /**
     * Opens the session detail screen for the selected available session.
     * Does nothing if no row is selected.
     */
    @FXML private void onViewDetailsButtonClicked() {
        Session selected = main_tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewHandler.openStudentSessionView(selected);
        } else {
            System.out.println("No session selected");
        }
    }

    /**
     * Wires all remaining buttons: refresh, view-details (both tables),
     * filter reset and the cancel action (duplicate of the inline handler
     * set during {@link #init} to ensure the disable binding is applied
     * only once).
     */
    private void setUpButtons()
    {
      refresh_btn.setOnAction(event -> viewModel.loadSessions());

      viewDetails_button.disableProperty().bind(
          main_tableView.getSelectionModel().selectedItemProperty().isNull());

      refreshBookedSessions_btn.setOnAction(event -> viewModel.loadBookedSessions());

      viewDetails_btn_bookedS.disableProperty().bind(
          bookedSessions_tableView.getSelectionModel().selectedItemProperty().isNull());

      viewDetails_btn_bookedS.setOnAction(event -> {
          Session selected = bookedSessions_tableView.getSelectionModel().getSelectedItem();
          if (selected != null) {
              viewHandler.openStudentSessionView(selected);
          }
      });

      reset_btn.setOnAction(e -> viewModel.resetFilters());

      cancel_btn.disableProperty().bind(
          bookedSessions_tableView.getSelectionModel().selectedItemProperty().isNull());

      cancel_btn.setOnAction(event -> {
        Session selected = bookedSessions_tableView.getSelectionModel().getSelectedItem();

        if(selected != null)
        {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
              "Are you sure you want to cancel this session?",
              ButtonType.YES, ButtonType.NO);
              alert.setTitle("Cancel Session");
              alert.setHeaderText("Confirm Cancellation");

              alert.showAndWait().ifPresent(response -> {
                if(response == ButtonType.YES) {
                  viewModel.cancelSession(selected);
                  bookedSessions_tableView.getSelectionModel().clearSelection();
                }
              });
        }
      });
    }

    /**
     * Toggles between view and edit mode for the profile tab.
     * In edit mode the button label changes to "Save"; on a successful save
     * the fields revert to read-only. Shows an error alert if saving fails.
     */
    @FXML private void onEditButtonClicked() {
        boolean editing = fname_textArea.isEditable();
        if (editing) {
            boolean success = viewModel.saveProfile();
            if (success) {
                setFieldsReadOnly();
                edit_button.setText("Edit");
            } else {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Save Failed");
                alert.setHeaderText(null);
                alert.setContentText("Could not save profile. Please check your input and try again.");
                alert.showAndWait();
            }
        } else {
            setFieldsEditable();
            edit_button.setText("Save");
        }
    }

    /**
     * Makes all profile text fields and the gender combo-box editable.
     */
    private void setFieldsEditable() {
        fname_textArea.setEditable(true);
        lname_textArea.setEditable(true);
        dateOfBirth_textArea.setEditable(true);
        email_textArea.setEditable(true);
        phone_textArea.setEditable(true);
        gender_comboBox.setDisable(false);
        language_textArea.setEditable(true);
    }
}
