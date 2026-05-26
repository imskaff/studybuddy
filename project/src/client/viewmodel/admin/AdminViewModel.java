package client.viewmodel.admin;

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
        runAsync(() -> {
            if (model.approveTutor(tutor.getId()))
                Platform.runLater(() -> pendingTutors.remove(tutor));
        });
    }

    public void declineTutor(Tutor tutor) {
        runAsync(() -> {
            if (model.declineTutor(tutor.getId()))
                Platform.runLater(() -> pendingTutors.remove(tutor));
        });
    }

    private static void runAsync(Runnable task) {
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    public ObservableList<Tutor> getPendingTutors() {
        return pendingTutors;
    }
}
