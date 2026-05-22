package client.model;

import server.model.Tutor;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public interface AdminModel {
    ArrayList<Tutor> getPendingTutors();
    boolean approveTutor(int tutorId);
    boolean declineTutor(int tutorId);
    void addPropertyChangeListener(PropertyChangeListener listener);
}
