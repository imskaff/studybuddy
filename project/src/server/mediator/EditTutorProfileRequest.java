package server.mediator;

import server.model.Tutor;

public class EditTutorProfileRequest {
    private final String type = "EDIT_TUTOR_PROFILE";
    private Tutor tutor;

    public EditTutorProfileRequest(Tutor tutor) {
        this.tutor = tutor;
    }

    public String getType() { return type; }
    public Tutor getTutor() { return tutor; }
}
