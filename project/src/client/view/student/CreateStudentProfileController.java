package client.view.student;

import client.view.ViewHandler;
import client.viewmodel.student.CreateStudentProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * JavaFX controller for the student registration form.
 * 
 * Binds all input fields bidirectionally to the
 * {@link CreateStudentProfileViewModel}. The register button is enabled only
 * when the rules check-box is ticked; on success it shows a confirmation
 * dialog and navigates to the welcome screen.
 * 
 *
 * @author Kamila Jeremicz
 */
public class CreateStudentProfileController
{
  @FXML private TextField fname_textField;
  @FXML private TextField lname_textField;
  @FXML private TextField email_textField;
  @FXML private TextField password_textField;
  @FXML private TextField phone_textField;
  @FXML private TextField date_textField;
  @FXML private ComboBox<String> gender_comboBox;

  @FXML private TextArea languages_textArea;

  @FXML private CheckBox confirm_checkBtn;

  @FXML private Button rules_btn;
  @FXML private Button back_btn;
  @FXML private Button register_btn;

  private Region root;
  private ViewHandler viewHandler;
  private CreateStudentProfileViewModel viewModel;

  /** No-arg constructor required by the JavaFX FXML loader. */
  public CreateStudentProfileController()
  {
  }

  /**
   * Initialises the controller: binds all form fields to ViewModel properties,
   * populates the gender combo-box, resets the rules check-box, enables all
   * fields and wires the buttons.
   *
   * @param viewHandler the application navigation handler
   * @param viewModel   the create-student-profile ViewModel
   * @param root        the root layout node
   */
  public void init(ViewHandler viewHandler, CreateStudentProfileViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    fname_textField.textProperty().bindBidirectional(viewModel.firstNameProperty());
    lname_textField.textProperty().bindBidirectional(viewModel.lastNameProperty());
    date_textField.textProperty().bindBidirectional(viewModel.dateOfBirthProperty());
    email_textField.textProperty().bindBidirectional(viewModel.emailProperty());
    password_textField.textProperty().bindBidirectional(viewModel.passwordProperty());
    phone_textField.textProperty().bindBidirectional(viewModel.phoneNoProperty());
    gender_comboBox.getItems().addAll("male", "female");
    gender_comboBox.valueProperty().bindBidirectional(viewModel.genderProperty());
    languages_textArea.textProperty().bindBidirectional(viewModel.languageInputProperty());

    confirm_checkBtn.setSelected(false);

    setFieldsEditable(true);
    setUpButtons();
  }

  /**
   * Sets the editable state of all input fields and the gender combo-box.
   *
   * @param editable {@code true} to allow user input; {@code false} for read-only
   */
  public void setFieldsEditable(boolean editable)
  {
    fname_textField.setEditable(editable);
    lname_textField.setEditable(editable);
    date_textField.setEditable(editable);
    email_textField.setEditable(editable);
    password_textField.setEditable(editable);
    phone_textField.setEditable(editable);
    gender_comboBox.setDisable(!editable);
    languages_textArea.setEditable(editable);
  }

  /**
   * Wires the back, rules and register buttons.
   * 
   * The register button requires the rules check-box to be selected; if not,
   * a warning alert is shown. On a successful registration an information
   * alert is displayed and the application navigates to the welcome screen;
   * on failure an error alert is shown.
   * 
   */
  private void setUpButtons()
  {
    back_btn.setOnAction(event -> viewHandler.openCreateNewProfile());
    rules_btn.setOnAction(event -> viewHandler.openRules());
    register_btn.setOnAction(event -> {
      if (!confirm_checkBtn.isSelected()) {
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
        alert.setTitle("Registration Successful");
        alert.setHeaderText(null);
        alert.setContentText("Your account has been created. You can now log in.");
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

  /**
   * Returns the root layout node.
   *
   * @return the root {@link Region}
   */
  public Region getRoot()
  {
    return root;
  }
}
