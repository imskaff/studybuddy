package client.viewmodel;

import client.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Student;

public class StudentMainPageViewModel implements PropertyChangeListener {
    private final Model model;
    private final ObservableList<Session> sessions;

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty dateOfBirth;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty gender;
    private final StringProperty languages;

    public StudentMainPageViewModel(Model model) {
        this.model = model;
        this.sessions = FXCollections.observableArrayList();

        firstName = new SimpleStringProperty("");
        lastName = new SimpleStringProperty("");
        dateOfBirth = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        phone = new SimpleStringProperty("");
        gender = new SimpleStringProperty("");
        languages = new SimpleStringProperty("");
        model.addPropertyChangeListener(this);
    }

    public void loadData() {
        loadSessions();
        Student student = model.getLoggedInStudent();
        if (student != null) loadProfile(student);
    }

    public void loadSessions() {
        sessions.setAll(model.getAllSessions());
    }

    public void loadProfile(Student student) {
        firstName.set(student.getFirst_name());
        lastName.set(student.getLast_name());
        dateOfBirth.set(student.getDate_of_birth().toString());
        email.set(student.getEmail());
        phone.set(student.getPhone());
        gender.set(student.getGender());
        languages.set(String.join(", ", student.getLanguages()));
    }

    public ObservableList<Session> getSessions() {
        return sessions;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public StringProperty languagesProperty() {
        return languages;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}