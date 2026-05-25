package client.viewmodel;

import client.model.Model;

public class WelcomeViewModel {
    private final Model model;

    public WelcomeViewModel(Model model) {
        this.model = model;
    }

    public String login(String email, String password) {
        try {
            return model.login(email, password);
        } catch (Exception e) {
            return null;
        }
    }
}
