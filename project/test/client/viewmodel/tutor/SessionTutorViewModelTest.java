package client.viewmodel.tutor;

import client.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Session;
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
}
