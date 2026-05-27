package client.viewmodel;

import client.model.Model;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Abstract base ViewModel for the student and tutor registration forms.
 * 
 * Holds the common JavaFX properties shared by both registration screens.
 * Subclasses ({@link client.viewmodel.student.CreateStudentProfileViewModel} and
 * {@link client.viewmodel.tutor.CreateTutorProfileViewModel}) implement
 * {@link #register()} with type-specific validation and model calls.
 * 
 * @author Kamila Jeremicz
 */
public abstract class CreateProfileViewModel
{
    /** The client-side model, accessible to subclasses. */
    protected Model model;

    private final StringProperty firstName   = new SimpleStringProperty("");
    private final StringProperty lastName    = new SimpleStringProperty("");
    private final StringProperty email       = new SimpleStringProperty("");
    private final StringProperty phoneNo     = new SimpleStringProperty("");
    private final StringProperty gender      = new SimpleStringProperty("");
    private final StringProperty password    = new SimpleStringProperty("");
    private final StringProperty dateOfBirth = new SimpleStringProperty("");
    private final ObjectProperty<UserType> userType = new SimpleObjectProperty<>();

    private final ObservableList<String> languages = FXCollections.observableArrayList();
    private final StringProperty languageInput = new SimpleStringProperty("");

    /**
     * Constructs a {@code CreateProfileViewModel} for the given user type.
     *
     * @param model the client-side model
     * @param type  the user type this form represents ({@link UserType#STUDENT} or {@link UserType#TUTOR})
     */
    public CreateProfileViewModel(Model model, UserType type) {
        this.model = model;
        this.userType.set(type);
    }

    /**
     * Validates the form fields and, if valid, sends the registration request to the server.
     *
     * @return {@code true} if registration succeeded; {@code false} if validation failed
     *         or the server rejected the request
     */
    public abstract boolean register();

    /**
     * Returns the first-name property.
     *
     * @return the first-name {@link StringProperty}
     */
    public StringProperty firstNameProperty() { return firstName; }

    /**
     * Returns the last-name property.
     *
     * @return the last-name {@link StringProperty}
     */
    public StringProperty lastNameProperty() { return lastName; }

    /**
     * Returns the email property.
     *
     * @return the email {@link StringProperty}
     */
    public StringProperty emailProperty() { return email; }

    /**
     * Returns the phone-number property.
     *
     * @return the phone-number {@link StringProperty}
     */
    public StringProperty phoneNoProperty() { return phoneNo; }

    /**
     * Returns the gender property.
     *
     * @return the gender {@link StringProperty}
     */
    public StringProperty genderProperty() { return gender; }

    /**
     * Returns the password property.
     *
     * @return the password {@link StringProperty}
     */
    public StringProperty passwordProperty() { return password; }

    /**
     * Returns the date-of-birth property (ISO-8601 string, e.g. {@code "2000-01-31"}).
     *
     * @return the date-of-birth {@link StringProperty}
     */
    public StringProperty dateOfBirthProperty() { return dateOfBirth; }

    /**
     * Returns the observable list of selected languages.
     *
     * @return the languages list
     */
    public ObservableList<String> getLanguages() { return languages; }

    /**
     * Returns the raw language-input property (comma-separated string).
     *
     * @return the language-input {@link StringProperty}
     */
    public StringProperty languageInputProperty()
    {
        return languageInput;
    }

    /**
     * Returns a read-only view of the user-type property.
     *
     * @return a read-only {@link ObjectProperty} holding the current {@link UserType}
     */
    public ReadOnlyObjectProperty<UserType> userTypeReadOnlyObjectProperty() { return userType; }
}
