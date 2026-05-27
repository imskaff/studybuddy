package server.mediator;

import server.model.Student;

/**
 * Request sent from the client when a user registers a new student profile.
 * Registration is rejected by the server if {@code rulesConfirmed} is {@code false}.
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class CreateStudentProfileRequest
{
    private String type = "CREATE_STUDENT_PROFILE";
    private Student student;
    private boolean rulesConfirmed;

    /**
     * Constructs a {@code CreateStudentProfileRequest}.
     *
     * @param student        the student data to register
     * @param rulesConfirmed whether the user has confirmed the platform rules
     */
    public CreateStudentProfileRequest(Student student, boolean rulesConfirmed)
    {
        this.student = student;
        this.rulesConfirmed = rulesConfirmed;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "CREATE_STUDENT_PROFILE"}
     */
    public String getType() { return type; }

    /**
     * Returns the student data to register.
     *
     * @return the student
     */
    public Student getStudent() { return student; }

    /**
     * Returns whether the user has confirmed the platform rules.
     *
     * @return {@code true} if rules were confirmed
     */
    public boolean isRulesConfirmed() { return rulesConfirmed; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with the student and rules confirmation flag
     */
    @Override
    public String toString()
    {
        return "CREATE_STUDENT_PROFILE{" +
            "student=" + student +
            ", rulesConfirmed=" + rulesConfirmed +
            '}';
    }
}
