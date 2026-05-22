package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.CreateNewSessionViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class CreateNewSessionController
{
  @FXML private ComboBox<String> subject_comboBox;
  @FXML private TextField price_textField;
  @FXML private TextField location_textField;
  @FXML private TextField startDate_textField;
  @FXML private TextField endDate_textField;
  @FXML private TextArea description_textArea;

  @FXML private Button create_btn;
  @FXML private Button back_btn;

  private Stage popup;
  private ViewHandler viewHandler;
  private CreateNewSessionViewModel viewModel;
  private Region root;

  public void init(Stage popup, ViewHandler viewHandler, CreateNewSessionViewModel viewModel, Region root)
  {
    this.popup = popup;
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    viewModel.loadSubjects();
    subject_comboBox.setItems(viewModel.getSubjectOptions());
    subject_comboBox.valueProperty().bindBidirectional(viewModel.subjectProperty());
    price_textField.textProperty().bindBidirectional(viewModel.priceProperty());
    location_textField.textProperty().bindBidirectional(viewModel.locationProperty());
    startDate_textField.textProperty().bindBidirectional(viewModel.startDateProperty());
    endDate_textField.textProperty().bindBidirectional(viewModel.endDateProperty());
    description_textArea.textProperty().bindBidirectional(viewModel.descriptionProperty());

    setUpButtons();
  }

  private void setUpButtons()
  {
    back_btn.setOnAction(event -> popup.close());
    create_btn.setOnAction(event -> setCreate_btn());
  }

  private void setCreate_btn()
  {
    boolean success = viewModel.createSessionFromInput();
    if (success)
    {
      popup.close();
    }
    else
    {
      System.err.println(viewModel.errorProperty().get());
    }
  }
}
