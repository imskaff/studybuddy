package client.view.student;

import client.view.ViewHandler;
import client.viewmodel.student.EditStudentProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * JavaFX controller for the edit-student-profile popup.
 * 
 * Loads the logged-in student's current data into the editable fields on
 * initialisation and provides confirm / back buttons. On confirm, calls
 * {@link EditStudentProfileViewModel#saveChanges()}; on success navigates to
 * the student profile details screen, otherwise shows an error alert.
 * 
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class EditStudentProfileController
{
    @FXML private TextField fname_textField;
    @FXML private TextField lname_textField;
    @FXML private TextField date_textField;
    @FXML private TextField email_textField;
    @FXML private TextField password_textField;
    @FXML private TextField phone_textField;
    @FXML private TextField gender_textField;

    @FXML private TextArea languages_textArea;

    @FXML private Button back_btn;
    @FXML private Button confirm_btn;

    private Stage popupStage;
    private Region root;
    private EditStudentProfileViewModel viewModel;
    private ViewHandler viewHandler = new ViewHandler();

    /** No-arg constructor required by the JavaFX FXML loader. */
    public EditStudentProfileController()
    {
    }

  /**
   * Initialises the controller: calls
   * {@link EditStudentProfileViewModel#loadStudentData()}, binds all text
   * fields bidirectionally to the ViewModel properties and makes every
   * field editable.
   *
   * @param popupStage the stage hosting this popup
   * @param viewModel  the edit-student-profile ViewModel
   * @param root       the root layout node
   */
  public void init(Stage popupStage, EditStudentProfileViewModel viewModel, Region root)
  {
    this.popupStage = popupStage;
    this.root = root;
    this.viewModel = viewModel;

    viewModel.loadStudentData();

    fname_textField.textProperty().bindBidirectional(viewModel.firstNameProperty());
    lname_textField.textProperty().bindBidirectional(viewModel.lastNameProperty());
    date_textField.textProperty().bindBidirectional(viewModel.dateOfBirthProperty());
    email_textField.textProperty().bindBidirectional(viewModel.emailProperty());
    password_textField.textProperty().bindBidirectional(viewModel.passwordProperty());
    phone_textField.textProperty().bindBidirectional(viewModel.phoneProperty());
    gender_textField.textProperty().bindBidirectional(viewModel.genderProperty());
    languages_textArea.textProperty().bindBidirectional(viewModel.languagesProperty());

    fname_textField.setEditable(true);
    lname_textField.setEditable(true);
    date_textField.setEditable(true);
    email_textField.setEditable(true);
    password_textField.setEditable(true);
    gender_textField.setEditable(true);
    languages_textArea.setEditable(true);
  }

  /**
   * Wires the back and confirm buttons.
   * 
   * Back navigates to the student profile details screen. Confirm calls
   * {@link EditStudentProfileViewModel#saveChanges()}; on success navigates
   * to the profile details screen, on failure shows an error alert.
   * 
   */
  public void setUpButtons()
  {
    back_btn.setOnAction(event -> viewHandler.openStudentProfileDetails());

    confirm_btn.setOnAction(event -> {
      boolean isSaved = viewModel.saveChanges();

      if(isSaved)
      {
        viewHandler.openStudentProfileDetails();
      }
      else{
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Edit Failed");
        alert.setHeaderText("Could not save profile changes.");
        alert.setContentText("Please check your inputs and make sure no required fields are empty.");
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
