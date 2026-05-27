package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.CreateNewSessionViewModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import server.model.Session;
import server.model.Tutor;

/**
 * JavaFX controller for the create-new-session popup.
 * 
 * Binds all input fields bidirectionally to the
 * {@link CreateNewSessionViewModel}. The create button parses the field
 * values, constructs a {@link Session} and delegates to
 * {@link CreateNewSessionViewModel#createSession(Session)}. Date-time values
 * must be in the format {@code yyyy-MM-dd HH:mm}.
 * 
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
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

  /**
   * Initialises the controller: binds all form fields to ViewModel properties,
   * loads the subject list and wires the back and create buttons.
   *
   * @param popup       the popup {@link Stage} (closed on back or successful creation)
   * @param viewHandler the application navigation handler
   * @param viewModel   the create-new-session ViewModel
   * @param root        the root layout node
   */
  public void init(Stage popup, ViewHandler viewHandler, CreateNewSessionViewModel viewModel, Region root)
  {
    this.popup = popup;
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    subject_comboBox.setItems(viewModel.getSubjectOptions());
    subject_comboBox.valueProperty().bindBidirectional(viewModel.subjectProperty());
    price_textField.textProperty().bindBidirectional(viewModel.priceProperty());
    location_textField.textProperty().bindBidirectional(viewModel.locationProperty());
    startDate_textField.textProperty().bindBidirectional(viewModel.startDateProperty());
    endDate_textField.textProperty().bindBidirectional(viewModel.endDateProperty());
    description_textArea.textProperty().bindBidirectional(viewModel.descriptionProperty());

    loadSubjects();
    setUpButtons();
  }

  /**
   * Wires the back button to close the popup and the create button to
   * {@link #setCreate_btn()}.
   */
  private void setUpButtons()
  {
    back_btn.setOnAction(event -> popup.close());
    create_btn.setOnAction(event -> setCreate_btn());
  }

  /**
   * Reads and validates the form fields, builds a {@link Session} and calls
   * {@link CreateNewSessionViewModel#createSession(Session)}.
   * Closes the popup on success. Logs an error message if the price is not a
   * valid number, the date format is wrong, or any other exception occurs.
   * 
   * Expected date-time format: {@code yyyy-MM-dd HH:mm} (e.g.
   * {@code 2024-05-20 14:30}).
   * 
   */
  private void setCreate_btn()
  {
    try
    {
      String subject = viewModel.subjectProperty().get();
      double price = Double.parseDouble(viewModel.priceProperty().get());
      String location = viewModel.locationProperty().get();
      String description = viewModel.descriptionProperty().get();

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
          "yyyy-MM-dd HH:mm");

      LocalDateTime startDate = LocalDateTime.parse(
          viewModel.startDateProperty().get(), formatter);
      LocalDateTime endDate = LocalDateTime.parse(
          viewModel.endDateProperty().get(), formatter);

      Tutor currentTutor = viewModel.getCurrentTutor();
      Session session = new Session(description, price, subject, location,
          startDate, endDate, currentTutor);

      boolean success = viewModel.createSession(session);

      if (success)
      {
        popup.close();
      }
      else
      {
        System.err.println("Database error: Could not create session.");
      }
    }
  catch (NumberFormatException e) {
  System.err.println("Invalid input: Price must be a valid number.");
} catch (DateTimeParseException e) {

  System.err.println("Invalid date format! Please use: yyyy-MM-dd HH:mm (e.g., 2024-05-20 14:30)");
} catch (Exception e) {
  e.printStackTrace();
}
  }

  /**
   * Fetches the subject list from the ViewModel and populates the subject
   * combo-box.
   */
  private void loadSubjects()
  {
    viewModel.loadSubjects();

    System.out.println("Subjects loaded from DB: " + viewModel.getSubjectOptions());

    subject_comboBox.setItems(viewModel.getSubjectOptions());
  }
}
