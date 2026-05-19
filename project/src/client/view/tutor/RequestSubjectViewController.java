package client.view.tutor;

import client.viewmodel.tutor.RequestSubjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RequestSubjectViewController {
    @FXML private TextField subjectName_textField;
    @FXML private TextArea reason_textArea;

    private Stage stage;
    private RequestSubjectViewModel viewModel;

    public void init(Stage stage, RequestSubjectViewModel viewModel) {
        this.stage = stage;
        this.viewModel = viewModel;
        viewModel.reset();
        subjectName_textField.textProperty().bindBidirectional(viewModel.subjectNameProperty());
        reason_textArea.textProperty().bindBidirectional(viewModel.reasonProperty());
    }

    @FXML private void onSubmit() {
        if (subjectName_textField.getText().isBlank()) return;
        viewModel.submit();
        stage.close();
    }

    @FXML private void onCancel() {
        stage.close();
    }
}
