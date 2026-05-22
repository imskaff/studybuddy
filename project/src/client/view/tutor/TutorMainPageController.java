package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.TutorMainPageViewModel;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import server.model.Session;
import server.model.SessionState;

public class TutorMainPageController
{
  @FXML private TableView<Session> mySessions_tView;
  @FXML private TableColumn<Session, String> subject_column;
  @FXML private TableColumn<Session, String> price_column;
  @FXML private TableColumn<Session, String> date_column;
  @FXML private TableColumn<Session, String> state_column;
  @FXML private TableColumn<Session, String> description_column;

  @FXML private TextField fname_textField;
  @FXML private TextField lname_textField;
  @FXML private TextField email_textField;
  @FXML private TextField phone_textField;
  @FXML private TextField dateOfBirth_textField;
  @FXML private ComboBox<String> gender_comboBox;
  @FXML private TextField language_textField;
  @FXML private TextArea education_textArea;

  @FXML private Button viewDetails_btn;
  @FXML private Button create_btn;
  @FXML private Button edit_btn;

  private Region root;
  private ViewHandler viewHandler;
  private TutorMainPageViewModel viewModel;

  public void init(ViewHandler viewHandler, TutorMainPageViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

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

    fname_textField.textProperty().bindBidirectional(viewModel.firstNameProperty());
    lname_textField.textProperty().bindBidirectional(viewModel.lastNameProperty());
    dateOfBirth_textField.textProperty().bindBidirectional(viewModel.dateOfBirthProperty());
    email_textField.textProperty().bindBidirectional(viewModel.emailProperty());
    phone_textField.textProperty().bindBidirectional(viewModel.phoneProperty());
    gender_comboBox.getItems().addAll("male", "female");
    gender_comboBox.valueProperty().bindBidirectional(viewModel.genderProperty());
    gender_comboBox.setDisable(true);
    language_textField.textProperty().bindBidirectional(viewModel.languagesProperty());
    education_textArea.textProperty().bindBidirectional(viewModel.educationProperty());

    setFieldsReadOnly();
    viewModel.loadData();

    viewDetails_btn.setOnAction(event -> onViewDetails());
    create_btn.setOnAction(event -> viewHandler.openCreateNewSession());
  }

  private void setFieldsReadOnly()
  {
    fname_textField.setEditable(false);
    lname_textField.setEditable(false);
    dateOfBirth_textField.setEditable(false);
    email_textField.setEditable(false);
    phone_textField.setEditable(false);
    gender_comboBox.setDisable(true);
    language_textField.setEditable(false);
    education_textArea.setEditable(false);
  }

  private void setFieldsEditable()
  {
    fname_textField.setEditable(true);
    lname_textField.setEditable(true);
    dateOfBirth_textField.setEditable(true);
    email_textField.setEditable(true);
    phone_textField.setEditable(true);
    gender_comboBox.setDisable(false);
    language_textField.setEditable(true);
    education_textArea.setEditable(true);
  }

  @FXML private void onViewDetails()
  {
    Session selected = mySessions_tView.getSelectionModel().getSelectedItem();
    if (selected != null)
      viewHandler.openTutorSessionView(selected);
  }

  @FXML private void onEditButtonClicked()
  {
    boolean editing = fname_textField.isEditable();
    if (editing) {
      boolean success = viewModel.saveProfile();
      if (success) {
        setFieldsReadOnly();
        edit_btn.setText("Edit");
      } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Save Failed");
        alert.setHeaderText(null);
        alert.setContentText("Could not save profile. Please check your input and try again.");
        alert.showAndWait();
      }
    } else {
      setFieldsEditable();
      edit_btn.setText("Save");
    }
  }

  public Region getRoot()
  {
    return root;
  }
}
