package client.view;

import client.view.admin.AdminViewController;
import client.view.student.*;
import client.view.tutor.*;
import client.viewmodel.ViewModelFactory;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import server.model.Session;
import server.model.Tutor;

/**
 * Central navigation controller for the JavaFX application.
 * 
 * Loads FXML files, obtains the matching view controller, injects the
 * corresponding ViewModel from the {@link ViewModelFactory}, and sets the
 * resulting {@link Scene} on the primary {@link Stage}. Each {@code open*}
 * method corresponds to one application screen. Popup screens are shown on
 * a new {@link Stage} owned by the primary stage.
 * 
 *
 * @author
 */
public class ViewHandler
{
  private ViewModelFactory viewModelFactory;
  private Stage primaryStage;

  /**
   * Constructs a {@code ViewHandler} with the given ViewModel factory.
   *
   * @param viewModelFactory the factory providing all ViewModels
   */
  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
  }

  /**
   * No-arg constructor used when {@code ViewHandler} is instantiated without
   * a factory (e.g. from certain popup controllers that only need navigation).
   */
  public ViewHandler()
  {
  }

  /**
   * Starts the application by showing the welcome screen on the given stage.
   *
   * @param primaryStage the application's primary stage
   */
  public void start(Stage primaryStage)
  {
    this.primaryStage = primaryStage;
    openWelcomeView();
  }

  /**
   * Opens the login / welcome screen on the primary stage.
   */
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

  /**
   * Opens the student main page (available sessions, booked sessions, profile)
   * on the primary stage.
   */
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

  /**
   * Opens the session detail screen for a student with the given session
   * pre-loaded into the ViewModel.
   *
   * @param selected the session to display
   */
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

  /**
   * Opens the session detail / edit screen for a tutor with the given session
   * pre-loaded into the ViewModel.
   *
   * @param selected the session to display
   */
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

  /**
   * Opens the tutor public-profile popup for the given tutor. The popup is
   * owned by the primary stage and is non-resizable.
   *
   * @param tutor the tutor whose profile should be displayed
   */
  public void openTutorProfileView(Tutor tutor) {
      try {
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("student/TutorProfileView.fxml"));
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

  /**
   * Opens the tutor main page (sessions list, profile) on the primary stage.
   */
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

  /**
   * Opens the create-new-session popup. The popup blocks until closed
   * ({@code showAndWait}).
   */
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
      popupStage.showAndWait();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Opens the administrator panel on the primary stage.
   */
  public void openAdminView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("admin/AdminView.fxml"));
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

  /**
   * Opens the profile-type selection screen (student vs. tutor) on the
   * primary stage.
   */
  public void openCreateNewProfile()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("CreateNewProfile.fxml"));
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

  /**
   * Opens the platform-rules acceptance screen on the primary stage.
   */
  public void openRules()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("RulesView.fxml"));
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

  /**
   * Opens the student registration form on the primary stage.
   */
  public void openCreateStudentProfile()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("student/CreateStudentProfile.fxml"));
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

  /**
   * Opens the tutor registration form on the primary stage.
   */
  public void openCreateTutorProfile()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("tutor/CreateTutorProfile.fxml"));
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

  /**
   * Opens the edit-student-profile popup. The popup is owned by the primary
   * stage and is non-resizable.
   */
  public void openEditStudentProfileDetails()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("client/view/student/EditStudentProfile.fxml"));
      Region root = loader.load();
      EditStudentProfileController controller = loader.getController();
      Stage popupStage = new Stage();
      controller.init(popupStage, viewModelFactory.getEditStudentProfileViewModel(), root);

      popupStage.setTitle("Edit Student Profile Details");
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

  /**
   * Opens the read-only student profile details popup. The popup is owned
   * by the primary stage and is non-resizable.
   */
  public void openStudentProfileDetails()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("student/StudentProfileDetails.fxml"));
      Region root = loader.load();
      StudentProfileDetailsController controller = loader.getController();

      Stage popupStage = new Stage();
      controller.init(popupStage, viewModelFactory.getEditStudentProfileViewModel(), root);

      popupStage.setTitle("Student Profile Details");
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

  /**
   * Opens the tutor profile details / edit popup. The popup is owned by the
   * primary stage and is non-resizable.
   */
  public void openTutorProfileDetails()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
          getClass().getResource("tutor/TutorProfileDetails.fxml"));
      Region root = loader.load();
      TutorProfileDetailsController controller = loader.getController();

      Stage popupStage = new Stage();
      controller.init(popupStage,viewModelFactory.getTutorMainPageViewModel(), root);

      popupStage.setTitle("Tutor Profile Details");
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
}
