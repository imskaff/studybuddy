package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.TutorMainPageViewModel;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import server.model.Session;

/**
 * JavaFX controller for the tutor main page.
 * 
 * Manages two areas:
 * <ul>
 *   <li><b>Sessions table</b> — shows all sessions owned by the logged-in
 *       tutor with subject, price, date, status, and description columns.
 *       Provides view-details, create, cancel, and refresh actions.</li>
 *   <li><b>Profile fields</b> — editable via an Edit / Save toggle button;
 *       delegates persistence to {@link TutorMainPageViewModel#saveProfile()}.</li>
 * </ul>
 * 
 *
 * @author
 */
public class TutorMainPageController
{
  @FXML private TableView<Session> mySessions_tView;
  @FXML private TableColumn<Session, String> subject_column;
  @FXML private TableColumn<Session, String> price_column;
  @FXML private TableColumn<Session, String> date_column;
  @FXML private TableColumn<Session, String> state_column;
  @FXML private TableColumn<Session, String> description_column;

  @FXML private TextField fname_textField;
  @FXML private TextField lname_textField;
  @FXML private TextField email_textField;
  @FXML private TextField phone_textField;
  @FXML private TextField dateOfBirth_textField;
  @FXML private ComboBox<String> gender_comboBox;
  @FXML private TextField language_textField;
  @FXML private TextArea education_textArea;

  @FXML private Button refresh_btn;
  @FXML private Button viewDetails_btn;
  @FXML private Button create_btn;
  @FXML private Button edit_btn;
  @FXML private Button cancel_btn;

  private Region root;
  private ViewHandler viewHandler;
  private TutorMainPageViewModel viewModel;

  /**
   * Initialises the controller: configures all table column cell-value
   * factories, binds the table to the ViewModel's sessions list, binds
   * profile fields bidirectionally to ViewModel properties, makes fields
   * read-only, wires buttons, and loads data.
   *
   * @param viewHandler the application navigation handler
   * @param viewModel   the tutor main-page ViewModel
   * @param root        the root layout node
   */
  public void init(ViewHandler viewHandler, TutorMainPageViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    subject_column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubject()));
    price_column.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().getPrice())));
    date_column.setCellValueFactory(data ->
        new SimpleStringProperty(data.getValue().getStart_date()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
    state_column.setCellValueFactory(cellData ->
        new SimpleStringProperty(cellData.getValue().getStatus())
    );
    description_column.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getDescription()));

    mySessions_tView.setItems(viewModel.getSessions());

    fname_textField.textProperty().bindBidirectional(viewModel.firstNameProperty());
    lname_textField.textProperty().bindBidirectional(viewModel.lastNameProperty());
    dateOfBirth_textField.textProperty().bindBidirectional(viewModel.dateOfBirthProperty());
    email_textField.textProperty().bindBidirectional(viewModel.emailProperty());
    phone_textField.textProperty().bindBidirectional(viewModel.phoneProperty());
    gender_comboBox.getItems().addAll("male", "female");
    gender_comboBox.valueProperty().bindBidirectional(viewModel.genderProperty());
    language_textField.textProperty().bindBidirectional(viewModel.languagesProperty());
    education_textArea.textProperty().bindBidirectional(viewModel.educationProperty());

    setFieldsReadOnly();
    setUpButtons();
    viewModel.loadData();
  }

  /**
   * Wires the refresh, view-details, create, edit, and cancel buttons.
   * 
   * The cancel button is disabled unless the selected session has status
   * {@code "Booked"}. It shows a confirmation dialog before delegating to
   * {@link TutorMainPageViewModel#cancelSession(Session)}.
   * 
   */
  private void setUpButtons()
  {
    refresh_btn.setOnAction(event -> viewModel.loadSessions());

    viewDetails_btn.disableProperty().bind(
        mySessions_tView.getSelectionModel().selectedItemProperty().isNull());
    viewDetails_btn.setOnAction(event -> onViewDetails());

    create_btn.setOnAction(event -> {
      viewHandler.openCreateNewSession();
      viewModel.loadSessions();
    });

    edit_btn.setOnAction(event -> onEditButtonClicked());

    cancel_btn.disableProperty().bind(
        javafx.beans.binding.Bindings.createBooleanBinding(
            () -> {
              Session s = mySessions_tView.getSelectionModel().getSelectedItem();
              return s == null || !"Booked".equals(s.getStatus());
            },
            mySessions_tView.getSelectionModel().selectedItemProperty()
        ));
    cancel_btn.setOnAction(event -> {
      Session selected = mySessions_tView.getSelectionModel().getSelectedItem();
      if (selected != null)
      {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to cancel this session?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Cancel Session");
        alert.setHeaderText("Confirm Cancellation");
        alert.showAndWait().ifPresent(response -> {
          if (response == ButtonType.YES)
          {
            viewModel.cancelSession(selected);
            mySessions_tView.getSelectionModel().clearSelection();
          }
        });
      }
    });
  }

  /**
   * Opens the tutor session detail screen for the currently selected session.
   * Does nothing if no row is selected.
   */
  @FXML private void onViewDetails()
  {
    Session selected = mySessions_tView.getSelectionModel().getSelectedItem();
    if (selected != null)
    {
      viewHandler.openTutorSessionView(selected);
    }
  }

  /**
   * Toggles between view and edit mode for the profile fields.
   * In edit mode the button label changes to "Save"; on a successful save the
   * fields revert to read-only. Shows an error alert if saving fails.
   */
  @FXML private void onEditButtonClicked()
  {
    boolean editing = fname_textField.isEditable();
    if (editing)
    {
      boolean success = viewModel.saveProfile();
      if (success)
      {
        setFieldsReadOnly();
        edit_btn.setText("Edit");
      }
      else
      {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Save Failed");
        alert.setHeaderText(null);
        alert.setContentText("Could not save profile. Please check your input and try again.");
        alert.showAndWait();
      }
    }
    else
    {
      setFieldsEditable();
      edit_btn.setText("Save");
    }
  }

  /**
   * Makes all profile fields and the gender combo-box non-editable.
   */
  private void setFieldsReadOnly()
  {
    fname_textField.setEditable(false);
    lname_textField.setEditable(false);
    dateOfBirth_textField.setEditable(false);
    email_textField.setEditable(false);
    phone_textField.setEditable(false);
    gender_comboBox.setDisable(true);
    language_textField.setEditable(false);
    education_textArea.setEditable(false);
  }

  /**
   * Makes all profile fields and the gender combo-box editable.
   */
  private void setFieldsEditable()
  {
    fname_textField.setEditable(true);
    lname_textField.setEditable(true);
    dateOfBirth_textField.setEditable(true);
    email_textField.setEditable(true);
    phone_textField.setEditable(true);
    gender_comboBox.setDisable(false);
    language_textField.setEditable(true);
    education_textArea.setEditable(true);
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
