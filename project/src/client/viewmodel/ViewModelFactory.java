package client.viewmodel;

import client.model.Model;

public class ViewModelFactory {
    private SessionViewModel sessionViewModel;
    private BookingViewModel bookingViewModel;
    private TutorMainPageViewModel tutorMainPageViewModel;
    private StudentMainPageViewModel studentMainPageViewModel;
    private WelcomeViewModel welcomeViewModel;

    public ViewModelFactory(Model model) {
        sessionViewModel = new SessionViewModel(model);
        bookingViewModel = new BookingViewModel(model);
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

    public BookingViewModel getBookingViewModel() {
        return bookingViewModel;
    }

    public TutorMainPageViewModel getTutorMainPageViewModel() {
        return tutorMainPageViewModel;
    }

    public StudentMainPageViewModel getStudentMainPageViewModel() {
        return studentMainPageViewModel;
    }   
}
