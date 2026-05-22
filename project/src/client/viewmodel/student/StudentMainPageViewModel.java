package client.viewmodel.student;

import client.model.StudentModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import server.model.Session;
import server.model.Student;

public class StudentMainPageViewModel implements PropertyChangeListener {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final StudentModel model;
    private final ObservableList<Session> sessions;
    private final FilteredList<Session> filteredSessions;
    private final ObservableList<Session> bookedSessions;
    private final ObservableList<String> subjectOptions;

    private final SessionFilterService filterService;

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty dateOfBirth;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty gender;
    private final StringProperty languages;

    public StudentMainPageViewModel(StudentModel model) {
        this.model = model;
        this.sessions = FXCollections.observableArrayList();
        this.filteredSessions = new FilteredList<>(sessions, s -> true);
        this.bookedSessions = FXCollections.observableArrayList();
        this.subjectOptions = FXCollections.observableArrayList();

        filterService = new SessionFilterService(filteredSessions);

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
        loadSubjects();
        loadSessions();
        loadBookedSessions();
        Student student = model.getLoggedInStudent();
        if (student != null) loadProfile(student);
    }

    public void loadSubjects() {
        subjectOptions.clear();
        subjectOptions.add("All");
        subjectOptions.addAll(model.getSubjects());
    }

    public void loadSessions() {
        sessions.setAll(model.getAllSessions());
    }

    public void loadBookedSessions() {
        bookedSessions.setAll(model.getBookedSessions());
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

    public boolean saveProfile() {
        Student current = model.getLoggedInStudent();
        if (current == null) return false;

        java.time.LocalDate date;
        try {
            date = java.time.LocalDate.parse(dateOfBirth.get());
        } catch (Exception e) {
            return false;
        }

        ArrayList<String> langList = new ArrayList<>(
            Arrays.asList(languages.get().split("\\s*,\\s*")));

        Student updated = new Student(
            firstName.get().trim(), lastName.get().trim(), date,
            email.get().trim(), phone.get().trim(),
            current.getPassword(), gender.get().trim(),
            langList, current.getId(), "Student");

        return model.editStudentProfile(updated);
    }

    public void resetFilters() {
        filterService.resetFilters();
    }

    public FilteredList<Session> getFilteredSessions() {
        return filteredSessions;
    }

    public ObservableList<Session> getSessions() {
        return sessions;
    }

    public ObservableList<Session> getBookedSessions() {
        return bookedSessions;
    }

    public ObservableList<String> getSubjectOptions() {
        return subjectOptions;
    }

    public String formatPrice(double price) { return String.format("%.2f", price); }
    public String formatDate(java.time.LocalDateTime date) { return date.format(DATE_FMT); }
    public String formatTutorName(server.model.Tutor tutor) { return tutor.getFirst_name() + " " + tutor.getLast_name(); }
    public String formatSessionState(Session session) { return session.getState() != null ? session.getState().getClass().getSimpleName() : ""; }

    public StringProperty subjectFilterProperty() { return filterService.subjectFilterProperty(); }
    public StringProperty minPriceProperty() { return filterService.minPriceProperty(); }
    public StringProperty maxPriceProperty() { return filterService.maxPriceProperty(); }
    public StringProperty fromDateProperty() { return filterService.fromDateProperty(); }
    public StringProperty toDateProperty() { return filterService.toDateProperty(); }

    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty dateOfBirthProperty() { return dateOfBirth; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty languagesProperty() { return languages; }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("SessionBooked".equals(evt.getPropertyName())) {
            Session session = (Session) evt.getNewValue();
            Platform.runLater(() -> {
                sessions.remove(session);
                bookedSessions.add(session);
            });
        }
    }
}
