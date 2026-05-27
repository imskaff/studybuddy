package client.view.student;

import client.view.ViewHandler;
import client.viewmodel.student.EditStudentProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * JavaFX controller for the read-only student profile details popup.
 * 
 * Loads the logged-in student's data into non-editable fields. Provides a
 * back button that closes the popup and an edit button that navigates to the
 * {@link EditStudentProfileController} screen.
 * 
 *
 * @author
 */
public class StudentProfileDetailsController
{
  @FXML private TextField fname_textField;
  @FXML private TextField lname_textField;
  @FXML private TextField date_textField;
  @FXML private TextField email_textField;
  @FXML private TextField phone_textField;
  @FXML private TextField gender_textField;

  @FXML private TextArea languages_textArea;

  @FXML private Button back_btn;
  @FXML private Button edit_btn;

  private ViewHandler viewHandler = new ViewHandler();
  private EditStudentProfileViewModel viewModel;
  private Region root;
  private Stage popupStage;

  /** No-arg constructor required by the JavaFX FXML loader. */
  public StudentProfileDetailsController()
  {
  }

  /**
   * Initialises the controller: calls
   * {@link EditStudentProfileViewModel#loadStudentData()}, binds all text
   * fields bidirectionally to the ViewModel properties, sets them read-only,
   * and wires the back and edit buttons.
   *
   * @param popupStage the stage hosting this popup
   * @param viewModel  the edit-student-profile ViewModel (used for data binding)
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
    phone_textField.textProperty().bindBidirectional(viewModel.phoneProperty());
    gender_textField.textProperty().bindBidirectional(viewModel.genderProperty());
    languages_textArea.textProperty().bindBidirectional(viewModel.languagesProperty());

    fname_textField.setEditable(false);
    lname_textField.setEditable(false);
    date_textField.setEditable(false);
    email_textField.setEditable(false);
    gender_textField.setEditable(false);
    languages_textArea.setEditable(false);

    setUpButtons();
  }

  /**
   * Wires the back button to close the popup and the edit button to open
   * the edit-student-profile screen.
   */
  private void setUpButtons()
  {
    back_btn.setOnAction(e -> popupStage.close());
    edit_btn.setOnAction(event -> viewHandler.openEditStudentProfileDetails());
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
