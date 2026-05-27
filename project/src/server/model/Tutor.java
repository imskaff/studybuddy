package server.model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a tutor user in the system.
 * 
 * Extends {@link User} with an education level and an approval flag.
 * A tutor must be approved by an administrator before they can create
 * and offer tutoring sessions.
 * 
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class Tutor extends User {
    private String education_level;
    private boolean approved;

    /**
     * Constructs a new {@code Tutor} with all personal and professional details.
     * The tutor is initially not approved.
     *
     * @param first_name      the tutor's first name
     * @param last_name       the tutor's last name
     * @param date_of_birth   the tutor's date of birth
     * @param email           the tutor's email address
     * @param phone           the tutor's phone number
     * @param password        the tutor's password
     * @param gender          the tutor's gender
     * @param languages       the list of languages spoken by the tutor
     * @param education_level the tutor's highest education level
     * @param type            the user type string (should be {@code "Tutor"})
     * @param id              the unique database identifier
     */
    public Tutor(String first_name, String last_name, LocalDate date_of_birth, String email, String phone,
                 String password, String gender, ArrayList<String> languages, String education_level, String type, int id) {
        super(id, first_name, last_name, date_of_birth, email, phone, password, gender, languages, type);
        this.education_level = education_level;
        this.approved = false;
    }

    /**
     * Returns the tutor's education level.
     *
     * @return the education level
     */
    public String getEducation_level() {
        return education_level;
    }

    /**
     * Sets the tutor's education level.
     *
     * @param education_level the new education level
     */
    public void setEducation_level(String education_level) {
        this.education_level = education_level;
    }

    /**
     * Returns whether this tutor has been approved by an administrator.
     *
     * @return {@code true} if the tutor is approved; {@code false} otherwise
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Sets the approval status of this tutor.
     *
     * @param approved {@code true} to approve the tutor; {@code false} to revoke approval
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * Returns a string representation of this tutor for debugging purposes.
     *
     * @return a formatted string with tutor-specific and inherited user fields
     */
    @Override public String toString() {
        return "Tutor{education_level='" + education_level + "', approved=" + approved + "}" + super.toString();
    }
}
