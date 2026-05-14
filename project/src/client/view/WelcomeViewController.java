package client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import client.viewmodel.WelcomeViewModel;

public class WelcomeViewController {
    @FXML private TextField email_field;
    @FXML private PasswordField password_field;
    @FXML private Label error_label;
    @FXML private Button login_button;
    @FXML private Button register_button;

    private Region root;
    private WelcomeViewModel viewModel;
    private ViewHandler viewHandler;

    public WelcomeViewController(){}

    public void init(ViewHandler viewHandler, WelcomeViewModel viewModel, Region root) {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;
    }

    public Region getRoot(){
        return root;
    }

    @FXML private void onLoginButtonClicked() {
        String userType = viewModel.login(email_field.getText(), password_field.getText());
        if ("student".equals(userType)) {
            viewHandler.openStudentMainPageView();
        } else if ("tutor".equals(userType)) {
            viewHandler.openTutorMainPageView();
        } else if ("admin".equals(userType)) {
            viewHandler.openAdminView();
        } else {
            error_label.setText("Invalid email or password.");
            error_label.setVisible(true);
            error_label.setManaged(true);
        }
    }

    @FXML private void onRegisterButtonClicked() {
        // TODO: navigate to register view
    }
}
