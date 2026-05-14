package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.TutorMainPageViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import server.model.Session;

public class TutorMainPageController {
    @FXML private TableView<Session> mySessions_tView;
    @FXML private TableColumn<Session, String> title_column;
    @FXML private TableColumn<Session, String> price_column;
    @FXML private TableColumn<Session, String> date_column;
    @FXML private TableColumn<Session, String> state_column;
    @FXML private Button viewDetails_btn;
    @FXML private Button create_btn;
    @FXML private Button remove_btn;
    @FXML private TextField fname_textField;
    @FXML private TextField lname_textField;
    @FXML private TextField dateOfBirth_textField;
    @FXML private TextField gender_textField;
    @FXML private TextField email_textField;
    @FXML private TextField phone_textField;
    @FXML private TextArea languages_textField;
    @FXML private TextArea education_textArea;
    @FXML private TextArea description_textArea;
    @FXML private Button edit_btn;

    private Region root;
    private ViewHandler viewHandler;
    private TutorMainPageViewModel viewModel;

    public TutorMainPageController() {}

    public void init(ViewHandler viewHandler, TutorMainPageViewModel viewModel, Region root) {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;
    }

    public Region getRoot() {
        return root;
    }
}
