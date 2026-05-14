package client.viewmodel;

import client.model.Model;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Tutor;

public class AdminViewModel {
    private final Model model;
    private final ObservableList<Tutor> pendingTutors;

    public AdminViewModel(Model model) {
        this.model = model;
        this.pendingTutors = FXCollections.observableArrayList();
    }

    public void loadPendingTutors() {
        pendingTutors.setAll(model.getPendingTutors());
    }

    public void approveTutor(Tutor tutor) {
        Thread t = new Thread(() -> {
            boolean success = model.approveTutor(tutor.getId());
            if (success)
                Platform.runLater(() -> pendingTutors.remove(tutor));
        });
        t.setDaemon(true);
        t.start();
    }

    public void declineTutor(Tutor tutor) {
        Thread t = new Thread(() -> {
            boolean success = model.declineTutor(tutor.getId());
            if (success)
                Platform.runLater(() -> pendingTutors.remove(tutor));
        });
        t.setDaemon(true);
        t.start();
    }

    public ObservableList<Tutor> getPendingTutors() {
        return pendingTutors;
    }
}
