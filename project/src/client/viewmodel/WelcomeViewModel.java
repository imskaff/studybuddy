package client.viewmodel;

import client.model.Model;

/**
 * ViewModel for the welcome / login screen.
 * 
 * Delegates login to the model and returns the user-type string so the
 * view can navigate to the correct main page.
 * 
 *
 * @author Adam Terelak
 */
public class WelcomeViewModel {
    private final Model model;

    /**
     * Constructs a {@code WelcomeViewModel} with the given model.
     *
     * @param model the client-side model
     */
    public WelcomeViewModel(Model model) {
        this.model = model;
    }

    /**
     * Attempts to log in with the given credentials.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return the user type string ({@code "student"}, {@code "tutor"}, {@code "admin"}),
     *         or {@code null} if login failed
     */
    public String login(String email, String password) {
        try {
            return model.login(email, password);
        } catch (Exception e) {
            return null;
        }
    }
}
