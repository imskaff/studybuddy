package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.CreateTutorProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * JavaFX controller for the tutor registration form.
 * 
 * Binds all input fields bidirectionally to the
 * {@link CreateTutorProfileViewModel}. The register button requires the rules
 * check-box to be ticked; on success it shows an information dialog noting
 * that the application is pending admin approval, then navigates to the
 * welcome screen.
 * 
 *
 * @author
 */
public class CreateTutorProfileController
{
  @FXML private TextField fname_textField;
  @FXML private TextField lname_textField;
  @FXML private TextField email_textField;
  @FXML private TextField password_textField;
  @FXML private TextField phone_textField;
  @FXML private TextField date_textField;
  @FXML private ComboBox<String> gender_comboBox;

  @FXML private TextArea languages_textArea;
  @FXML private TextArea education_textArea;

  @FXML private CheckBox rules_checkBox;

  @FXML private Button register_Btn;
  @FXML private Button rules_Btn;
  @FXML private Button back_Btn;

  private CreateTutorProfileViewModel viewModel;
  private ViewHandler viewHandler;
  private Region root;

  /** No-arg constructor required by the JavaFX FXML loader. */
  public CreateTutorProfileController()
  {
  }

  /**
   * Initialises the controller: binds all form fields to ViewModel properties,
   * populates the gender combo-box, resets the rules check-box, enables all
   * fields, and wires the buttons.
   *
   * @param viewHandler             the application navigation handler
   * @param createTutorViewModel    the create-tutor-profile ViewModel
   * @param root                    the root layout node
   */
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
    gender_comboBox.getItems().addAll("male", "female");
    gender_comboBox.valueProperty().bindBidirectional(viewModel.genderProperty());
    languages_textArea.textProperty().bindBidirectional(viewModel.languageInputProperty());
    education_textArea.textProperty().bindBidirectional(viewModel.educationLevelProperty());

    rules_checkBox.setSelected(false);
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
    education_textArea.setEditable(editable);
  }

  /**
   * Wires the back, rules, and register buttons.
   * 
   * The register button requires the rules check-box to be selected; if not,
   * a warning alert is shown. On a successful registration an information
   * alert is shown noting that the application is pending admin approval,
   * and the application navigates to the welcome screen. On failure an error
   * alert is shown.
   * 
   */
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
