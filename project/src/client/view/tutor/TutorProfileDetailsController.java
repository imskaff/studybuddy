package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.TutorMainPageViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class TutorProfileDetailsController
{
  @FXML private TextField fname_textField;
  @FXML private TextField lname_textField;
  @FXML private TextField date_textField;
  @FXML private TextField email_textField;
  @FXML private TextField phone_textField;
  @FXML private TextField password_textField;
  @FXML private TextField gender_textField;

  @FXML private TextArea languages_textArea;
  @FXML private TextArea education_textArea;

  @FXML private Button back_btn;
  @FXML private Button confirm_btn;


  private Stage popupStage;
  private ViewHandler viewHandler = new ViewHandler();
  private TutorMainPageViewModel viewModel;
  private Region root;

  public TutorProfileDetailsController()
  {

  }

  public void init(Stage popupStage, TutorMainPageViewModel viewModel, Region root)
  {
    this.popupStage = popupStage;
    this.viewModel = viewModel;
    this.root = root;

    fname_textField.textProperty().bindBidirectional(viewModel.firstNameProperty());
    lname_textField.textProperty().bindBidirectional(viewModel.lastNameProperty());
    date_textField.textProperty().bindBidirectional(viewModel.dateOfBirthProperty());
    email_textField.textProperty().bindBidirectional(viewModel.emailProperty());
    phone_textField.textProperty().bindBidirectional(viewModel.phoneProperty());
    password_textField.textProperty().bindBidirectional(viewModel.passwordProperty());
    gender_textField.textProperty().bindBidirectional(viewModel.genderProperty());

    languages_textArea.textProperty().bindBidirectional(viewModel.languagesProperty());
    education_textArea.textProperty().bindBidirectional(viewModel.educationProperty());

    fname_textField.setEditable(true);
    lname_textField.setEditable(true);
    date_textField.setEditable(true);
    email_textField.setEditable(true);
    password_textField.setEditable(true);
    gender_textField.setEditable(true);
    languages_textArea.setEditable(true);
    education_textArea.setEditable(true);

    setUpButtons();
  }

  private void setUpButtons()
  {
    back_btn.setOnAction(event -> viewHandler.openTutorMainPageView());

    confirm_btn.setOnAction(event -> {
      boolean isSaved = viewModel.saveChanges();

      if(isSaved)
      {
        viewHandler.openTutorMainPageView();
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

  public Region getRoot()
  {
    return root;
  }
}
