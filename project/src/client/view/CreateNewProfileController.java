package client.view;

import client.viewmodel.CreateNewProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Region;

public class CreateNewProfileController
{

  @FXML private CheckBox tutor_checkBtn;
  @FXML private CheckBox student_checkBtn;
  @FXML private Button confirm_Btn;
  @FXML private Button back_Btn;

  private Region root;
  private ViewHandler viewHandler;
  private CreateNewProfileViewModel viewModel;

  public void init(ViewHandler viewHandler, CreateNewProfileViewModel viewModel, Region root)
  {
    this.root = root;
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;

    back_Btn.setOnAction(event -> viewHandler.openWelcomeView());
    confirm_Btn.setOnAction(event -> chooseUserType());

    tutor_checkBtn.setSelected(false);
    student_checkBtn.setSelected(false);

    tutor_checkBtn.setOnAction(event -> { if (tutor_checkBtn.isSelected()) student_checkBtn.setSelected(false); });
    student_checkBtn.setOnAction(event -> { if (student_checkBtn.isSelected()) tutor_checkBtn.setSelected(false); });
  }

  private void chooseUserType()
  {
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
  }

  private void userTypeNotChosenAlert()
  {
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    errorAlert.setTitle("Validation Error");
    errorAlert.setHeaderText(null);
    errorAlert.setContentText("You have to choose the user type");

    errorAlert.showAndWait();
  }

  public Region getRoot()
  {
    return root;
  }
}
