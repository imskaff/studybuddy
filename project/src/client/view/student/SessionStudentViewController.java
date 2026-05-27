package client.view.student;

import client.view.ViewHandler;
import client.viewmodel.student.SessionStudentViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import server.model.Tutor;

/**
 * JavaFX controller for the session detail screen (student view).
 * 
 * Displays all session fields as read-only and provides two actions:
 * <ul>
 *   <li><b>Book session</b> — runs the booking on a daemon thread to avoid
 *       blocking the JavaFX application thread, then navigates back to the
 *       main page.</li>
 *   <li><b>View tutor profile</b> — opens the tutor public-profile popup.</li>
 * </ul>
 * 
 *
 * @author
 */
public class SessionStudentViewController
{
  @FXML private TextArea description_textArea;
  @FXML private TextField tutor_textField;
  @FXML private TextField subject_textField;
  @FXML private TextField location_textField;
  @FXML private TextField startDate_textField;
  @FXML private TextField endDate_textField;
  @FXML private TextField price_textField;
  @FXML private Button view_profile_button;
  @FXML private Button book_session_button;
  @FXML private Button back_button;

  private Region root;
  private SessionStudentViewModel viewModel;
  private ViewHandler viewHandler;

  /** No-arg constructor required by the JavaFX FXML loader. */
  public SessionStudentViewController()
  {
  }

  /**
   * Initialises the controller: binds all display fields to ViewModel
   * properties (one-way), makes fields read-only, and wires the buttons.
   *
   * @param viewHandler the application navigation handler
   * @param viewModel   the session-student ViewModel (session already loaded)
   * @param root        the root layout node
   */
  public void init(ViewHandler viewHandler, SessionStudentViewModel viewModel, Region root)
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

  /**
   * Makes all display fields non-editable.
   */
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
   * Wires the back, book, and view-profile buttons.
   */
  private void setUpButtons()
  {
    back_button.setOnAction(event -> viewHandler.openStudentMainPageView());
    book_session_button.setOnAction(event -> onBookSession());
    view_profile_button.setOnAction(event -> onViewTutorProfile());
  }

  /**
   * Books the selected session on a daemon background thread to avoid
   * blocking the UI, disables the button to prevent double-clicks, and
   * navigates back to the student main page on the JavaFX application thread
   * once complete.
   */
  @FXML private void onBookSession()
  {
    book_session_button.setDisable(true);
    Thread t = new Thread(() -> {
      viewModel.bookSession();
      Platform.runLater(() -> viewHandler.openStudentMainPageView());
    });
    t.setDaemon(true);
    t.start();
  }

  /**
   * Opens the tutor public-profile popup for the tutor of the selected
   * session. Does nothing if no tutor is associated.
   */
  @FXML private void onViewTutorProfile()
  {
    Tutor tutor = viewModel.getSelectedTutor();
    if (tutor != null)
    {
      viewHandler.openTutorProfileView(tutor);
    }
  }
}
