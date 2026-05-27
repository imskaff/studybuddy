package client.viewmodel.student;

import client.model.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.model.Student;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * ViewModel for the edit-student-profile screen.
 * 
 * Loads the currently logged-in student's data into editable JavaFX properties
 * and provides a {@link #saveChanges()} method that validates and persists updates.
 * 
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class EditStudentProfileViewModel
{
    private Model model;

    private final StringProperty firstName   = new SimpleStringProperty("");
    private final StringProperty lastName    = new SimpleStringProperty("");
    private final StringProperty email       = new SimpleStringProperty("");
    private final StringProperty password    = new SimpleStringProperty("");
    private final StringProperty phone       = new SimpleStringProperty("");
    private final StringProperty gender      = new SimpleStringProperty("");
    private final StringProperty dateOfBirth = new SimpleStringProperty("");
    private final StringProperty languages   = new SimpleStringProperty("");

    /**
     * Constructs an {@code EditStudentProfileViewModel} with the given model.
     *
     * @param model the client-side model
     */
    public EditStudentProfileViewModel(Model model)
    {
        this.model = model;
    }

    /** @return the first-name property */
    public StringProperty firstNameProperty() { return firstName; }
    /** @return the last-name property */
    public StringProperty lastNameProperty()  { return lastName; }
    /** @return the email property */
    public StringProperty emailProperty()     { return email; }
    /** @return the password property */
    public StringProperty passwordProperty()  { return password; }
    /** @return the phone-number property */
    public StringProperty phoneProperty()     { return phone; }
    /** @return the gender property */
    public StringProperty genderProperty()    { return gender; }
    /** @return the date-of-birth property (ISO-8601 string) */
    public StringProperty dateOfBirthProperty() { return dateOfBirth; }
    /** @return the languages property (comma-separated string) */
    public StringProperty languagesProperty() { return languages; }

    /**
     * Populates all editable properties with the currently logged-in student's data.
     */
    public void loadStudentData()
    {
        Student currentStudent = model.getLoggedInStudent();

        if (currentStudent != null)
        {
            firstName.set(currentStudent.getFirst_name());
            lastName.set(currentStudent.getLast_name());
            email.set(currentStudent.getEmail());
            password.set(currentStudent.getPassword());
            phone.set(currentStudent.getPhone());
            gender.set(currentStudent.getGender());
            languages.set(currentStudent.getLanguages().toString());

            if (currentStudent.getDate_of_birth() != null)
            {
                dateOfBirth.set(currentStudent.getDate_of_birth().toString());
            }
        }
    }

    /**
     * Validates the current property values and, if valid, sends a profile update
     * to the server.
     *
     * @return {@code true} if the update succeeded; {@code false} if validation
     *         failed (e.g. invalid date format) or the server rejected the update
     */
    public boolean saveChanges()
    {
        try {
            Student student = model.getLoggedInStudent();

            if (student == null)
            {
                return false;
            }

            student.setFirst_name(firstName.get());
            student.setLast_name(lastName.get());
            student.setEmail(email.get());
            student.setPassword(password.get());
            student.setPhone(phone.get());
            student.setGender(gender.get());

            ArrayList<String> languagesList = new ArrayList<>(
                Arrays.asList(languages.get().split("\\s*,\\s*")));
            student.setLanguages(languagesList);

            try {
                student.setDateOfBirth(LocalDate.parse(dateOfBirth.get()));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Expected YYYY-MM-DD.");
                return false;
            }

            return model.editStudentProfile(student);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
