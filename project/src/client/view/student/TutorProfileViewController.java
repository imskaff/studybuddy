package client.view.student;

import client.view.ViewHandler;
import client.viewmodel.TutorProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class TutorProfileViewController {
    @FXML private TextField fname_textField;
    @FXML private TextField lname_textField;
    @FXML private TextField age_textField;
    @FXML private TextField email_textField;
    @FXML private TextField gender_textField;
    @FXML private TextArea  languages_textArea;
    @FXML private TextArea  education_textArea;
    @FXML private Button    back_btn;

    private Region root;
    private TutorProfileViewModel viewModel;


    public TutorProfileViewController() {}

    public void init(ViewHandler viewHandler, TutorProfileViewModel viewModel, Region root) {
        this.viewModel  = viewModel;
        this.root       = root;

        fname_textField.textProperty().bind(viewModel.firstNameProperty());
        lname_textField.textProperty().bind(viewModel.lastNameProperty());
        age_textField.textProperty().bind(viewModel.dateOfBirthProperty());
        email_textField.textProperty().bind(viewModel.emailProperty());
        gender_textField.textProperty().bind(viewModel.genderProperty());
        languages_textArea.textProperty().bind(viewModel.languagesProperty());
        education_textArea.textProperty().bind(viewModel.educationLevelProperty());

        fname_textField.setEditable(false);
        lname_textField.setEditable(false);
        age_textField.setEditable(false);
        email_textField.setEditable(false);
        gender_textField.setEditable(false);
        languages_textArea.setEditable(false);
        education_textArea.setEditable(false);

        back_btn.setOnAction(e -> popupStage.close());
    }

    public Region getRoot() { return root; }
}
