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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import server.model.Session;
import server.model.Tutor;

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
    @FXML private TextArea fname_textArea;
    @FXML private TextArea lname_textArea;
    @FXML private TextArea email_textArea;
    @FXML private TextArea phone_textArea;
    @FXML private TextArea gender_textArea;
    @FXML private TextArea dateOfBirth_textArea;
    @FXML private TextArea language_textArea;

    @FXML private Button viewDetails_button;
    @FXML private Button viewDetails_btn_bookedS;
    @FXML private Button cancel_btn;

    @FXML private ComboBox<String> subject_comboBox;
    @FXML private TextField minPrice_textField;
    @FXML private TextField maxPrice_textField;
    @FXML private TextField fromDate_textField;
    @FXML private TextField toDate_textField;
    @FXML private Button reset_btn;

    private Region root;
    private ViewHandler viewHandler;
    private StudentMainPageViewModel viewModel;

    public StudentMainPageViewController() {}

    public void init(ViewHandler viewHandler, StudentMainPageViewModel viewModel, Region root) {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;

        // available sessions
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
        viewDetails_button.disableProperty().bind(
            main_tableView.getSelectionModel().selectedItemProperty().isNull());

        // booked sessions
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
            new SimpleStringProperty(data.getValue().getState() != null
                ? data.getValue().getState().getClass().getSimpleName() : ""));
        bookedSessions_tableView.setItems(viewModel.getBookedSessions());
        viewDetails_btn_bookedS.disableProperty().bind(
            bookedSessions_tableView.getSelectionModel().selectedItemProperty().isNull());

        // profile fields
        fname_textArea.textProperty().bindBidirectional(viewModel.firstNameProperty());
        lname_textArea.textProperty().bindBidirectional(viewModel.lastNameProperty());
        dateOfBirth_textArea.textProperty().bindBidirectional(viewModel.dateOfBirthProperty());
        email_textArea.textProperty().bindBidirectional(viewModel.emailProperty());
        phone_textArea.textProperty().bindBidirectional(viewModel.phoneProperty());
        gender_textArea.textProperty().bindBidirectional(viewModel.genderProperty());
        language_textArea.textProperty().bindBidirectional(viewModel.languagesProperty());

        // filter bar
        subject_comboBox.setItems(viewModel.getSubjectOptions());
        subject_comboBox.valueProperty().bindBidirectional(viewModel.subjectFilterProperty());
        minPrice_textField.textProperty().bindBidirectional(viewModel.minPriceProperty());
        maxPrice_textField.textProperty().bindBidirectional(viewModel.maxPriceProperty());
        fromDate_textField.textProperty().bindBidirectional(viewModel.fromDateProperty());
        toDate_textField.textProperty().bindBidirectional(viewModel.toDateProperty());
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

        setFieldsReadOnly();
        viewModel.loadData();
    }

    public Region getRoot() {
        return root;
    }

    private void setFieldsReadOnly() {
        fname_textArea.setEditable(false);
        lname_textArea.setEditable(false);
        dateOfBirth_textArea.setEditable(false);
        email_textArea.setEditable(false);
        phone_textArea.setEditable(false);
        gender_textArea.setEditable(false);
        language_textArea.setEditable(false);
    }

    @FXML private void onViewDetailsButtonClicked() {
        Session selected = main_tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewHandler.openStudentSessionView(selected);
        } else {
            System.out.println("No session selected");
        }
    }

    @FXML private void onEditButtonClicked() {
        // TODO: implement profile editing
    }
}
