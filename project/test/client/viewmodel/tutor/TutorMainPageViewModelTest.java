package client.viewmodel.tutor;

import client.model.Model;
import client.viewmodel.ViewState;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Session;
import server.model.Tutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TutorMainPageViewModelTest {

    private Model mockModel;
    private TutorMainPageViewModel viewModel;

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 1, 10, 0);
    private static final LocalDateTime END   = LocalDateTime.of(2026, 6, 1, 12, 0);

    @BeforeEach
    void setUp() {
        mockModel = mock(Model.class);
        viewModel = new TutorMainPageViewModel(mockModel, new ViewState());
    }

    // --- loading sessions ---

    @Test
    void listIsEmptyBeforeLoadingSessions() {
        assertTrue(viewModel.getSessions().isEmpty());
    }

    @Test
    void loadSessionsPopulatesListFromModel() {
        Session s1 = makeSession(1, "Math");
        Session s2 = makeSession(2, "Physics");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(s1, s2)));

        viewModel.loadSessions();

        ObservableList<Session> sessions = viewModel.getSessions();
        assertEquals(2, sessions.size());
        assertTrue(sessions.contains(s1));
        assertTrue(sessions.contains(s2));
    }

    @Test
    void loadSessionsEmptyWhenModelReturnsEmptyList() {
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>());

        viewModel.loadSessions();

        assertTrue(viewModel.getSessions().isEmpty());
    }

    @Test
    void loadSessionsReplacesExistingList() {
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(makeSession(1, "Math"))));
        viewModel.loadSessions();

        Session replacement = makeSession(2, "Chemistry");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(replacement)));
        viewModel.loadSessions();

        ObservableList<Session> sessions = viewModel.getSessions();
        assertEquals(1, sessions.size());
        assertEquals("Chemistry", sessions.get(0).getSubject());
    }

    // --- creating a session ---

    @Test
    void listReflectsNewSessionAfterReload() {
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>());
        viewModel.loadSessions();

        Session created = makeSession(10, "French");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(created)));
        viewModel.loadSessions();

        assertEquals(1, viewModel.getSessions().size());
        assertEquals("French", viewModel.getSessions().getFirst().getSubject());
    }

    @Test
    void listSizeIncreasesAfterNewSessionIsCreatedAndReloaded() {
        Session existing = makeSession(1, "Math");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(existing)));
        viewModel.loadSessions();

        Session created = makeSession(2, "Biology");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(existing, created)));
        viewModel.loadSessions();

        assertEquals(2, viewModel.getSessions().size());
    }

    @Test
    void createdSessionDescriptionIsVisibleAfterReload() {
        Session created = makeSession(5, "Statistics");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(created)));

        viewModel.loadSessions();

        assertEquals("Test description", viewModel.getSessions().get(0).getDescription());
    }

    // --- updating a session ---

    @Test
    void listReflectsUpdatedSubjectAfterReload() {
        Session original = makeSession(1, "Physics");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(original)));
        viewModel.loadSessions();

        Session updated = makeSessionWithSubject(1, "Quantum Physics");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(updated)));
        viewModel.loadSessions();

        assertEquals("Quantum Physics", viewModel.getSessions().getFirst().getSubject());
    }

    @Test
    void listReflectsUpdatedPriceAfterReload() {
        Session original = makeSession(1, "Math");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(original)));
        viewModel.loadSessions();

        Session updated = makeSessionWithPrice(1, "Math", 200.0);
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(updated)));
        viewModel.loadSessions();

        assertEquals(200.0, viewModel.getSessions().getFirst().getPrice());
    }

    @Test
    void listSizeDoesNotChangeAfterUpdateAndReload() {
        Session s1 = makeSession(1, "Math");
        Session s2 = makeSession(2, "Physics");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(s1, s2)));
        viewModel.loadSessions();

        Session s1Updated = makeSessionWithSubject(1, "Advanced Math");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(s1Updated, s2)));
        viewModel.loadSessions();

        assertEquals(2, viewModel.getSessions().size());
    }

    @Test
    void updatedDescriptionIsVisibleAfterReload() {
        Session original = makeSession(1, "Math");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(original)));
        viewModel.loadSessions();

        Session updated = makeSessionWithDescription(1, "Math", "Updated description");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(updated)));
        viewModel.loadSessions();

        assertEquals("Updated description", viewModel.getSessions().getFirst().getDescription());
    }

    // --- helpers ---

    private static Tutor makeTutor() {
        return new Tutor("John", "Doe", LocalDate.of(1985, 1, 1),
                "john@example.com", "111111111", "pass", "Male",
                new ArrayList<>(), "Master's", "tutor", 10);
    }

    private static Session makeSession(int id, String subject) {
        return new Session(id, "Test description", 50.0, subject, "Room A",
                START, END, makeTutor(), null);
    }

    private static Session makeSessionWithSubject(int id, String subject) {
        return new Session(id, "Test description", 50.0, subject, "Room A",
                START, END, makeTutor(), null);
    }

    private static Session makeSessionWithPrice(int id, String subject, double price) {
        return new Session(id, "Test description", price, subject, "Room A",
                START, END, makeTutor(), null);
    }

    private static Session makeSessionWithDescription(int id, String subject, String description) {
        return new Session(id, description, 50.0, subject, "Room A",
                START, END, makeTutor(), null);
    }
}
