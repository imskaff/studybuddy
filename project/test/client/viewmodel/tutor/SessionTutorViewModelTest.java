package client.viewmodel.tutor;

import client.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionTutorViewModelTest
{
    private Model mockModel;
    private SessionTutorViewModel viewModel;

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 1, 10, 0);
    private static final LocalDateTime END   = LocalDateTime.of(2026, 6, 1, 12, 0);

    @BeforeEach
    void setUp()
    {
        mockModel = mock(Model.class);
        viewModel = new SessionTutorViewModel(mockModel);
    }

    // --- loading session details ---

    @Test
    void loadSessionSetsSubjectProperty()
    {
        Session session = makeSession(1, "Physics");

        viewModel.loadSession(session);

        assertEquals("Physics", viewModel.subjectProperty().get());
    }

    @Test
    void loadSessionSetsDescriptionProperty()
    {
        Session session = makeSession(1, "Physics");

        viewModel.loadSession(session);

        assertEquals("Intro to calculus", viewModel.descriptionProperty().get());
    }

    @Test
    void loadSessionSetsTutorNameProperty()
    {
        Session session = makeSession(1, "Physics");

        viewModel.loadSession(session);

        assertEquals("John Doe", viewModel.tutorProperty().get());
    }

    @Test
    void loadSessionSetsFormattedDates()
    {
        Session session = makeSession(1, "Physics");

        viewModel.loadSession(session);

        assertEquals("01/06/2026 10:00", viewModel.startDateProperty().get());
        assertEquals("01/06/2026 12:00", viewModel.endDateProperty().get());
    }

    @Test
    void loadSessionSetsPriceProperty()
    {
        Session session = makeSession(1, "Physics");

        viewModel.loadSession(session);

        assertEquals("75.0", viewModel.priceProperty().get());
    }

    @Test
    void loadSessionSetsLocationProperty()
    {
        Session session = makeSession(1, "Physics");

        viewModel.loadSession(session);

        assertEquals("Room B", viewModel.locationProperty().get());
    }

    @Test
    void loadSessionStoresSelectedSession()
    {
        Session session = makeSession(1, "Physics");

        viewModel.loadSession(session);

        assertEquals(session, viewModel.getSelectedSession());
    }

    @Test
    void propertiesAreEmptyBeforeLoadingSession()
    {
        assertEquals("", viewModel.subjectProperty().get());
        assertEquals("", viewModel.tutorProperty().get());
        assertEquals("", viewModel.priceProperty().get());
        assertEquals("", viewModel.locationProperty().get());
    }

    @Test
    void getSelectedSessionBeforeLoadReturnsNull()
    {
        assertNull(viewModel.getSelectedSession());
    }

    @Test
    void loadingDifferentSessionOverwritesPreviousData()
    {
        viewModel.loadSession(makeSession(1, "Physics"));
        Session second = makeSession(2, "Chemistry");

        viewModel.loadSession(second);

        assertEquals("Chemistry", viewModel.subjectProperty().get());
        assertEquals(second, viewModel.getSelectedSession());
    }

    // --- editing session ---

    @Test
    void editSessionCallsModelWithUpdatedSession()
    {
        FakeModel fake = new FakeModel();
        SessionTutorViewModel vm = new SessionTutorViewModel(fake);
        vm.loadSession(makeSession(1, "Physics"));
        vm.subjectProperty().set("Chemistry");
        vm.priceProperty().set("99.0");

        vm.editSession();

        assertEquals("Chemistry", fake.lastEditedSession.getSubject());
        assertEquals(99.0, fake.lastEditedSession.getPrice());
        assertEquals(1, fake.lastEditedSession.getId());
    }

    @Test
    void editSessionPreservesSessionIdAndTutor()
    {
        FakeModel fake = new FakeModel();
        SessionTutorViewModel vm = new SessionTutorViewModel(fake);
        Session original = makeSession(42, "Physics");
        vm.loadSession(original);
        vm.subjectProperty().set("Biology");

        vm.editSession();

        assertEquals(42, fake.lastEditedSession.getId());
        assertEquals(original.getTutor(), fake.lastEditedSession.getTutor());
    }

    @Test
    void editSessionReturnsTrueOnSuccess()
    {
        viewModel.loadSession(makeSession(1, "Physics"));
        when(mockModel.editSession(any(Session.class))).thenReturn(true);

        boolean result = viewModel.editSession();

        assertTrue(result);
    }

    @Test
    void editSessionReturnsFalseWhenModelFails()
    {
        viewModel.loadSession(makeSession(1, "Physics"));
        when(mockModel.editSession(any(Session.class))).thenReturn(false);

        boolean result = viewModel.editSession();

        assertFalse(result);
    }

    @Test
    void editSessionWithNoSessionLoadedReturnsFalse()
    {
        boolean result = viewModel.editSession();

        assertFalse(result);
    }

    @Test
    void editSessionWithInvalidPriceReturnsFalse()
    {
        viewModel.loadSession(makeSession(1, "Physics"));
        viewModel.priceProperty().set("not-a-number");

        boolean result = viewModel.editSession();

        assertFalse(result);
    }

    @Test
    void editSessionWithInvalidDateReturnsFalse()
    {
        viewModel.loadSession(makeSession(1, "Physics"));
        viewModel.startDateProperty().set("bad-date");

        boolean result = viewModel.editSession();

        assertFalse(result);
    }

    // --- deleting session ---

    @Test
    void deleteSessionCallsModelWithCorrectId()
    {
        viewModel.loadSession(makeSession(7, "Physics"));
        when(mockModel.deleteSession(7)).thenReturn(true);

        viewModel.deleteSession();
    }

    @Test
    void deleteSessionReturnsTrueOnSuccess()
    {
        viewModel.loadSession(makeSession(1, "Physics"));
        when(mockModel.deleteSession(anyInt())).thenReturn(true);

        boolean result = viewModel.deleteSession();

        assertTrue(result);
    }

    @Test
    void deleteSessionReturnsFalseWhenModelFails()
    {
        viewModel.loadSession(makeSession(1, "Physics"));
        when(mockModel.deleteSession(anyInt())).thenReturn(false);

        boolean result = viewModel.deleteSession();

        assertFalse(result);
    }

    @Test
    void deleteSessionWithNoSessionLoadedReturnsFalse()
    {
        boolean result = viewModel.deleteSession();

        assertFalse(result);
    }

    // --- helpers ---

    private static Tutor makeTutor()
    {
        return new Tutor("John", "Doe", LocalDate.of(1985, 1, 1),
                "john@example.com", "111111111", "pass", "Male",
                new ArrayList<>(), "Master's", "tutor", 10);
    }

    private static Session makeSession(int id, String subject)
    {
        return new Session(id, "Intro to calculus", 75.0, subject, "Room B",
                START, END, makeTutor(), null);
    }

    private static class FakeModel implements Model
    {
        Session lastEditedSession;
        boolean editReturn = true;

        @Override public boolean editSession(Session session) { lastEditedSession = session; return editReturn; }

        @Override public void connect() {}
        @Override public void disconnect() {}
        @Override public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {}
        @Override public ArrayList<Session> getAllSessions() { return new ArrayList<>(); }
        @Override public ArrayList<Session> getTutorSessions() { return new ArrayList<>(); }
        @Override public ArrayList<Session> getBookedSessions() { return new ArrayList<>(); }
        @Override public void bookSession(Student s, Tutor t, Session se) {}
        @Override public void setLoggedInStudent(Student s) {}
        @Override public Student getLoggedInStudent() { return null; }
        @Override public void setLoggedInAdminId(int id) {}
        @Override public int getLoggedInAdminId() { return 0; }
        @Override public void setLoggedInTutor(Tutor t) {}
        @Override public Tutor getLoggedInTutor() { return null; }
        @Override public ArrayList<Tutor> getPendingTutors() { return new ArrayList<>(); }
        @Override public boolean createSession(Session s) { return false; }
        @Override public boolean cancelSession(int u, int s, String t) { return false; }
        @Override public boolean deleteSession(int id) { return false; }
        @Override public boolean approveTutor(int id) { return false; }
        @Override public boolean declineTutor(int id) { return false; }
        @Override public boolean registerStudent(Student s) { return false; }
        @Override public boolean registerTutor(Tutor t) { return false; }
        @Override public boolean editStudentProfile(Student s) { return false; }
        @Override public boolean editTutorProfile(Tutor t) { return false; }
        @Override public java.util.List<String> getSubjects() { return new ArrayList<>(); }
        @Override public String login(String e, String p) { return null; }
        @Override public void complete(Session s) {}
        @Override public void book(Session s) {}
        @Override public void cancel(Session s) {}
        @Override public void remove(Session s) {}
    }
}
