package client.view;

import client.viewmodel.WelcomeViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * JavaFX controller for the login / welcome screen.
 * 
 * Delegates credential validation to {@link WelcomeViewModel#login(String, String)}
 * and navigates to the appropriate main page based on the returned user-type
 * string. Shows an error label for invalid credentials and a warning dialog
 * for pending tutor accounts.
 * 
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class WelcomeViewController {
    @FXML private TextField email_field;
    @FXML private PasswordField password_field;
    @FXML private Label error_label;
    @FXML private Button login_button;
    @FXML private Button register_button;

    private Region root;
    private WelcomeViewModel viewModel;
    private ViewHandler viewHandler;

    /** No-arg constructor required by the JavaFX FXML loader. */
    public WelcomeViewController(){}

    /**
     * Initialises the controller, wires the register button and makes the
     * error label invisible until a failed login occurs.
     *
     * @param viewHandler the application navigation handler
     * @param viewModel   the login ViewModel
     * @param root        the root layout node
     */
    public void init(ViewHandler viewHandler, WelcomeViewModel viewModel, Region root) {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;

        onRegisterButtonClicked();
    }

    /**
     * Returns the root layout node.
     *
     * @return the root {@link Region}
     */
    public Region getRoot(){
        return root;
    }

    /**
     * Handles a login attempt. Calls {@link WelcomeViewModel#login(String, String)}
     * with the values of the email and password fields, then:
     * <ul>
     *   <li>{@code "student"} → opens the student main page</li>
     *   <li>{@code "tutor"} → opens the tutor main page</li>
     *   <li>{@code "admin"} → opens the admin panel</li>
     *   <li>{@code "tutor_pending"} → shows a warning dialog</li>
     *   <li>any other value → shows the error label</li>
     * </ul>
     */
    @FXML private void onLoginButtonClicked() {
        String userType = viewModel.login(email_field.getText(), password_field.getText());
        if ("student".equals(userType)) {
            viewHandler.openStudentMainPageView();
        } else if ("tutor".equals(userType)) {
            viewHandler.openTutorMainPageView();
        } else if ("admin".equals(userType)) {
            viewHandler.openAdminView();
        } else if ("tutor_pending".equals(userType)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Account Pending Approval");
            alert.setHeaderText(null);
            alert.setContentText("Your tutor account has not been approved yet. Please wait for an administrator to review your application.");
            alert.showAndWait();
        } else {
            error_label.setText("Invalid email or password.");
            error_label.setVisible(true);
            error_label.setManaged(true);
        }
    }

    /**
     * Wires the register button to navigate to the profile-type selection screen.
     */
    @FXML private void onRegisterButtonClicked() {
        register_button.setOnAction(event -> viewHandler.openCreateNewProfile());
    }
}
