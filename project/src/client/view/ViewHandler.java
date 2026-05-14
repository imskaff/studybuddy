package client.view;

import client.view.student.SessionViewController;
import client.view.student.StudentMainPageViewController;
import client.view.tutor.CreateNewSessionController;
import client.view.tutor.TutorProfileViewController;
import client.view.tutor.TutorMainPageController;
import client.viewmodel.ViewModelFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import server.model.Session;
import server.model.Tutor;

public class ViewHandler {
    private final ViewModelFactory viewModelFactory;
    private Stage primaryStage;

    public ViewHandler(ViewModelFactory viewModelFactory) {
        this.viewModelFactory = viewModelFactory;
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        openWelcomeView();
    }

    private void openWelcomeView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("WelcomeView.fxml"));
            Region root = loader.load();
            WelcomeViewController controller = loader.getController();
            controller.init(this, viewModelFactory.getWelcomeViewModel(), root);

            primaryStage.setTitle("Welcome");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openStudentMainPageView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("student/StudentMainPageView.fxml"));
            Region root = loader.load();
            StudentMainPageViewController controller = loader.getController();
            controller.init(this, viewModelFactory.getStudentMainPageViewModel(), root);

            primaryStage.setTitle("Student Main Page");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openSessionView(Session selected) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("student/SessionView.fxml"));
            Region root = loader.load();
            SessionViewController controller = loader.getController();
            viewModelFactory.getSessionViewModel().loadSession(selected);
            controller.init(this, viewModelFactory.getSessionViewModel(), root);

            primaryStage.setTitle("Session");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openTutorProfileView(Tutor tutor) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("tutor/TutorProfileView.fxml"));
            Region root = loader.load();
            TutorProfileViewController controller = loader.getController();
            viewModelFactory.getTutorProfileViewModel().loadTutor(tutor);

            Stage popupStage = new Stage();
            controller.init(popupStage, viewModelFactory.getTutorProfileViewModel(), root);
            popupStage.setTitle("Tutor Profile");
            popupStage.setScene(new Scene(root));
            popupStage.setResizable(false);
            popupStage.initOwner(primaryStage);
            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openTutorMainPageView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("tutor/TutorMainPage.fxml"));
            Region root = loader.load();
            TutorMainPageController controller = loader.getController();
            controller.init(this, viewModelFactory.getTutorMainPageViewModel(), root);

            primaryStage.setTitle("Tutor Main Page");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openAdminView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AdminView.fxml"));
            Region root = loader.load();
            AdminViewController controller = loader.getController();
            controller.init(this, viewModelFactory.getAdminViewModel(), root);

            primaryStage.setTitle("Admin Panel");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
