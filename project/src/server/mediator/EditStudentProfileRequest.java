package server.mediator;

import server.model.Student;

public class EditStudentProfileRequest {
    private final String type = "EDIT_STUDENT_PROFILE";
    private Student student;

    public EditStudentProfileRequest(Student student) {
        this.student = student;
    }

    public String getType() { return type; }
    public Student getStudent() { return student; }
}
