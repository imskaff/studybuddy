package client.viewmodel.tutor;

import client.model.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RequestSubjectViewModel {
    private final Model model;
    private final StringProperty subjectName = new SimpleStringProperty("");
    private final StringProperty reason = new SimpleStringProperty("");

    public RequestSubjectViewModel(Model model) {
        this.model = model;
    }

    public StringProperty subjectNameProperty() { return subjectName; }
    public StringProperty reasonProperty() { return reason; }

    public boolean submit() {
        String email = model.getLoggedInTutor().getEmail();
        return model.submitSubjectProposal(email, subjectName.get(), reason.get());
    }

    public void reset() {
        subjectName.set("");
        reason.set("");
    }
}
