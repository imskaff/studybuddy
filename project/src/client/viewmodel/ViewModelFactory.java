package client.viewmodel;

import client.model.Model;
import client.viewmodel.student.SessionViewModel;
import client.viewmodel.student.StudentMainPageViewModel;
import client.viewmodel.student.TutorProfileViewModel;
import client.viewmodel.tutor.CreateNewSessionViewModel;
import client.viewmodel.tutor.TutorMainPageViewModel;

public class ViewModelFactory
{
    private SessionViewModel sessionViewModel;
    private TutorProfileViewModel tutorProfileViewModel;
    private StudentMainPageViewModel studentMainPageViewModel;
    private WelcomeViewModel welcomeViewModel;
    private TutorMainPageViewModel tutorMainPageViewModel;
    private CreateNewSessionViewModel createNewSessionViewModel;
    private CreateStudentProfileViewModel createStudentViewModel;
    private CreateTutorProfileViewModel createTutorViewModel;
    private AdminViewModel adminViewModel;

    private ViewState viewState;

    public ViewModelFactory(Model model) {
        this.viewState = new ViewState();
        sessionViewModel = new SessionViewModel(model);
        tutorProfileViewModel = new TutorProfileViewModel(model);
        studentMainPageViewModel = new StudentMainPageViewModel(model);
        welcomeViewModel = new WelcomeViewModel(model);
        tutorMainPageViewModel = new TutorMainPageViewModel(model, viewState);
//        createNewSessionViewModel = new CreateNewSessionViewModel(model, viewState);
        createStudentViewModel = new CreateStudentProfileViewModel(model);
        createTutorViewModel = new CreateTutorProfileViewModel(model);
        adminViewModel = new AdminViewModel(model);
        createNewSessionViewModel = new CreateNewSessionViewModel(model);
    }

    public WelcomeViewModel getWelcomeViewModel() {
        return welcomeViewModel;
    }

    public SessionViewModel getSessionViewModel() {
        return sessionViewModel;
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
}