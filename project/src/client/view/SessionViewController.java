package client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

import client.viewmodel.SessionViewModel;

public class SessionViewController {
    @FXML private TextArea description_textArea;
    @FXML private TextArea tutor_textArea;
    @FXML private TextArea subject_textArea;
    @FXML private TextArea location_textArea;
    @FXML private TextArea duration_textArea;
    @FXML private TextArea price_textArea;
    @FXML private Button view_profile_button;
    @FXML private Button book_session_button;

    private Region root;
    private SessionViewModel viewModel;
    private ViewHandler viewHandler;
    
    public SessionViewController() {}

    public void init(ViewHandler viewHandler, SessionViewModel viewModel, Region root) {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;

        description_textArea.textProperty().bindBidirectional(viewModel.descriptionProperty());
        tutor_textArea.textProperty().bindBidirectional(viewModel.tutorProperty());
        subject_textArea.textProperty().bindBidirectional(viewModel.subjectProperty());
        location_textArea.textProperty().bindBidirectional(viewModel.locationProperty());
        duration_textArea.textProperty().bindBidirectional(viewModel.durationProperty());
        price_textArea.textProperty().bindBidirectional(viewModel.priceProperty());
    }

    public Region getRoot() {
        return root;
    }

    // fxml methods here \/
    
}
