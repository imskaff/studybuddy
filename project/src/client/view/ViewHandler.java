package client.view;

import client.view.student.SessionViewController;
import client.view.student.StudentMainPageViewController;
import client.view.student.TutorProfileViewController;
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
    private Scene currentScene;

    public ViewHandler(ViewModelFactory viewModelFactory) {
        this.viewModelFactory = viewModelFactory;
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        openWelcomeView(); // welcome page - register/logging in
    }

    private void openWelcomeView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("WelcomeView.fxml"));
            Region root = loader.load();
            WelcomeViewController controller = loader.getController();
            controller.init(this, viewModelFactory.getWelcomeViewModel(), root);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Welcome");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openStudentMainPageView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("StudentMainPageView.fxml"));
            Region root = loader.load();
            StudentMainPageViewController controller = loader.getController();
            controller.init(this, viewModelFactory.getStudentMainPageViewModel(), root);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Student Main Page");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openSessionView(Session selected) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("SessionView.fxml"));
            Region root = loader.load();
            SessionViewController controller = loader.getController();
            controller.init(this, viewModelFactory.getSessionViewModel(), root);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Session");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  public void openTutorProfileView(Tutor tutor)
  {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("student/TutorProfileView.fxml"));
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

  public void openTutorMainPageView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/TutorMainPageView.fxml"));
      Region root = loader.load();
        TutorMainPageViewController controller = loader.getController();
        controller.init(this, viewModelFactory.getTutorMainPageViewModel(), root);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Tutor Main Page");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    catch (Exception e)    {
      e.printStackTrace();
    }
}

  /*public void openStudentMainPageView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/StudentMainPageView.fxml"));
      Region root = loader.load();

      StudentMainPageViewController controller = loader.getController();
      controller.init(this, viewModelFactory.getStudentMainPageViewModel(), root);

      currentScene.setRoot(root);
      primaryStage.setTitle("Session Details");
      primaryStage.setScene(currentScene);
      primaryStage.show();

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }*/
}
