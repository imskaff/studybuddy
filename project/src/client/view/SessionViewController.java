package client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import server.model.Tutor;

import client.viewmodel.SessionViewModel;

public class SessionViewController
{
  @FXML private TextArea description_textArea;
  @FXML private TextArea tutor_textArea;
  @FXML private TextArea subject_textArea;
  @FXML private TextArea location_textArea;
  @FXML private TextArea duration_textArea;
  @FXML private TextArea price_textArea;
  @FXML private Button view_profile_button;
  @FXML private Button book_session_button;
  @FXML private Button back_button;

  private Region root;
  private SessionViewModel viewModel;
  private ViewHandler viewHandler;

  public SessionViewController()
  {
  }

  public void init(ViewHandler viewHandler, SessionViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    description_textArea.textProperty().bind(viewModel.descriptionProperty());
    tutor_textArea.textProperty().bind(viewModel.tutorProperty());
    subject_textArea.textProperty().bind(viewModel.subjectProperty());
    location_textArea.textProperty().bind(viewModel.locationProperty());
    duration_textArea.textProperty().bind(viewModel.durationProperty());
    price_textArea.textProperty().bind(viewModel.priceProperty());

    makeTextAreasReadOnly();
  }

  private void makeTextAreasReadOnly()
  {
    description_textArea.setEditable(false);
    tutor_textArea.setEditable(false);
    subject_textArea.setEditable(false);
    location_textArea.setEditable(false);
    duration_textArea.setEditable(false);
    price_textArea.setEditable(false);
  }

  public Region getRoot()
  {
    return root;
  }

  private void setUpButtons()
  {
    back_button.setOnAction(event -> viewHandler.openStudentMainPageView());

    book_session_button.setOnAction(event -> onBookSession());

    view_profile_button.setOnAction(event -> onViewTutorProfile());
  }

  @FXML private void onBookSession()
  {
    viewModel.bookSession();
  }

  @FXML private void onViewTutorProfile()
  {
    Tutor tutor = viewModel.getSelectedTutor();

    if (tutor != null)
    {
      viewHandler.openTutorProfileView(tutor);
    }
  }




}
