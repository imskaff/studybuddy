package client.view;

import client.view.admin.AdminViewController;
import client.view.student.CreateStudentProfileController;
import client.view.student.SessionStudentViewController;
import client.view.student.StudentMainPageViewController;
import client.view.student.TutorProfileViewController;
import client.view.tutor.CreateNewSessionController;
import client.view.tutor.CreateTutorProfileController;
import client.view.tutor.SessionTutorViewController;
import client.view.tutor.TutorMainPageController;
import client.viewmodel.ViewModelFactory;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import server.model.Session;
import server.model.Tutor;

public class ViewHandler
{
  private final ViewModelFactory viewModelFactory;
  private Stage primaryStage;

  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
  }

  public void start(Stage primaryStage)
  {
    this.primaryStage = primaryStage;
    openWelcomeView();
  }

  public void openWelcomeView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("WelcomeView.fxml"));
      Region root = loader.load();
      WelcomeViewController controller = loader.getController();
      controller.init(this, viewModelFactory.getWelcomeViewModel(), root);

      primaryStage.setTitle("Welcome");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void openStudentMainPageView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          getClass().getResource("student/StudentMainPageView.fxml"));
      Region root = loader.load();
      StudentMainPageViewController controller = loader.getController();
      controller.init(this, viewModelFactory.getStudentMainPageViewModel(),
          root);

      primaryStage.setTitle("Student Main Page");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void openStudentSessionView(Session selected)
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("student/SessionStudentView.fxml"));
      Region root = loader.load();
      SessionStudentViewController controller = loader.getController();
      viewModelFactory.getSessionStudentViewModel().loadSession(selected);
      controller.init(this, viewModelFactory.getSessionStudentViewModel(), root);

      primaryStage.setTitle("Session");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void openTutorSessionView(Session selected)
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("tutor/SessionTutorView.fxml"));
      Region root = loader.load();
      SessionTutorViewController controller = loader.getController();
      controller.init(this, viewModelFactory.getSessionTutorViewModel(), root);
      viewModelFactory.getSessionTutorViewModel().loadSession(selected);

      primaryStage.setTitle("Session");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();
    }
    catch (Exception e)
    {
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
      controller.init(popupStage, viewModelFactory.getTutorProfileViewModel(),
          root);
      popupStage.setTitle("Tutor Profile");
      popupStage.setScene(new Scene(root));
      popupStage.setResizable(false);
      popupStage.initOwner(primaryStage);
      popupStage.show();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void openTutorMainPageView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          getClass().getResource("tutor/TutorMainPage.fxml"));
      Region root = loader.load();
      TutorMainPageController controller = loader.getController();
      controller.init(this, viewModelFactory.getTutorMainPageViewModel(), root);

      primaryStage.setTitle("Tutor Main Page");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void openCreateNewSession()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          getClass().getResource("tutor/CreateNewSession.fxml"));
      Region root = loader.load();
      CreateNewSessionController controller = loader.getController();

      Stage popupStage = new Stage();
      controller.init(popupStage,this, viewModelFactory.getCreateNewSessionViewModel(), root);

      popupStage.setTitle("Create New Session");
      popupStage.setScene(new Scene(root));
      popupStage.setResizable(false);
      popupStage.initOwner(primaryStage);
      popupStage.show();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void openAdminView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("AdminView.fxml"));
      Region root = loader.load();
      AdminViewController controller = loader.getController();
      controller.init(this, viewModelFactory.getAdminViewModel(), root);

      primaryStage.setTitle("Admin Panel");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void openCreateNewProfile()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource(
          "client/view/CreateNewProfile.fxml"));
      Region root = loader.load();
      CreateNewProfileController controller = loader.getController();
      controller.init(this, viewModelFactory.getCreateNewProfileViewModel(), root);

      primaryStage.setTitle("Create Profile View");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void openRules()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource(
          "client/view/RulesView.fxml"));
      Region root = loader.load();
      RulesViewController controller = loader.getController();
      controller.init(this, viewModelFactory.getRulesViewModel(), root);

      primaryStage.setTitle("Rules View");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void openCreateStudentProfile()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("client/view/student/CreateStudentProfile.fxml"));
      Region root = loader.load();
      CreateStudentProfileController controller = loader .getController();
      controller.init(this, viewModelFactory.getCreateStudentViewModel(),root);

      primaryStage.setTitle("Create Student Profile View");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void openCreateTutorProfile()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("client/view/tutor/CreateTutorProfile.fxml"));
      Region root = loader.load();
      CreateTutorProfileController controller = loader .getController();
      controller.init(this, viewModelFactory.getCreateTutorViewModel(),root);

      primaryStage.setTitle("Create Tutor Profile View");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }


}

