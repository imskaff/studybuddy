package client.viewmodel.tutor;

import client.model.Model;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.model.Tutor;

public class TutorProfileViewModel {
    private Model model;

    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty phone = new SimpleStringProperty("");
    private final StringProperty gender = new SimpleStringProperty("");
    private final StringProperty dateOfBirth = new SimpleStringProperty("");
    private final StringProperty educationLevel = new SimpleStringProperty("");
    private final StringProperty languages = new SimpleStringProperty("");

    public TutorProfileViewModel(Model model) {
        this.model = model;
    }

    public void loadTutor(Tutor tutor) {
        if (tutor == null) return;
        firstName.set(tutor.getFirst_name() != null ? tutor.getFirst_name() : "");
        lastName.set(tutor.getLast_name() != null ? tutor.getLast_name() : "");
        email.set(tutor.getEmail() != null ? tutor.getEmail() : "");
        phone.set(tutor.getPhone() != null ? tutor.getPhone() : "");
        gender.set(tutor.getGender() != null ? tutor.getGender() : "");
        dateOfBirth.set(tutor.getDate_of_birth() != null
            ? tutor.getDate_of_birth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            : "");
        educationLevel.set(tutor.getEducation_level() != null ? tutor.getEducation_level() : "");
        ArrayList<String> langs = tutor.getLanguages();
        languages.set(langs != null && !langs.isEmpty() ? String.join(", ", langs) : "");
    }

    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty dateOfBirthProperty() { return dateOfBirth; }
    public StringProperty educationLevelProperty() { return educationLevel; }
    public StringProperty languagesProperty() { return languages; }
}
