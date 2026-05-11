package client.viewmodel;

import client.model.Model;

public class ViewModelFactory {
    private SessionViewModel sessionViewModel;
    private TutorMainPageViewModel tutorMainPageViewModel;
    private StudentMainPageViewModel studentMainPageViewModel;
    private WelcomeViewModel welcomeViewModel;

    public ViewModelFactory(Model model) {
        try {
            model.connect();
        } catch (Exception e) {
            System.out.println("Could not connect to server: " + e.getMessage());
        }
        sessionViewModel = new SessionViewModel(model);
        tutorMainPageViewModel = new TutorMainPageViewModel(model);
        studentMainPageViewModel = new StudentMainPageViewModel(model);
        welcomeViewModel = new WelcomeViewModel(model);
    }

    public WelcomeViewModel getWelcomeViewModel() {
        return welcomeViewModel;
    }

    public SessionViewModel getSessionViewModel() {
        return sessionViewModel;
    }

    public TutorMainPageViewModel getTutorMainPageViewModel() {
        return tutorMainPageViewModel;
    }

    public StudentMainPageViewModel getStudentMainPageViewModel() {
        return studentMainPageViewModel;
    }   
}
