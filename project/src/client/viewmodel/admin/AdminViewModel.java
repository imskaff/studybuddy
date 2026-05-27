package client.viewmodel.admin;

import client.model.Model;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Tutor;

/**
 * ViewModel for the administrator panel.
 * 
 * Manages the observable list of pending tutor registrations and
 * provides approve/decline actions that run on a background daemon thread
 * to avoid blocking the JavaFX application thread.
 * 
 *
 * @author
 */
public class AdminViewModel {
    private final Model model;
    private final ObservableList<Tutor> pendingTutors;

    /**
     * Constructs an {@code AdminViewModel} with the given model.
     *
     * @param model the client-side model
     */
    public AdminViewModel(Model model) {
        this.model = model;
        this.pendingTutors = FXCollections.observableArrayList();
    }

    /**
     * Fetches pending tutor registrations from the server and populates
     * the observable list.
     */
    public void loadPendingTutors() {
        pendingTutors.setAll(model.getPendingTutors());
    }

    /**
     * Approves the given tutor on a background thread. On success, removes
     * the tutor from the observable list on the JavaFX application thread.
     *
     * @param tutor the tutor to approve
     */
    public void approveTutor(Tutor tutor) {
        runAsync(() -> {
            if (model.approveTutor(tutor.getId()))
                Platform.runLater(() -> pendingTutors.remove(tutor));
        });
    }

    /**
     * Declines the given tutor on a background thread. On success, removes
     * the tutor from the observable list on the JavaFX application thread.
     *
     * @param tutor the tutor to decline
     */
    public void declineTutor(Tutor tutor) {
        runAsync(() -> {
            if (model.declineTutor(tutor.getId()))
                Platform.runLater(() -> pendingTutors.remove(tutor));
        });
    }

    /**
     * Runs the given task on a new daemon thread.
     *
     * @param task the task to execute asynchronously
     */
    private static void runAsync(Runnable task) {
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Returns the observable list of pending tutor registrations.
     *
     * @return the pending-tutors list
     */
    public ObservableList<Tutor> getPendingTutors() {
        return pendingTutors;
    }
}
