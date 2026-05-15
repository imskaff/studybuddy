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
  @FXML private Button delete_button;

  private Region root;
  private SessionTutorViewModel viewModel;
  private ViewHandler viewHandler;
  private boolean editMode = false;

  public SessionTutorViewController()
  {
  }

  public void init(ViewHandler viewHandler, SessionTutorViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    // tutor name is display-only
    tutor_textField.textProperty().bind(viewModel.tutorProperty());

    // editable fields so user input flows back to ViewModel
    description_textArea.textProperty().bindBidirectional(viewModel.descriptionProperty());
    subject_textField.textProperty().bindBidirectional(viewModel.subjectProperty());
    location_textField.textProperty().bindBidirectional(viewModel.locationProperty());
    startDate_textField.textProperty().bindBidirectional(viewModel.startDateProperty());
    endDate_textField.textProperty().bindBidirectional(viewModel.endDateProperty());
    price_textField.textProperty().bindBidirectional(viewModel.priceProperty());

    setFieldsEditable(false);
    setUpButtons();
  }

  private void setFieldsEditable(boolean editable)
  {
    description_textArea.setEditable(editable);
    subject_textField.setEditable(editable);
    location_textField.setEditable(editable);
    startDate_textField.setEditable(editable);
    endDate_textField.setEditable(editable);
    price_textField.setEditable(editable);
    // tutor_textField is always read-only
    tutor_textField.setEditable(false);
  }

  public Region getRoot()
  {
    return root;
  }

  private void setUpButtons()
  {
    back_button.setOnAction(event -> viewHandler.openTutorMainPageView());
    edit_session_button.setOnAction(event -> onEditOrSave());
    delete_button.setOnAction(event -> onDelete());
  }

  private void onEditOrSave()
  {
    if (!editMode)
    {
      editMode = true;
      setFieldsEditable(true);
      edit_session_button.setText("Save");
    }
    else
    {
      boolean success = viewModel.editSession();
      if (success)
      {
        viewHandler.openTutorMainPageView();
      }
      else
      {
        System.err.println("Failed to save session changes.");
      }
    }
  }

  private void onDelete()
  {
    boolean success = viewModel.deleteSession();
    if (success)
    {
      viewHandler.openTutorMainPageView();
    }
    else
    {
      System.err.println("Failed to delete session.");
    }
  }
}
