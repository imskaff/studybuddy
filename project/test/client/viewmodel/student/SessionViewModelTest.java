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

class SessionViewModelTest {

    private Model mockModel;
    private SessionViewModel viewModel;

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 1, 10, 0);
    private static final LocalDateTime END   = LocalDateTime.of(2026, 6, 1, 12, 0);

    @BeforeEach
    void setUp() {
        mockModel = mock(Model.class);
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>());
        viewModel = new SessionViewModel(mockModel);
    }

    // --- viewing session details ---

    @Test
    void loadSessionSetsSubjectProperty() {
        Session session = makeSession(1, "Mathematics");

        viewModel.loadSession(session);

        assertEquals("Mathematics", viewModel.subjectProperty().get());
    }

    @Test
    void loadSessionSetsDescriptionProperty() {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals("Intro to calculus", viewModel.descriptionProperty().get());
    }

    @Test
    void loadSessionSetsTutorNameProperty() {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals("John Doe", viewModel.tutorProperty().get());
    }

    @Test
    void loadSessiosSetsFormattedDates() {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals("01/06/2026 10:00", viewModel.startDateProperty().get());
        assertEquals("01/06/2026 12:00", viewModel.endDateProperty().get());
    }

    @Test
    void loadSessionSetsPriceProperty() {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals("75.0", viewModel.priceProperty().get());
    }

    @Test
    void loadSessionStoresSelectedSession() {
        Session session = makeSession(1, "Math");

        viewModel.loadSession(session);

        assertEquals(session, viewModel.getSelectedSession());
    }

    // --- viewing tutor profile through session details ---

    @Test
    void getSelectedTutorReturnsTutorFromSelectedSession() {
        Session session = makeSession(1, "Math");
        viewModel.loadSession(session);

        Tutor tutor = viewModel.getSelectedTutor();

        assertNotNull(tutor);
        assertEquals("John", tutor.getFirst_name());
        assertEquals("Doe", tutor.getLast_name());
    }

    @Test
    void checkIfGetSelectedTutorWhenNoSessionLoadedReturnsNull() {
        assertNull(viewModel.getSelectedTutor());
    }

    // --- booking session ---

    @Test
    void bookSessionCallsModelWithCorrectStudentAndSession() throws Exception {
        Session session = makeSession(1, "Math");
        Student student = makeStudent(5);
        when(mockModel.getLoggedInStudent()).thenReturn(student);
        viewModel.loadSession(session);

        viewModel.bookSession();

        verify(mockModel, times(1)).bookSession(student, session.getTutor(), session);
    }

    // --- helpers ---

    private static Tutor makeTutor() {
        return new Tutor("John", "Doe", LocalDate.of(1985, 1, 1),
                "john@example.com", "111111111", "pass", "Male",
                new ArrayList<>(), "Master's", "tutor", 10);
    }

    private static Session makeSession(int id, String subject) {
        return new Session(id, "Intro to calculus", 75.0, subject, "Room B",
                START, END, makeTutor(), null);
    }

    private static Student makeStudent(int id) {
        return new Student("Anna", "Brown", LocalDate.of(2000, 3, 15),
                "anna@example.com", "222222222", "pass", "Female",
                new ArrayList<>(), id, "student");
    }
}
