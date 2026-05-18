package client.viewmodel.student;

import client.model.Model;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import server.model.Session;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentMainPageViewModelTest {

    private Model mockModel;
    private StudentMainPageViewModel viewModel;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
        Thread.sleep(100);
    }

    @BeforeEach
    void setUp() {
        mockModel = mock(Model.class);
        viewModel = new StudentMainPageViewModel(mockModel);
    }

    // --- viewing sessions ---

    @Test
    void checkIfListPopulatesFromModel() {
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
    void checkIfListIsEmptyWhenModelReturnsEmptyList() {
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>());

        viewModel.loadSessions();

        assertTrue(viewModel.getSessions().isEmpty(), () -> "Sessions list should be empty");
    }

    // --- viewing booked sessions ---

    @Test
    void checkIfBookedListPopulatesFromModel() {
        Session booked = makeSession(3, "Chemistry");
        when(mockModel.getBookedSessions()).thenReturn(new ArrayList<>(List.of(booked)));

        viewModel.loadBookedSessions();

        ObservableList<Session> bookedSessions = viewModel.getBookedSessions();
        assertEquals(1, bookedSessions.size());
        assertTrue(bookedSessions.contains(booked), () -> "Booked sessions list should contain the session returned by the model");
    }

    @Test
    void checkIfBookedListIsEmptyWhenModelReturnsEmptyList() {
        when(mockModel.getBookedSessions()).thenReturn(new ArrayList<>());

        viewModel.loadBookedSessions();

        assertTrue(viewModel.getBookedSessions().isEmpty(), () -> "Booked sessions list should be empty");
    }

    // --- booking moves session to booked list ---

    @Test
    void sessionBookedEventRemovesSessionFromAvailableList() throws InterruptedException {
        Session session = makeSession(1, "Math");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(session)));
        viewModel.loadSessions();

        viewModel.propertyChange(new PropertyChangeEvent(this, "SessionBooked", null, session));
        Thread.sleep(300);

        assertFalse(viewModel.getSessions().contains(session), () -> "Booked session should be removed from the available list");
    }

    @Test
    void sessionBookedEventAddsSessionToBookedList() throws InterruptedException {
        Session session = makeSession(1, "Math");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(session)));
        when(mockModel.getBookedSessions()).thenReturn(new ArrayList<>());
        viewModel.loadSessions();
        viewModel.loadBookedSessions();

        viewModel.propertyChange(new PropertyChangeEvent(this, "SessionBooked", null, session));
        Thread.sleep(300);

        assertTrue(viewModel.getBookedSessions().contains(session), () -> "Booked session should be added to the booked list");
    }

    @Test
    void sessionBookedEventDoesNotAffectOtherSessions() throws InterruptedException {
        Session s1 = makeSession(1, "Math");
        Session s2 = makeSession(2, "Physics");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(s1, s2)));
        viewModel.loadSessions();

        viewModel.propertyChange(new PropertyChangeEvent(this, "SessionBooked", null, s1));
        Thread.sleep(300);

        assertFalse(viewModel.getSessions().contains(s1), () -> "Booked session should be removed from the available list");
        assertTrue(viewModel.getSessions().contains(s2), () -> "Only the booked session should be removed from the available list");
    }

    // --- filter tests ---

    @Test
    void subjectFilterShowsOnlyMatchingSubject() {
        loadFilterSessions();
        viewModel.subjectFilterProperty().set("Math");

        var result = viewModel.getFilteredSessions();
        assertEquals(1, result.size());
        assertEquals("Math", result.get(0).getSubject());
    }

    @Test
    void subjectFilterWhenAllSelectedShowsEverything() {
        loadFilterSessions();
        viewModel.subjectFilterProperty().set("Math");
        viewModel.subjectFilterProperty().set("All");

        assertEquals(3, viewModel.getFilteredSessions().size());
    }

    @Test
    void subjectFilterWhenNoMatchShowsNothing() {
        loadFilterSessions();
        viewModel.subjectFilterProperty().set("Biology");

        assertTrue(viewModel.getFilteredSessions().isEmpty());
    }

    @Test
    void minPriceFilterHidesCheaperSessions() {
        loadFilterSessions();
        viewModel.minPriceProperty().set("55");

        var result = viewModel.getFilteredSessions();
        assertEquals(1, result.size());
        assertEquals(80.0, result.get(0).getPrice());
    }

    @Test
    void maxPriceFilterHidesExpensiveSessions() {
        loadFilterSessions();
        viewModel.maxPriceProperty().set("45");

        var result = viewModel.getFilteredSessions();
        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getPrice());
    }

    @Test
    void priceRangeFilterShowsOnlySessionsWithinRange() {
        loadFilterSessions();
        viewModel.minPriceProperty().set("30");
        viewModel.maxPriceProperty().set("60");

        var result = viewModel.getFilteredSessions();
        assertEquals(1, result.size());
        assertEquals(50.0, result.get(0).getPrice());
    }

    @Test
    void invalidPriceInputIsIgnoredAndShowsAllSessions() {
        loadFilterSessions();
        viewModel.minPriceProperty().set("test");

        assertEquals(3, viewModel.getFilteredSessions().size());
    }

    @Test
    void fromDateFilterHidesSessionsBeforeFromDate() {
        loadFilterSessions();
        viewModel.fromDateProperty().set("01/03/2026");

        assertEquals(2, viewModel.getFilteredSessions().size());
    }

    @Test
    void toDateFilterHidesSessionsAfterToDate() {
        loadFilterSessions();
        viewModel.toDateProperty().set("01/05/2026");

        assertEquals(2, viewModel.getFilteredSessions().size());
    }

    @Test
    void invalidDateInputIsIgnoredAndShowsAllSessions() {
        loadFilterSessions();
        viewModel.fromDateProperty().set("test");

        assertEquals(3, viewModel.getFilteredSessions().size());
    }

    @Test
    void resetFiltersButtonClearsAllFiltersAndShowsAll() {
        loadFilterSessions();
        viewModel.subjectFilterProperty().set("Math");
        viewModel.minPriceProperty().set("60");
        viewModel.fromDateProperty().set("01/05/2026");

        viewModel.resetFilters();

        assertEquals(3, viewModel.getFilteredSessions().size());
    }

    @Test
    void combinedFiltersApplyAndLogic() {
        loadFilterSessions();
        viewModel.minPriceProperty().set("40");
        viewModel.toDateProperty().set("01/05/2026");

        // there is one session that matches both filters (Chemistry)
        var result = viewModel.getFilteredSessions();
        assertEquals(1, result.size());
        assertEquals("Chemistry", result.get(0).getSubject());
    }

    // --- helpers ---

    private void loadFilterSessions() {
        Session math    = makeSession(1, "Math",      20.0, LocalDateTime.of(2026, 2, 1, 10, 0));
        Session physics = makeSession(2, "Physics",   80.0, LocalDateTime.of(2026, 6, 1, 10, 0));
        Session chem    = makeSession(3, "Chemistry", 50.0, LocalDateTime.of(2026, 4, 1, 10, 0));
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(math, physics, chem)));
        viewModel.loadSessions();
    }

    private static Session makeSession(int id, String subject) {
        return makeSession(id, subject, 50.0, LocalDateTime.now().plusDays(1));
    }

    private static Session makeSession(int id, String subject, double price, LocalDateTime startDate) {
        Tutor tutor = new Tutor("John", "Doe", LocalDate.of(1985, 1, 1),
                "john@example.com", "111111111", "pass", "Male",
                new ArrayList<>(), "Master's", "tutor", 10);
        return new Session(id, "desc", price, subject, "Room A",
                startDate, startDate.plusHours(1),
                tutor, null);
    }
}
