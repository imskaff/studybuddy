package server.model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Abstract base class representing a user in the system.
 * 
 * Holds the common personal information shared by all user types
 * ({@link Student} and {@link Tutor}).
 * 
 *
 * @author
 */
public abstract class User {
    private int id;
    private String first_name;
    private String last_name;
    private LocalDate date_of_birth;
    private String email;
    private String phone;
    private String password;
    private String gender;
    private ArrayList<String> languages;
    private String type;

    /**
     * Constructs a new {@code User} with all personal details.
     *
     * @param id            the unique database identifier
     * @param first_name    the user's first name
     * @param last_name     the user's last name
     * @param date_of_birth the user's date of birth
     * @param email         the user's email address
     * @param phone         the user's phone number
     * @param password      the user's password
     * @param gender        the user's gender
     * @param languages     the list of languages the user speaks
     * @param type          the user type string (e.g. {@code "student"} or {@code "tutor"})
     */
    public User(int id, String first_name, String last_name, LocalDate date_of_birth, String email, String phone,
            String password, String gender, ArrayList<String> languages, String type) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.languages = languages;
        this.type = type;
    }

    /**
     * Returns the unique database identifier of this user.
     *
     * @return the user's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the user's first name.
     *
     * @return the first name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Sets the user's first name.
     *
     * @param first_name the new first name
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Returns the user's last name.
     *
     * @return the last name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Sets the user's last name.
     *
     * @param last_name the new last name
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * Returns the user's date of birth.
     *
     * @return the date of birth
     */
    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    /**
     * Sets the user's date of birth.
     *
     * @param date_of_birth the new date of birth
     */
    public void setDateOfBirth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    /**
     * Returns the user's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the user's phone number.
     *
     * @param phone the new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the user's gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the user's gender.
     *
     * @param gender the new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns the user's password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the list of languages spoken by the user.
     *
     * @return the list of languages
     */
    public ArrayList<String> getLanguages() {
        return languages;
    }

    /**
     * Sets the list of languages spoken by the user.
     *
     * @param languages the new list of languages
     */
    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    /**
     * Returns the user type string (e.g. {@code "student"} or {@code "tutor"}).
     *
     * @return the user type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the user type string.
     *
     * @param type the new user type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns a string representation of this user for debugging purposes.
     *
     * @return a formatted string with all user fields
     */
    @Override public String toString()
    {
        return "User{" + "id=" + id + ", first_name='" + first_name + '\''
            + ", last_name='" + last_name + '\'' + ", date_of_birth="
            + date_of_birth + ", email='" + email + '\'' + ", phone='" + phone
            + '\'' + ", password='" + password + '\'' + ", gender='" + gender + '\''
            + ", languages=" + languages + ", type='" + type + '\'' + '}';
    }
}
