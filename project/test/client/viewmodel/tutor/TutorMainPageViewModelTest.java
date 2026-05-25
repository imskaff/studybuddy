package client.viewmodel.tutor;

import client.model.Model;
import client.viewmodel.ViewState;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Session;
import server.model.Tutor;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TutorMainPageViewModelTest {

    private FakeModel fakeModel;
    private TutorMainPageViewModel viewModel;

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 1, 10, 0);
    private static final LocalDateTime END   = LocalDateTime.of(2026, 6, 1, 12, 0);

    @BeforeEach
    void setUp() {
        fakeModel = new FakeModel();
        viewModel = new TutorMainPageViewModel(fakeModel, new ViewState());
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
        fakeModel.sessions = new ArrayList<>(List.of(s1, s2));

        viewModel.loadSessions();

        ObservableList<Session> sessions = viewModel.getSessions();
        assertEquals(2, sessions.size());
        assertTrue(sessions.contains(s1));
        assertTrue(sessions.contains(s2));
    }

    @Test
    void loadSessionsEmptyWhenModelReturnsEmptyList() {
        fakeModel.sessions = new ArrayList<>();

        viewModel.loadSessions();

        assertTrue(viewModel.getSessions().isEmpty());
    }

    @Test
    void loadSessionsReplacesExistingList() {
        fakeModel.sessions = new ArrayList<>(List.of(makeSession(1, "Math")));
        viewModel.loadSessions();

        fakeModel.sessions = new ArrayList<>(List.of(makeSession(2, "Chemistry")));
        viewModel.loadSessions();

        ObservableList<Session> sessions = viewModel.getSessions();
        assertEquals(1, sessions.size());
        assertEquals("Chemistry", sessions.get(0).getSubject());
    }

    // --- creating a session ---

    @Test
    void listReflectsNewSessionAfterReload() {
        fakeModel.sessions = new ArrayList<>();
        viewModel.loadSessions();

        fakeModel.sessions = new ArrayList<>(List.of(makeSession(10, "French")));
        viewModel.loadSessions();

        assertEquals(1, viewModel.getSessions().size());
        assertEquals("French", viewModel.getSessions().getFirst().getSubject());
    }

    @Test
    void listSizeIncreasesAfterNewSessionIsCreatedAndReloaded() {
        Session existing = makeSession(1, "Math");
        fakeModel.sessions = new ArrayList<>(List.of(existing));
        viewModel.loadSessions();

        fakeModel.sessions = new ArrayList<>(List.of(existing, makeSession(2, "Biology")));
        viewModel.loadSessions();

        assertEquals(2, viewModel.getSessions().size());
    }

    @Test
    void createdSessionDescriptionIsVisibleAfterReload() {
        fakeModel.sessions = new ArrayList<>(List.of(makeSession(5, "Statistics")));

        viewModel.loadSessions();

        assertEquals("Test description", viewModel.getSessions().get(0).getDescription());
    }

    // --- updating a session ---

    @Test
    void listReflectsUpdatedSubjectAfterReload() {
        fakeModel.sessions = new ArrayList<>(List.of(makeSession(1, "Physics")));
        viewModel.loadSessions();

        fakeModel.sessions = new ArrayList<>(List.of(makeSession(1, "Quantum Physics")));
        viewModel.loadSessions();

        assertEquals("Quantum Physics", viewModel.getSessions().getFirst().getSubject());
    }

    @Test
    void listReflectsUpdatedPriceAfterReload() {
        fakeModel.sessions = new ArrayList<>(List.of(makeSession(1, "Math")));
        viewModel.loadSessions();

        fakeModel.sessions = new ArrayList<>(List.of(makeSessionWithPrice(1, "Math", 200.0)));
        viewModel.loadSessions();

        assertEquals(200.0, viewModel.getSessions().getFirst().getPrice());
    }

    @Test
    void listSizeDoesNotChangeAfterUpdateAndReload() {
        Session s1 = makeSession(1, "Math");
        Session s2 = makeSession(2, "Physics");
        fakeModel.sessions = new ArrayList<>(List.of(s1, s2));
        viewModel.loadSessions();

        fakeModel.sessions = new ArrayList<>(List.of(makeSession(1, "Advanced Math"), s2));
        viewModel.loadSessions();

        assertEquals(2, viewModel.getSessions().size());
    }

    @Test
    void updatedDescriptionIsVisibleAfterReload() {
        fakeModel.sessions = new ArrayList<>(List.of(makeSession(1, "Math")));
        viewModel.loadSessions();

        fakeModel.sessions = new ArrayList<>(List.of(makeSessionWithDescription(1, "Math", "Updated description")));
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

    private static Session makeSessionWithPrice(int id, String subject, double price) {
        return new Session(id, "Test description", price, subject, "Room A",
                START, END, makeTutor(), null);
    }

    private static Session makeSessionWithDescription(int id, String subject, String description) {
        return new Session(id, description, 50.0, subject, "Room A",
                START, END, makeTutor(), null);
    }

    // --- fake model ---

    private static class FakeModel implements Model {
        ArrayList<Session> sessions = new ArrayList<>();

        @Override public ArrayList<Session> getAllSessions() { return sessions; }
        @Override public void addPropertyChangeListener(PropertyChangeListener l) {}
        @Override public void connect() {}
        @Override public void disconnect() {}
        @Override public void bookSession(server.model.Student s, Tutor t, Session se) {}
        @Override public ArrayList<Session> getBookedSessions() { return new ArrayList<>(); }
        @Override public void setLoggedInStudent(server.model.Student s) {}
        @Override public server.model.Student getLoggedInStudent() { return null; }
        @Override public void setLoggedInAdminId(int id) {}
        @Override public int getLoggedInAdminId() { return 0; }
        @Override public ArrayList<Tutor> getPendingTutors() { return new ArrayList<>(); }
        @Override public boolean createSession(Session s) { return false; }
        @Override public boolean editSession(Session s) { return false; }
        @Override public boolean cancelSession(int u, int s, String t) { return false; }
        @Override public boolean deleteSession(int id) { return false; }
        @Override public boolean approveTutor(int id) { return false; }
        @Override public boolean declineTutor(int id) { return false; }
        @Override public boolean registerStudent(server.model.Student s) { return false; }
        @Override public boolean registerTutor(Tutor t) { return false; }
        @Override public void setLoggedInTutor(Tutor t) {}
        @Override public Tutor getLoggedInTutor() { return null; }
        @Override public boolean editStudentProfile(server.model.Student s) { return false; }
        @Override public java.util.List<String> getSubjects() { return new ArrayList<>(); }
        @Override public boolean editTutorProfile(Tutor t) { return false; }
        @Override public String login(String e, String p) { return null; }
        @Override public void complete(Session s) {}
        @Override public void book(Session s) {}
        @Override public void cancel(Session s) {}
        @Override public void remove(Session s) {}
    }
}
