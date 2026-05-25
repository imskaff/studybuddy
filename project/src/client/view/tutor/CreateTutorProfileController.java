package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.CreateTutorProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import javafx.scene.control.Alert;

public class CreateTutorProfileController
{
  @FXML private TextField fname_textField;
  @FXML private TextField lname_textField;
  @FXML private TextField email_textField;
  @FXML private TextField password_textField;
  @FXML private TextField phone_textField;
  @FXML private TextField date_textField;
  @FXML private TextField gender_textField;

  @FXML private TextArea languages_textArea;
  @FXML private TextArea education_textArea;

  @FXML private CheckBox rules_checkBox;

  @FXML private Button register_Btn;
  @FXML private Button rules_Btn;
  @FXML private Button back_Btn;


  private CreateTutorProfileViewModel viewModel;
  private ViewHandler viewHandler;
  private Region root;

  public CreateTutorProfileController()
  {

  }

  public void init(ViewHandler viewHandler, CreateTutorProfileViewModel createTutorViewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = createTutorViewModel;
    this.root = root;

    fname_textField.textProperty().bindBidirectional(viewModel.firstNameProperty());
    lname_textField.textProperty().bindBidirectional(viewModel.lastNameProperty());
    date_textField.textProperty().bindBidirectional(viewModel.dateOfBirthProperty());
    email_textField.textProperty().bindBidirectional(viewModel.emailProperty());
    password_textField.textProperty().bindBidirectional(viewModel.passwordProperty());
    phone_textField.textProperty().bindBidirectional(viewModel.phoneNoProperty());
    gender_textField.textProperty().bindBidirectional(viewModel.genderProperty());
    languages_textArea.textProperty().bindBidirectional(viewModel.languageInputProperty());
    education_textArea.textProperty().bindBidirectional(viewModel.educationLevelProperty());

    rules_checkBox.setSelected(false);
    setFieldsEditable(true);
    setUpButtons();
  }

  public void setFieldsEditable(boolean editable)
  {
    fname_textField.setEditable(editable);
    lname_textField.setEditable(editable);
    date_textField.setEditable(editable);
    email_textField.setEditable(editable);
    password_textField.setEditable(editable);
    phone_textField.setEditable(editable);
    gender_textField.setEditable(editable);
    languages_textArea.setEditable(editable);
    education_textArea.setEditable(editable);
  }

  private void setUpButtons()
  {

    back_Btn.setOnAction(event -> viewHandler.openCreateNewProfile());
    rules_Btn.setOnAction(event -> viewHandler.openRules());
    register_Btn.setOnAction(event -> {
      if (!rules_checkBox.isSelected()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Rules Required");
        alert.setHeaderText(null);
        alert.setContentText("You must accept the rules before registering.");
        alert.showAndWait();
        return;
      }
      boolean success = viewModel.register();
      if (success) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Submitted");
        alert.setHeaderText(null);
        alert.setContentText("Your tutor application has been submitted and is pending admin approval.");
        alert.showAndWait();
        viewHandler.openWelcomeView();
      } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Failed");
        alert.setHeaderText(null);
        alert.setContentText("Registration failed. The email may already be in use or some fields are invalid.");
        alert.showAndWait();
      }
    });
  }

  public Region getRoot()
  {
    return root;
  }
}
