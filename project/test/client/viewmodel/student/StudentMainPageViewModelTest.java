package client.viewmodel.student;

import client.model.Model;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

        assertTrue(viewModel.getSessions().isEmpty());
    }

    // --- viewing booked sessions ---

    @Test
    void checkIfBookedListPopulatesFromModel() {
        Session booked = makeSession(3, "Chemistry");
        when(mockModel.getBookedSessions()).thenReturn(new ArrayList<>(List.of(booked)));

        viewModel.loadBookedSessions();

        ObservableList<Session> bookedSessions = viewModel.getBookedSessions();
        assertEquals(1, bookedSessions.size());
        assertTrue(bookedSessions.contains(booked));
    }

    @Test
    void checkIfBookedListIsEmptyWhenModelReturnsEmptyList() {
        when(mockModel.getBookedSessions()).thenReturn(new ArrayList<>());

        viewModel.loadBookedSessions();

        assertTrue(viewModel.getBookedSessions().isEmpty());
    }

    // --- booking moves session to booked list ---

    @Test
    void sessionBookedEventRemovesSessionFromAvailableList() throws InterruptedException {
        Session session = makeSession(1, "Math");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(session)));
        viewModel.loadSessions();

        viewModel.propertyChange(new PropertyChangeEvent(this, "SessionBooked", null, session));
        Thread.sleep(300);

        assertFalse(viewModel.getSessions().contains(session));
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

        assertTrue(viewModel.getBookedSessions().contains(session));
    }

    @Test
    void sessionBookedEventDoesNotAffectOtherSessions() throws InterruptedException {
        Session s1 = makeSession(1, "Math");
        Session s2 = makeSession(2, "Physics");
        when(mockModel.getAllSessions()).thenReturn(new ArrayList<>(List.of(s1, s2)));
        viewModel.loadSessions();

        viewModel.propertyChange(new PropertyChangeEvent(this, "SessionBooked", null, s1));
        Thread.sleep(300);

        assertFalse(viewModel.getSessions().contains(s1));
        assertTrue(viewModel.getSessions().contains(s2));
    }

    // --- helpers ---

    private static Session makeSession(int id, String subject) {
        Tutor tutor = new Tutor("John", "Doe", LocalDate.of(1985, 1, 1),
                "john@example.com", "111111111", "pass", "Male",
                new ArrayList<>(), "Master's", "tutor", 10);
        return new Session(id, "desc", 50.0, subject, "Room A",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1),
                tutor, null);
    }
}
