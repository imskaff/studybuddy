package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.SessionTutorViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * JavaFX controller for the session detail / edit screen (tutor view).
 * 
 * Starts in read-only mode. The Edit button toggles to edit mode, changes
 * its label to "Save", and enables all editable fields. On a subsequent
 * Save click the controller delegates to
 * {@link SessionTutorViewModel#editSession()} and navigates back on success.
 * The Delete button delegates to {@link SessionTutorViewModel#deleteSession()}.
 * The tutor-name field is always read-only.
 * 
 *
 * @author
 */
public class SessionTutorViewController
{
  @FXML private TextArea description_textArea;
  @FXML private TextField tutor_textField;
  @FXML private ComboBox<String> subject_comboBox;
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
  /** {@code true} when the form is in edit mode (fields are editable). */
  private boolean editMode = false;

  /** No-arg constructor required by the JavaFX FXML loader. */
  public SessionTutorViewController()
  {
  }

  /**
   * Initialises the controller: binds the tutor-name field one-way, binds
   * all editable fields bidirectionally to ViewModel properties, loads the
   * subject options, starts in read-only mode, and wires the buttons.
   *
   * @param viewHandler the application navigation handler
   * @param viewModel   the session-tutor ViewModel (session already loaded)
   * @param root        the root layout node
   */
  public void init(ViewHandler viewHandler, SessionTutorViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    tutor_textField.textProperty().bind(viewModel.tutorProperty());

    description_textArea.textProperty().bindBidirectional(viewModel.descriptionProperty());
    viewModel.loadSubjects();
    subject_comboBox.setItems(viewModel.getSubjectOptions());
    subject_comboBox.valueProperty().bindBidirectional(viewModel.subjectProperty());
    location_textField.textProperty().bindBidirectional(viewModel.locationProperty());
    startDate_textField.textProperty().bindBidirectional(viewModel.startDateProperty());
    endDate_textField.textProperty().bindBidirectional(viewModel.endDateProperty());
    price_textField.textProperty().bindBidirectional(viewModel.priceProperty());

    setFieldsEditable(false);
    setUpButtons();
  }

  /**
   * Sets the editable state of all user-editable fields.
   * The tutor-name field is always kept read-only.
   *
   * @param editable {@code true} to allow editing; {@code false} for read-only
   */
  private void setFieldsEditable(boolean editable)
  {
    description_textArea.setEditable(editable);
    subject_comboBox.setDisable(!editable);
    location_textField.setEditable(editable);
    startDate_textField.setEditable(editable);
    endDate_textField.setEditable(editable);
    price_textField.setEditable(editable);
    tutor_textField.setEditable(false);
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

  /**
   * Wires the back, edit/save, and delete buttons.
   */
  private void setUpButtons()
  {
    back_button.setOnAction(event -> viewHandler.openTutorMainPageView());
    edit_session_button.setOnAction(event -> onEditOrSave());
    delete_button.setOnAction(event -> onDelete());
  }

  /**
   * Toggles between view and edit mode.
   * <ul>
   *   <li>First click — enters edit mode, enables fields, changes button label
   *       to "Save".</li>
   *   <li>Second click — calls {@link SessionTutorViewModel#editSession()};
   *       on success navigates back to the tutor main page.</li>
   * </ul>
   */
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

  /**
   * Deletes the currently selected session. Navigates back to the tutor main
   * page on success.
   */
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
