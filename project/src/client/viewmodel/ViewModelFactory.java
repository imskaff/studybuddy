package client.viewmodel;

import client.model.Model;
import client.viewmodel.admin.AdminViewModel;
import client.viewmodel.student.CreateStudentProfileViewModel;
import client.viewmodel.student.SessionStudentViewModel;
import client.viewmodel.student.StudentMainPageViewModel;
import client.viewmodel.student.TutorProfileViewModel;
import client.viewmodel.tutor.CreateNewSessionViewModel;
import client.viewmodel.tutor.CreateTutorProfileViewModel;
import client.viewmodel.tutor.SessionTutorViewModel;
import client.viewmodel.tutor.TutorMainPageViewModel;

public class ViewModelFactory
{
    private SessionStudentViewModel sessionStudentViewModel;
    private SessionTutorViewModel sessionTutorViewModel;
    private TutorProfileViewModel tutorProfileViewModel;
    private StudentMainPageViewModel studentMainPageViewModel;
    private WelcomeViewModel welcomeViewModel;
    private TutorMainPageViewModel tutorMainPageViewModel;
    private CreateNewSessionViewModel createNewSessionViewModel;
    private CreateStudentProfileViewModel createStudentViewModel;
    private CreateTutorProfileViewModel createTutorViewModel;
    private AdminViewModel adminViewModel;
    private CreateProfileViewModel createProfileViewModel;

    private ViewState viewState;

    public ViewModelFactory(Model model) {
        this.viewState = new ViewState();
        sessionStudentViewModel = new SessionStudentViewModel(model);
        sessionTutorViewModel = new SessionTutorViewModel(model);
        tutorProfileViewModel = new TutorProfileViewModel(model);
        studentMainPageViewModel = new StudentMainPageViewModel(model);
        welcomeViewModel = new WelcomeViewModel(model);
        tutorMainPageViewModel = new TutorMainPageViewModel(model, viewState);
//        createNewSessionViewModel = new CreateNewSessionViewModel(model, viewState);
        createStudentViewModel = new CreateStudentProfileViewModel(model);
        createTutorViewModel = new CreateTutorProfileViewModel(model);
        adminViewModel = new AdminViewModel(model);
        createNewSessionViewModel = new CreateNewSessionViewModel(model);
        createProfileViewModel = new CreateProfileViewModel();

    }

    public WelcomeViewModel getWelcomeViewModel() {
        return welcomeViewModel;
    }

    public SessionStudentViewModel getSessionStudentViewModel() {
        return sessionStudentViewModel;
    }

    public SessionTutorViewModel getSessionTutorViewModel() {
        return sessionTutorViewModel;
    }

    public TutorProfileViewModel getTutorProfileViewModel() {
        return tutorProfileViewModel;
    }

    public StudentMainPageViewModel getStudentMainPageViewModel() {
        return studentMainPageViewModel;
    }

    public TutorMainPageViewModel getTutorMainPageViewModel() {
        return tutorMainPageViewModel;
    }

    public CreateNewSessionViewModel getCreateNewSessionViewModel() {
        return createNewSessionViewModel;
    }
    public CreateStudentProfileViewModel getCreateStudentViewModel() {
        return createStudentViewModel;
    }

    public CreateTutorProfileViewModel getCreateTutorViewModel() {
        return createTutorViewModel;
    }

    public AdminViewModel getAdminViewModel() {
        return adminViewModel;
    }

  public CreateProfileViewModel getCreateProfileViewModel()
  {
    return createProfileViewModel;
  }
}