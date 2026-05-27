package client.viewmodel;

import client.model.Model;
import client.viewmodel.admin.AdminViewModel;
import client.viewmodel.student.*;
import client.viewmodel.tutor.*;

/**
 * Factory that constructs and holds all ViewModel instances for the application.
 * 
 * Created once at startup and passed to the {@link client.view.ViewHandler}.
 * Each view controller obtains its ViewModel through the corresponding getter.
 * 
 *
 * @author
 */
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
    private CreateNewProfileViewModel createNewProfileViewModel;
    private RulesViewModel rulesViewModel;
    private CreateStudentProfileViewModel createStudentProfileViewModel;
    private EditStudentProfileViewModel editStudentProfileViewModel;

    private ViewState viewState;

    /**
     * Constructs the factory and eagerly instantiates all ViewModels.
     *
     * @param model the client-side model injected into every ViewModel
     */
    public ViewModelFactory(Model model) {
        this.viewState = new ViewState();
        sessionStudentViewModel = new SessionStudentViewModel(model);
        sessionTutorViewModel = new SessionTutorViewModel(model);
        tutorProfileViewModel = new TutorProfileViewModel(model);
        studentMainPageViewModel = new StudentMainPageViewModel(model);
        welcomeViewModel = new WelcomeViewModel(model);
        tutorMainPageViewModel = new TutorMainPageViewModel(model, viewState);
        createStudentViewModel = new CreateStudentProfileViewModel(model);
        createTutorViewModel = new CreateTutorProfileViewModel(model);
        adminViewModel = new AdminViewModel(model);
        createNewSessionViewModel = new CreateNewSessionViewModel(model);
        createNewProfileViewModel = new CreateNewProfileViewModel(model);
        rulesViewModel = new RulesViewModel(model);
        createStudentProfileViewModel = new CreateStudentProfileViewModel(model);
        editStudentProfileViewModel = new EditStudentProfileViewModel(model);
    }

    /**
     * Returns the {@link WelcomeViewModel} for the login screen.
     *
     * @return the welcome ViewModel
     */
    public WelcomeViewModel getWelcomeViewModel() {
        return welcomeViewModel;
    }

    /**
     * Returns the {@link SessionStudentViewModel} for the session detail screen (student).
     *
     * @return the session-student ViewModel
     */
    public SessionStudentViewModel getSessionStudentViewModel() {
        return sessionStudentViewModel;
    }

    /**
     * Returns the {@link SessionTutorViewModel} for the session detail screen (tutor).
     *
     * @return the session-tutor ViewModel
     */
    public SessionTutorViewModel getSessionTutorViewModel() {
        return sessionTutorViewModel;
    }

    /**
     * Returns the {@link TutorProfileViewModel} for viewing a tutor's public profile.
     *
     * @return the tutor-profile ViewModel
     */
    public TutorProfileViewModel getTutorProfileViewModel() {
        return tutorProfileViewModel;
    }

    /**
     * Returns the {@link StudentMainPageViewModel} for the student main page.
     *
     * @return the student main-page ViewModel
     */
    public StudentMainPageViewModel getStudentMainPageViewModel() {
        return studentMainPageViewModel;
    }

    /**
     * Returns the {@link TutorMainPageViewModel} for the tutor main page.
     *
     * @return the tutor main-page ViewModel
     */
    public TutorMainPageViewModel getTutorMainPageViewModel() {
        return tutorMainPageViewModel;
    }

    /**
     * Returns the {@link CreateNewSessionViewModel} for the create-session form.
     *
     * @return the create-session ViewModel
     */
    public CreateNewSessionViewModel getCreateNewSessionViewModel() {
        return createNewSessionViewModel;
    }

    /**
     * Returns the {@link CreateStudentProfileViewModel} (first instance).
     *
     * @return the create-student-profile ViewModel
     */
    public CreateStudentProfileViewModel getCreateStudentViewModel() {
        return createStudentViewModel;
    }

    /**
     * Returns the {@link CreateTutorProfileViewModel} for the tutor registration form.
     *
     * @return the create-tutor-profile ViewModel
     */
    public CreateTutorProfileViewModel getCreateTutorViewModel() {
        return createTutorViewModel;
    }

    /**
     * Returns the {@link AdminViewModel} for the administrator panel.
     *
     * @return the admin ViewModel
     */
    public AdminViewModel getAdminViewModel() {
        return adminViewModel;
    }

    /**
     * Returns the {@link CreateNewProfileViewModel} for the profile-type selection screen.
     *
     * @return the create-new-profile ViewModel
     */
    public CreateNewProfileViewModel getCreateNewProfileViewModel()
    {
        return createNewProfileViewModel;
    }

    /**
     * Returns the {@link RulesViewModel} for the rules acceptance screen.
     *
     * @return the rules ViewModel
     */
    public RulesViewModel getRulesViewModel()
    {
        return rulesViewModel;
    }

    /**
     * Returns the second {@link CreateStudentProfileViewModel} instance used
     * by the dedicated student registration view.
     *
     * @return the create-student-profile ViewModel
     */
    public CreateStudentProfileViewModel getCreateStudentProfileViewModel()
    {
        return createStudentProfileViewModel;
    }

    /**
     * Returns the {@link EditStudentProfileViewModel} for the edit-profile screen.
     *
     * @return the edit-student-profile ViewModel
     */
    public EditStudentProfileViewModel getEditStudentProfileViewModel()
    {
        return editStudentProfileViewModel;
    }
}
