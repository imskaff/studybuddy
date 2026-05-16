package client.view.student;

import client.view.ViewHandler;
import client.viewmodel.student.CreateStudentProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class CreateStudentProfileController
{
  @FXML private TextField fname_textField;
  @FXML private TextField lname_textField;
  @FXML private TextField email_textField;
  @FXML private TextField password_textField;
  @FXML private TextField phone_textField;
  @FXML private TextField date_textField;
  @FXML private TextField gender_textField;
  
  @FXML private TextArea languages_textArea;
  
  @FXML private CheckBox confirm_checkBtn;
  
  @FXML private Button rules_btn;
  @FXML private Button back_btn;
  @FXML private Button register_btn;
  
  private Region root;
  private ViewHandler viewHandler;
  private CreateStudentProfileViewModel viewModel;
  
  public CreateStudentProfileController()
  {
    
  }
  
  public void init(ViewHandler viewHandler, CreateStudentProfileViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    fname_textField.textProperty().bindBidirectional(viewModel.firstNameProperty());
    lname_textField.textProperty().bindBidirectional(viewModel.lastNameProperty());
    date_textField.textProperty().bindBidirectional(viewModel.dateOfBirthProperty());
    email_textField.textProperty().bindBidirectional(viewModel.emailProperty());
    phone_textField.textProperty().bindBidirectional(viewModel.phoneProperty());
    gender_textField.textProperty().bindBidirectional(viewModel.genderProperty());
    languages_textArea.textProperty().bindBidirectional(viewModel.languagesProperty());
    
    setUpButtons();
  }

  private void setUpButtons()
  {

    back_btn.setOnAction(event -> viewHandler.openCreateNewProfile());
    rules_btn.setOnAction(event -> viewHandler.openRules());
  }
}
