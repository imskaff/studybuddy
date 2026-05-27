package server.model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a student user in the system.
 * 
 * Extends {@link User} with no additional fields. Students can browse
 * available tutoring sessions and book them.
 * 
 *
 * @author
 */
public class Student extends User {

    /**
     * Constructs a fully populated {@code Student}.
     *
     * @param first_name    the student's first name
     * @param last_name     the student's last name
     * @param date_of_birth the student's date of birth
     * @param email         the student's email address
     * @param phone         the student's phone number
     * @param password      the student's password
     * @param gender        the student's gender
     * @param languages     the list of languages spoken by the student
     * @param id            the unique database identifier
     * @param type          the user type string (should be {@code "Student"})
     */
    public Student(String first_name, String last_name, LocalDate date_of_birth, String email, String phone,
                   String password, String gender, ArrayList<String> languages, int id, String type) {
        super(id, first_name, last_name, date_of_birth, email, phone, password, gender, languages, type);
    }

    /**
     * Constructs a minimal {@code Student} containing only the database ID.
     * 
     * Used internally when only the student reference is needed (e.g. when
     * linking a booked session to a student).
     * 
     *
     * @param id the unique database identifier
     */
    public Student(int id)
    {
        super(id, "", "", null,
            "", "", "", "",
            new ArrayList<>(), "Student");
    }
}
