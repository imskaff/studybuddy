package client.viewmodel;

import client.model.Model;
import client.viewmodel.admin.AdminViewModel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Tutor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminViewModelTest {

    private Model mockModel;
    private AdminViewModel viewModel;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // JavaFX already running
        }
        Thread.sleep(100);
    }

    @BeforeEach
    void setUp() {
        mockModel = mock(Model.class);
        viewModel = new AdminViewModel(mockModel);
    }

    // --- loadPendingTutors ---

    @Test
    void loadPendingTutorsPopulatesListFromModel() {
        Tutor t1 = makeTutor(1, "Alice", "Smith");
        Tutor t2 = makeTutor(2, "Bob", "Jones");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(t1, t2)));

        viewModel.loadPendingTutors();

        ObservableList<Tutor> result = viewModel.getPendingTutors();
        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
    }

    @Test
    void loadPendingTutorsWithEmptyListShouldBeEmpty() {
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>());

        viewModel.loadPendingTutors();

        assertTrue(viewModel.getPendingTutors().isEmpty());
    }

    @Test
    void loadPendingTutorsReplacesExistingContent() {
        Tutor old = makeTutor(1, "Old", "Tutor");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(old)));
        viewModel.loadPendingTutors();

        Tutor fresh = makeTutor(2, "New", "Tutor");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(fresh)));
        viewModel.loadPendingTutors();

        ObservableList<Tutor> result = viewModel.getPendingTutors();
        assertEquals(1, result.size());
        assertTrue(result.contains(fresh));
        assertFalse(result.contains(old));
    }

    // --- approveTutor ---

    @Test
    void approveTutorOnFailureKeepsTutorInList() throws InterruptedException {
        Tutor tutor = makeTutor(1, "Alice", "Smith");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(tutor)));
        when(mockModel.approveTutor(1)).thenReturn(false);
        viewModel.loadPendingTutors();

        viewModel.approveTutor(tutor);
        Thread.sleep(300);

        assertTrue(viewModel.getPendingTutors().contains(tutor));
    }

    @Test
    void approveTutorCallsModelWithCorrectId() throws InterruptedException {
        Tutor tutor = makeTutor(42, "Alice", "Smith");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(tutor)));
        when(mockModel.approveTutor(42)).thenReturn(true);
        viewModel.loadPendingTutors();

        viewModel.approveTutor(tutor);
        Thread.sleep(300);

        assertFalse(viewModel.getPendingTutors().contains(tutor));
    }

    @Test
    void approveTutorOnSuccessRemovesFromPendingList() throws InterruptedException {
        Tutor tutor = makeTutor(1, "Alice", "Smith");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(tutor)));
        when(mockModel.approveTutor(1)).thenReturn(true);
        viewModel.loadPendingTutors();

        viewModel.approveTutor(tutor);
        Thread.sleep(300);

        assertFalse(viewModel.getPendingTutors().contains(tutor));
    }

    @Test
    void approveTutorDoesNotRemoveOtherTutors() throws InterruptedException {
        Tutor t1 = makeTutor(1, "Alice", "Smith");
        Tutor t2 = makeTutor(2, "Bob", "Jones");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(t1, t2)));
        when(mockModel.approveTutor(1)).thenReturn(true);
        viewModel.loadPendingTutors();

        viewModel.approveTutor(t1);
        Thread.sleep(300);

        assertFalse(viewModel.getPendingTutors().contains(t1));
        assertTrue(viewModel.getPendingTutors().contains(t2));
    }

    // --- declineTutor ---

    @Test
    void declineTutorCallsModelWithCorrectId() throws InterruptedException {
        Tutor tutor = makeTutor(99, "Bob", "Jones");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(tutor)));
        when(mockModel.declineTutor(99)).thenReturn(true);
        viewModel.loadPendingTutors();

        viewModel.declineTutor(tutor);
        Thread.sleep(300);

        assertFalse(viewModel.getPendingTutors().contains(tutor));
    }

    @Test
    void declineTutorOnSuccessRemovesFromPendingList() throws InterruptedException {
        Tutor tutor = makeTutor(1, "Bob", "Jones");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(tutor)));
        when(mockModel.declineTutor(1)).thenReturn(true);
        viewModel.loadPendingTutors();

        viewModel.declineTutor(tutor);
        Thread.sleep(300);

        assertFalse(viewModel.getPendingTutors().contains(tutor));
    }

    @Test
    void declineTutorOnFailureKeepsTutorInList() throws InterruptedException {
        Tutor tutor = makeTutor(1, "Bob", "Jones");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(tutor)));
        when(mockModel.declineTutor(1)).thenReturn(false);
        viewModel.loadPendingTutors();

        viewModel.declineTutor(tutor);
        Thread.sleep(300);

        assertTrue(viewModel.getPendingTutors().contains(tutor));
    }

    @Test
    void declineTutorDoesNotRemoveOtherTutors() throws InterruptedException {
        Tutor t1 = makeTutor(1, "Alice", "Smith");
        Tutor t2 = makeTutor(2, "Bob", "Jones");
        when(mockModel.getPendingTutors()).thenReturn(new ArrayList<>(List.of(t1, t2)));
        when(mockModel.declineTutor(2)).thenReturn(true);
        viewModel.loadPendingTutors();

        viewModel.declineTutor(t2);
        Thread.sleep(300);

        assertTrue(viewModel.getPendingTutors().contains(t1));
        assertFalse(viewModel.getPendingTutors().contains(t2));
    }

    // --- helpers ---

    private static Tutor makeTutor(int id, String firstName, String lastName) {
        return new Tutor(firstName, lastName, LocalDate.of(1990, 1, 1),
                firstName.toLowerCase() + "@example.com", "000000000",
                "password", "Other", new ArrayList<>(), "Bachelor's", "tutor", id);
    }
}
