package server.model;

import org.junit.jupiter.api.*;
import server.dao.SessionDAO;
import server.dao.UserDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionModelManagerTest {

    private ModelManager manager;
    private Session mockSession;
    private Session existingSession;
    private Tutor mockTutor;
    private SessionDAO mockSessionDao;
    private UserDAO mockUserDao;

    @BeforeEach
    void setUp() {
        mockSessionDao  = mock(SessionDAO.class);
        mockUserDao     = mock(UserDAO.class);
        manager         = new ModelManager(mockSessionDao, mockUserDao);

        mockTutor       = mock(Tutor.class);
        mockSession     = mock(Session.class);
        existingSession = mock(Session.class);

        // Default valid-session stubs — tests override only what they need
        when(mockSession.getTutor()).thenReturn(mockTutor);
        when(mockSession.getDescription()).thenReturn("Valid Description");
        when(mockSession.getPrice()).thenReturn(50.0);
        when(mockSession.getLocation()).thenReturn("Library");
        when(mockSession.getSubject()).thenReturn("Mathematics");
        when(mockSession.getStart_date()).thenReturn(LocalDateTime.now());
        when(mockSession.getEnd_date()).thenReturn(LocalDateTime.now().plusHours(1));
        when(mockSession.getStudent()).thenReturn(null);
    }

    // --- createSession ---

    @Test
    void createSession_validData_returnsTrue() throws SQLException {
        // Arrange — fully covered by @BeforeEach

        boolean result = manager.createSession(mockSession);

        assertTrue(result);
    }

    @Test
    void createSession_negativePrice_returnsFalse() throws SQLException {
        when(mockSession.getPrice()).thenReturn(-10.0);

        boolean result = manager.createSession(mockSession);

        assertFalse(result);
    }

    @Test
    void createSession_zeroPrice_returnsFalse() throws SQLException {
        when(mockSession.getPrice()).thenReturn(0.0);

        boolean result = manager.createSession(mockSession);

        assertFalse(result);
    }

    // --- editSession ---

    @Test
    void editSession_validData_returnsTrue() throws Exception {
        when(mockSession.getId()).thenReturn(1);
        when(mockSession.getPrice()).thenReturn(60.0);
        when(mockSession.getDescription()).thenReturn("New Description");
        when(mockSession.getLocation()).thenReturn("Online");
        when(mockSessionDao.readByInt(1)).thenReturn(existingSession);

        boolean result = manager.editSession(mockSession);

        assertTrue(result);
    }

    @Test
    void editSession_negativePrice_returnsFalse() throws Exception {
        when(mockSession.getPrice()).thenReturn(-5.0);

        boolean result = manager.editSession(mockSession);

        assertFalse(result);
    }

    // --- deleteSession ---

    @Test
    void deleteSession_sessionExists_returnsTrue() throws Exception {
        when(mockSessionDao.readByInt(1)).thenReturn(existingSession);

        boolean result = manager.deleteSession(1);

        assertTrue(result);
    }

    @Test
    void deleteSession_sessionNotFound_returnsFalse() throws Exception {
        when(mockSessionDao.readByInt(99)).thenThrow(new SQLException("No session found"));

        boolean result = manager.deleteSession(99);

        assertFalse(result);
    }
}
