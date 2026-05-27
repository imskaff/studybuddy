package client.viewmodel.student;

import client.model.Model;
import client.viewmodel.CreateProfileViewModel;
import client.viewmodel.UserType;
import javafx.beans.property.StringProperty;
import server.model.Student;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * ViewModel for the student registration form.
 * Extends {@link CreateProfileViewModel} with student-specific registration logic.
 *
 * @author Kamila Jeremicz
 */
public class CreateStudentProfileViewModel extends CreateProfileViewModel
{
    /**
     * Constructs the ViewModel for the given model.
     *
     * @param model the client-side model
     */
    public CreateStudentProfileViewModel(Model model)
    {
        super(model, UserType.STUDENT);
    }

    /**
     * Validates all required fields and registers the student via the model.
     *
     * @return {@code true} if registration succeeded, {@code false} if any field
     *         is blank or the server rejected the request
     */
    @Override
    public boolean register()
    {
        String dob = dateOfBirthProperty().get();
        if (firstNameProperty().get().isBlank() || lastNameProperty().get().isBlank()
            || dob.isBlank() || emailProperty().get().isBlank()
            || phoneNoProperty().get().isBlank() || passwordProperty().get().isBlank()
            || genderProperty().get().isBlank() || languageInputProperty().get().isBlank()) {
            return false;
        }
        try {
            ArrayList<String> langs = new ArrayList<>();
            for (String l : languageInputProperty().get().split(",")) {
                if (!l.isBlank()) langs.add(l.trim());
            }
            Student student = new Student(
                firstNameProperty().get().trim(),
                lastNameProperty().get().trim(),
                LocalDate.parse(dob.trim()),
                emailProperty().get().trim(),
                phoneNoProperty().get().trim(),
                passwordProperty().get(),
                genderProperty().get().trim(),
                langs,
                0,
                UserType.STUDENT.toString()
            );
            return model.registerStudent(student);
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns the first-name property. */
    @Override public StringProperty firstNameProperty() { return super.firstNameProperty(); }

    /** Returns the last-name property. */
    @Override public StringProperty lastNameProperty() { return super.lastNameProperty(); }

    /** Returns the date-of-birth property. */
    @Override public StringProperty dateOfBirthProperty() { return super.dateOfBirthProperty(); }

    /** Returns the email property. */
    @Override public StringProperty emailProperty() { return super.emailProperty(); }

    /** Returns the password property. */
    @Override public StringProperty passwordProperty() { return super.passwordProperty(); }

    /** Returns the gender property. */
    @Override public StringProperty genderProperty() { return super.genderProperty(); }

    /** Returns the phone-number property. */
    @Override public StringProperty phoneNoProperty() { return super.phoneNoProperty(); }

    /** Returns the language-input property. */
    @Override public StringProperty languageInputProperty() { return super.languageInputProperty(); }
}
