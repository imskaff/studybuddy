package client.view;

import client.viewmodel.CreateNewProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Region;

/**
 * JavaFX controller for the profile-type selection screen.
 * 
 * Presents two mutually exclusive check-boxes (student / tutor). The confirm
 * button navigates to the appropriate registration form; the back button
 * returns to the welcome screen. If neither type is selected an error alert
 * is shown.
 * 
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class CreateNewProfileController
{

  @FXML private CheckBox tutor_checkBtn;
  @FXML private CheckBox student_checkBtn;
  @FXML private Button confirm_Btn;
  @FXML private Button back_Btn;

  private Region root;
  private ViewHandler viewHandler;
  private CreateNewProfileViewModel viewModel;

  /**
   * Initialises the controller, enforces mutual exclusion between the two
   * check-boxes, wires the back and confirm buttons and resets both
   * check-boxes to unchecked.
   *
   * @param viewHandler the application navigation handler
   * @param viewModel   the create-new-profile ViewModel
   * @param root        the root layout node
   */
  public void init(ViewHandler viewHandler, CreateNewProfileViewModel viewModel, Region root)
  {
    this.root = root;
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;

    back_Btn.setOnAction(event -> viewHandler.openWelcomeView());

    setupConfirmButton();
    chooseUserType();
    tutor_checkBtn.setSelected(false);
    student_checkBtn.setSelected(false);
  }

  /**
   * Wires the confirm button so it navigates to the tutor or student
   * registration form depending on which check-box is selected, or shows
   * an error alert when neither is selected.
   */
  private void setupConfirmButton()
  {
    confirm_Btn.setOnAction(event -> {
      if (tutor_checkBtn.isSelected())
      {
        viewHandler.openCreateTutorProfile();
      }
      else if (student_checkBtn.isSelected())
      {
        viewHandler.openCreateStudentProfile();
      }
      else
      {
        userTypeNotChosenAlert();
      }
    });
  }

  /**
   * Enforces mutual exclusion: selecting one check-box automatically
   * deselects the other.
   */
  private void chooseUserType()
  {
    tutor_checkBtn.setOnAction(e -> {
      if (tutor_checkBtn.isSelected()) {
        student_checkBtn.setSelected(false);
      }
    });

    student_checkBtn.setOnAction(e -> {
      if (student_checkBtn.isSelected()) {
        tutor_checkBtn.setSelected(false);
      }
    });
  }

  /**
   * Shows an error dialog prompting the user to select a profile type before
   * confirming.
   */
  private void userTypeNotChosenAlert()
  {
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    errorAlert.setTitle("Validation Error");
    errorAlert.setHeaderText(null);
    errorAlert.setContentText("You have to choose the user type");

    errorAlert.showAndWait();
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
