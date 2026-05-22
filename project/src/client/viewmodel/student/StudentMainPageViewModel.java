package client.viewmodel.student;

import client.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    private final Model model;
    private final ObservableList<Session> sessions;
    private final FilteredList<Session> filteredSessions;
    private final ObservableList<Session> bookedSessions;
    private final ObservableList<String> subjectOptions;

    private final StringProperty subjectFilter;
    private final StringProperty minPrice;
    private final StringProperty maxPrice;
    private final StringProperty fromDate;
    private final StringProperty toDate;

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
        this.filteredSessions = new FilteredList<>(sessions, s -> true);
        this.bookedSessions = FXCollections.observableArrayList();
        this.subjectOptions = FXCollections.observableArrayList();

        subjectFilter = new SimpleStringProperty("All");
        minPrice = new SimpleStringProperty("");
        maxPrice = new SimpleStringProperty("");
        fromDate = new SimpleStringProperty("");
        toDate = new SimpleStringProperty("");

        firstName = new SimpleStringProperty("");
        lastName = new SimpleStringProperty("");
        dateOfBirth = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        phone = new SimpleStringProperty("");
        gender = new SimpleStringProperty("");
        languages = new SimpleStringProperty("");

        subjectFilter.addListener((obs, o, n) -> applyFilter());
        minPrice.addListener((obs, o, n) -> applyFilter());
        maxPrice.addListener((obs, o, n) -> applyFilter());
        fromDate.addListener((obs, o, n) -> applyFilter());
        toDate.addListener((obs, o, n) -> applyFilter());

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

        LocalDate date;
        try {
            date = LocalDate.parse(dateOfBirth.get());
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
        subjectFilter.set("All");
        minPrice.set("");
        maxPrice.set("");
        fromDate.set("");
        toDate.set("");
    }

    private void applyFilter() {
        filteredSessions.setPredicate(session -> {
            // Subject filter
            String subject = subjectFilter.get();
            if (subject != null && !subject.equals("All") && !subject.isBlank()) {
                if (!subject.equals(session.getSubject())) return false;
            }

            // Min price filter
            String minP = minPrice.get();
            if (minP != null && !minP.isBlank()) {
                try {
                    if (session.getPrice() < Double.parseDouble(minP)) return false;
                } catch (NumberFormatException ignored) {}
            }

            // Max price filter
            String maxP = maxPrice.get();
            if (maxP != null && !maxP.isBlank()) {
                try {
                    if (session.getPrice() > Double.parseDouble(maxP)) return false;
                } catch (NumberFormatException ignored) {}
            }

            // From date filter
            String from = fromDate.get();
            if (from != null && !from.isBlank()) {
                try {
                    LocalDate fd = LocalDate.parse(from, DATE_FMT);
                    if (session.getStart_date().toLocalDate().isBefore(fd)) return false;
                } catch (DateTimeParseException ignored) {}
            }

            // To date filter
            String to = toDate.get();
            if (to != null && !to.isBlank()) {
                try {
                    LocalDate td = LocalDate.parse(to, DATE_FMT);
                    if (session.getStart_date().toLocalDate().isAfter(td)) return false;
                } catch (DateTimeParseException ignored) {}
            }

            return true;
        });
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

    public StringProperty subjectFilterProperty() { return subjectFilter; }
    public StringProperty minPriceProperty() { return minPrice; }
    public StringProperty maxPriceProperty() { return maxPrice; }
    public StringProperty fromDateProperty() { return fromDate; }
    public StringProperty toDateProperty() { return toDate; }

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
