package client.viewmodel.student;

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

class SessionStudentViewModelTest
{
    private Model mockModel;
    private SessionStudentViewModel viewModel;

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 1, 10, 0);
    private static final LocalDateTime END   = LocalDateTime.of(2026, 6, 1, 12, 0);

    @BeforeEach
    void setUp()
    {
        mockModel = mock(Model.class);
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>());
        viewModel = new SessionStudentViewModel(mockModel);
    }

    // --- loading session details ---

    @Test
    void loadSessionSetsSubjectProperty()
    {
        Session session = makeSession(1, "Mathematics");

        viewModel.loadSession(session);

        assertEquals("Mathematics", viewModel.subjectProperty().get());
    }

    @Test
    void loadSessionSetsDescriptionProperty()
    {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals("Intro to calculus", viewModel.descriptionProperty().get());
    }

    @Test
    void loadSessionSetsTutorNameProperty()
    {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals("John Doe", viewModel.tutorProperty().get());
    }

    @Test
    void loadSessionSetsFormattedDates()
    {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals("01/06/2026 10:00", viewModel.startDateProperty().get());
        assertEquals("01/06/2026 12:00", viewModel.endDateProperty().get());
    }

    @Test
    void loadSessionSetsPriceProperty()
    {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals("75.0", viewModel.priceProperty().get());
    }

    @Test
    void loadSessionSetsLocationProperty()
    {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals("Room B", viewModel.locationProperty().get());
    }

    @Test
    void loadSessionStoresSelectedSession()
    {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals(session, viewModel.getSelectedSession());
    }

    @Test
    void getSelectedTutorReturnsTutorFromLoadedSession()
    {
        Session session = makeSession(1, "Math");
        viewModel.loadSession(session);

        Tutor tutor = viewModel.getSelectedTutor();

        assertNotNull(tutor);
        assertEquals("John", tutor.getFirst_name());
        assertEquals("Doe", tutor.getLast_name());
    }

    @Test
    void getSelectedTutorBeforeLoadReturnsNull()
    {
        assertNull(viewModel.getSelectedTutor());
    }

    // booking session

    @Test
    void bookSessionCallsModelWithCorrectStudentAndSession() throws Exception
    {
        FakeModel fake = new FakeModel();
        SessionStudentViewModel vm = new SessionStudentViewModel(fake);
        Session session = makeSession(1, "Math");
        Student student = makeStudent(5);
        fake.loggedInStudent = student;
        vm.loadSession(session);

        vm.bookSession();

        assertEquals(student, fake.lastBookedStudent);
        assertEquals(session, fake.lastBookedSession);
    }

    @Test
    void bookSessionWithNoSessionLoadedDoesNotCallModel() throws Exception
    {
        FakeModel fake = new FakeModel();
        SessionStudentViewModel vm = new SessionStudentViewModel(fake);
        fake.loggedInStudent = makeStudent(5);

        vm.bookSession();

        assertNull(fake.lastBookedSession);
    }

    @Test
    void bookSessionWithNoLoggedInStudentDoesNotCallModel() throws Exception
    {
        FakeModel fake = new FakeModel();
        SessionStudentViewModel vm = new SessionStudentViewModel(fake);
        fake.loggedInStudent = null;
        vm.loadSession(makeSession(1, "Math"));

        vm.bookSession();

        assertNull(fake.lastBookedSession);
    }

    // helpers

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

    private static Student makeStudent(int id)
    {
        return new Student("Anna", "Brown", LocalDate.of(2000, 3, 15),
                "anna@example.com", "222222222", "pass", "Female",
                new ArrayList<>(), id, "student");
    }

    private static class FakeModel implements Model
    {
        Student loggedInStudent;
        Student lastBookedStudent;
        Session lastBookedSession;

        @Override public void bookSession(Student s, Tutor t, Session se) { lastBookedStudent = s; lastBookedSession = se; }
        @Override public Student getLoggedInStudent() { return loggedInStudent; }
        @Override public ArrayList<Session> getAllSessions() { return new ArrayList<>(); }

        @Override public void connect() {}
        @Override public void disconnect() {}
        @Override public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {}
        @Override public ArrayList<Session> getTutorSessions() { return new ArrayList<>(); }
        @Override public ArrayList<Session> getBookedSessions() { return new ArrayList<>(); }
        @Override public void setLoggedInStudent(Student s) {}
        @Override public void setLoggedInAdminId(int id) {}
        @Override public int getLoggedInAdminId() { return 0; }
        @Override public void setLoggedInTutor(Tutor t) {}
        @Override public Tutor getLoggedInTutor() { return null; }
        @Override public ArrayList<Tutor> getPendingTutors() { return new ArrayList<>(); }
        @Override public boolean createSession(Session s) { return false; }
        @Override public boolean editSession(Session s) { return false; }
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
