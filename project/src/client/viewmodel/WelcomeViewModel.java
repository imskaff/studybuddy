package client.viewmodel;

import client.model.Model;
import server.dao.UserDAOImpl;
import server.model.Student;
import server.model.Tutor;
import server.model.User;

public class WelcomeViewModel {
    private final Model model;

    public WelcomeViewModel(Model model) {
        this.model = model;
    }

    public String login(String email, String password) {
        try {
            User user = UserDAOImpl.getInstance().readByEmailAndPassword(email, password);
            if (user instanceof Student student) {
                model.setLoggedInStudent(student);
                return "student";
            }
            if (user instanceof Tutor) {
                return "tutor";
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
