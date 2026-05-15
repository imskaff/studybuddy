package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.SessionTutorViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class SessionTutorViewController
{
  @FXML private TextArea description_textArea;
  @FXML private TextField tutor_textField;
  @FXML private TextField subject_textField;
  @FXML private TextField location_textField;
  @FXML private TextField startDate_textField;
  @FXML private TextField endDate_textField;
  @FXML private TextField price_textField;
  @FXML private Button back_button;
  @FXML private Button edit_session_button;

  private Region root;
  private SessionTutorViewModel viewModel;
  private ViewHandler viewHandler;

  public SessionTutorViewController()
  {
  }

  public void init(ViewHandler viewHandler, SessionTutorViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    description_textArea.textProperty().bind(viewModel.descriptionProperty());
    tutor_textField.textProperty().bind(viewModel.tutorProperty());
    subject_textField.textProperty().bind(viewModel.subjectProperty());
    location_textField.textProperty().bind(viewModel.locationProperty());
    startDate_textField.textProperty().bind(viewModel.startDateProperty());
    endDate_textField.textProperty().bind(viewModel.endDateProperty());
    price_textField.textProperty().bind(viewModel.priceProperty());

    setFieldsReadOnly();
    setUpButtons();
  }

  private void setFieldsReadOnly()
  {
    description_textArea.setEditable(false);
    tutor_textField.setEditable(false);
    subject_textField.setEditable(false);
    location_textField.setEditable(false);
    startDate_textField.setEditable(false);
    endDate_textField.setEditable(false);
    price_textField.setEditable(false);
  }

  public Region getRoot()
  {
    return root;
  }

  private void setUpButtons()
  {
    back_button.setOnAction(event -> viewHandler.openTutorMainPageView());
    edit_session_button.setOnAction(event -> System.out.println("Edit session"));
  }
}
