package client.viewmodel.student;

import client.model.Model;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.model.Tutor;

/**
 * ViewModel for the tutor public-profile screen (student view).
 * 
 * Holds display-only properties populated from a {@link Tutor} object.
 * The student uses this screen to view a tutor's details before or after
 * booking a session.
 * 
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class TutorProfileViewModel {
    private Model model;

    private final StringProperty firstName     = new SimpleStringProperty("");
    private final StringProperty lastName      = new SimpleStringProperty("");
    private final StringProperty email         = new SimpleStringProperty("");
    private final StringProperty phone         = new SimpleStringProperty("");
    private final StringProperty gender        = new SimpleStringProperty("");
    private final StringProperty dateOfBirth   = new SimpleStringProperty("");
    private final StringProperty educationLevel = new SimpleStringProperty("");
    private final StringProperty languages     = new SimpleStringProperty("");

    /**
     * Constructs a {@code TutorProfileViewModel} with the given model.
     *
     * @param model the client-side model
     */
    public TutorProfileViewModel(Model model) {
        this.model = model;
    }

    /**
     * Populates all display properties from the given tutor.
     * {@code null} field values are replaced with empty strings.
     * The date of birth is formatted as {@code dd/MM/yyyy}.
     * Languages are joined with {@code ", "}.
     *
     * @param tutor the tutor to display; if {@code null} the method returns immediately
     */
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

    /** @return the first-name property */
    public StringProperty firstNameProperty()      { return firstName;      }
    /** @return the last-name property */
    public StringProperty lastNameProperty()       { return lastName;       }
    /** @return the email property */
    public StringProperty emailProperty()          { return email;          }
    /** @return the phone-number property */
    public StringProperty phoneProperty()          { return phone;          }
    /** @return the gender property */
    public StringProperty genderProperty()         { return gender;         }
    /** @return the date-of-birth property (formatted as {@code dd/MM/yyyy}) */
    public StringProperty dateOfBirthProperty()    { return dateOfBirth;    }
    /** @return the education-level property */
    public StringProperty educationLevelProperty() { return educationLevel; }
    /** @return the languages property (comma-separated string) */
    public StringProperty languagesProperty()      { return languages;      }
}
