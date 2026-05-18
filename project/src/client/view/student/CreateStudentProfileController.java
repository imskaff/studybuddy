package client.view.student;

import client.view.ViewHandler;
import client.viewmodel.student.CreateStudentProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import java.sql.SQLException;

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
    password_textField.textProperty().bindBidirectional(viewModel.passwordProperty());
    phone_textField.textProperty().bindBidirectional(viewModel.phoneProperty());
    gender_textField.textProperty().bindBidirectional(viewModel.genderProperty());
    languages_textArea.textProperty().bindBidirectional(viewModel.languagesProperty());

    confirm_checkBtn.setSelected(false);

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
  }

  private void setUpButtons()
  {

    back_btn.setOnAction(event -> viewHandler.openCreateNewProfile());
    rules_btn.setOnAction(event -> viewHandler.openRules());
    register_btn.setOnAction(event -> {

      if(confirm_checkBtn.equals(true))
      {
        try
        {
          viewModel.register();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      else
      {
        System.err.println("Rules has to be confirmed to register");
      }
    });
  }

  public Region getRoot()
  {
    return root;
  }
}
