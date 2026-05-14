package client.viewmodel;

import client.model.Model;
import client.viewmodel.student.SessionViewModel;
import client.viewmodel.student.StudentMainPageViewModel;
import client.viewmodel.student.TutorProfileViewModel;

public class ViewModelFactory {
    private SessionViewModel sessionViewModel;
    private TutorProfileViewModel tutorProfileViewModel;
    private StudentMainPageViewModel studentMainPageViewModel;
    private WelcomeViewModel welcomeViewModel;
    private TutorMainPageViewModel tutorMainPageViewModel;

    public ViewModelFactory(Model model) {
        try {
            model.connect();
        } catch (Exception e) {
            System.out.println("Could not connect to server: " + e.getMessage());
        }
        sessionViewModel = new SessionViewModel(model);
        tutorProfileViewModel = new TutorProfileViewModel(model);
        studentMainPageViewModel = new StudentMainPageViewModel(model);
        welcomeViewModel = new WelcomeViewModel(model);
        tutorMainPageViewModel = new TutorMainPageViewModel(model);
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
}
