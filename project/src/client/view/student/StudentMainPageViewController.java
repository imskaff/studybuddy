package client.view.student;

import client.view.ViewHandler;
import client.viewmodel.student.StudentMainPageViewModel;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.*;
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


    @FXML private Button viewDetails_button;
    @FXML private Button viewDetails_btn_bookedS;
    @FXML private Button viewProfileDetails_btn;
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


        // filter bar
        subject_comboBox.setItems(viewModel.getSubjectOptions());
        subject_comboBox.valueProperty().bindBidirectional(viewModel.subjectFilterProperty());
        minPrice_textField.textProperty().bindBidirectional(viewModel.minPriceProperty());
        maxPrice_textField.textProperty().bindBidirectional(viewModel.maxPriceProperty());
        fromDate_textField.textProperty().bindBidirectional(viewModel.fromDateProperty());
        toDate_textField.textProperty().bindBidirectional(viewModel.toDateProperty());

        setUpButtons();
        viewModel.loadData();
    }

    public Region getRoot() {
        return root;
    }



    @FXML private void onViewDetailsButtonClicked() {
        Session selected = main_tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewHandler.openStudentSessionView(selected);
        } else {
            System.out.println("No session selected");
        }
    }

    private void setUpButtons()
    {
      viewProfileDetails_btn.setOnAction(event -> viewHandler.openStudentProfileDetails());
      //Available sessions
      viewDetails_button.disableProperty().bind(
          main_tableView.getSelectionModel().selectedItemProperty().isNull());
      //Booked Sessions
      viewDetails_btn_bookedS.disableProperty().bind(
          bookedSessions_tableView.getSelectionModel().selectedItemProperty().isNull());
      //Filtering
      reset_btn.setOnAction(e -> viewModel.resetFilters());
      //Cancel Session

    }


}
