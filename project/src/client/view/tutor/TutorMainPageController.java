package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.TutorMainPageViewModel;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import server.model.Session;
import server.model.SessionState;

public class TutorMainPageController
{
  @FXML private TableView<Session> mySessions_tView;
  @FXML private TableColumn<Session, String>  subject_column;
  @FXML private TableColumn<Session, String>  price_column;
  @FXML private TableColumn<Session, String>  date_column;
  @FXML private TableColumn<Session, String>  state_column;
  @FXML private TableColumn<Session, String>  description_column;

  @FXML private TextField fname_textField;
  @FXML private TextField lname_textField;
  @FXML private TextField email_textField;
  @FXML private TextField phone_textField;
  @FXML private TextField gender_textField;
  @FXML private TextField dateOfBirth_textField;

  @FXML private TextArea language_textArea;
  @FXML private TextArea education_textArea;

  @FXML private Button viewDetails_btn;
  @FXML private Button create_btn;
  private Region root;
  private ViewHandler viewHandler;
  private TutorMainPageViewModel viewModel;

  public void init(ViewHandler viewHandler, TutorMainPageViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    //main
    subject_column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubject()));
    price_column.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().getPrice())));
    date_column.setCellValueFactory(data ->
        new SimpleStringProperty(data.getValue().getStart_date()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
    state_column.setCellValueFactory(cellData -> {
        SessionState state = cellData.getValue().getState();
        return new SimpleStringProperty(state != null ? state.getClass().getSimpleName() : "");
    });
    description_column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

    mySessions_tView.setItems(viewModel.getSessions());
    viewDetails_btn.disableProperty().bind(
        mySessions_tView.getSelectionModel().selectedItemProperty().isNull());

    //profile
    fname_textField.textProperty().bindBidirectional(viewModel.firstNameProperty());
    lname_textField.textProperty().bindBidirectional(viewModel.lastNameProperty());
    dateOfBirth_textField.textProperty().bindBidirectional(viewModel.dateOfBirthProperty());
    email_textField.textProperty().bindBidirectional(viewModel.emailProperty());
    phone_textField.textProperty().bindBidirectional(viewModel.phoneProperty());
    gender_textField.textProperty().bindBidirectional(viewModel.genderProperty());
    language_textArea.textProperty().bindBidirectional(viewModel.languagesProperty());
    education_textArea.textProperty().bindBidirectional(viewModel.educationProperty());

    setFieldsReadOnly();
    viewModel.loadData();
    setUpButtons();
  }

  private void setFieldsReadOnly()
  {
    fname_textField.setEditable(false);
    lname_textField.setEditable(false);
    dateOfBirth_textField.setEditable(false);
    email_textField.setEditable(false);
    phone_textField.setEditable(false);
    gender_textField.setEditable(false);
    language_textArea.setEditable(false);
    education_textArea.setEditable(false);
  }

  @FXML private void onViewDetails()
  {
      Session selected = mySessions_tView.getSelectionModel().getSelectedItem();
      if(selected != null)
      {
        viewHandler.openTutorSessionView(selected);
      }
      else {
        System.out.println("No session selected.");
      }
  }

  private void setUpButtons()
  {
    viewDetails_btn.setOnAction(event -> onViewDetails());
    create_btn.setOnAction(event -> viewHandler.openCreateNewSession());
  }
}
