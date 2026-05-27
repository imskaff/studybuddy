package client.view.student;

import client.viewmodel.student.TutorProfileViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * JavaFX controller for the tutor public-profile popup (student view).
 * 
 * All fields are bound one-way to the {@link TutorProfileViewModel} and are
 * set to read-only. The back button closes the popup stage.
 * 
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
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
    private Stage popupStage;
    private TutorProfileViewModel viewModel;

    /** No-arg constructor required by the JavaFX FXML loader. */
    public TutorProfileViewController() {}

    /**
     * Initialises the controller: binds all display fields to ViewModel
     * properties, makes them read-only and wires the back button to close
     * the popup stage.
     *
     * @param popupStage the stage hosting this popup
     * @param viewModel  the tutor-profile ViewModel (tutor already loaded)
     * @param root       the root layout node
     */
    public void init(Stage popupStage, TutorProfileViewModel viewModel, Region root) {
        this.popupStage = popupStage;
        this.root = root;
        this.viewModel = viewModel;

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

    /**
     * Returns the root layout node.
     *
     * @return the root {@link Region}
     */
    public Region getRoot() { return root; }
}
