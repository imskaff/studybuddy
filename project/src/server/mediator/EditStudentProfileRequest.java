package server.mediator;

import server.model.Student;

/**
 * Request sent from the client when a student updates their profile information.
 *
 * @author
 */
public class EditStudentProfileRequest
{
    private String type = "EDIT_STUDENT_PROFILE";
    private Student student;

    /**
     * Constructs an {@code EditStudentProfileRequest} wrapping the updated student data.
     *
     * @param student the student with updated field values
     */
    public EditStudentProfileRequest(Student student) {
        this.student = student;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "EDIT_STUDENT_PROFILE"}
     */
    public String getType() { return type; }

    /**
     * Returns the student with the updated field values.
     *
     * @return the updated student
     */
    public Student getStudent() { return student; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with the student
     */
    @Override
    public String toString()
    {
        return "EDIT_STUDENT_PROFILE{" +
            "student=" + student + '}';
    }
}
