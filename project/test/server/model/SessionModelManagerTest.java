package server.model;

import org.junit.jupiter.api.*;
import server.dao.SessionDAO;
import server.dao.UserDAO;

import java.sql.SQLException;

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
    mockSessionDao = mock(SessionDAO.class);
    mockUserDao = mock(UserDAO.class);
    manager = new ModelManager(mockSessionDao, mockUserDao);

    mockSession = mock(Session.class);
    existingSession = mock(Session.class);
    mockTutor = mock(Tutor.class);
  }

  @Nested
  @DisplayName("Tests for createSession()")
  class CreateSessionTests {

    @Test
    void validDataReturnsTrue() throws SQLException {
      when(mockSession.getTutor()).thenReturn(mockTutor);
      when(mockSession.getDescription()).thenReturn("Valid Description");
      when(mockSession.getPrice()).thenReturn(50.0);
      when(mockSession.getLocation()).thenReturn("Library");
      when(mockSession.getSubject()).thenReturn("Mathematics");
      when(mockSession.getStart_date()).thenReturn(java.time.LocalDateTime.now());
      when(mockSession.getEnd_date()).thenReturn(java.time.LocalDateTime.now().plusHours(1));
      when(mockSession.getStudent()).thenReturn(null);

      boolean result = manager.createSession(mockSession);

      assertTrue(result);
    }

    @Test
    void invalidDataNegativePriceReturnsFalse() throws SQLException {
      when(mockSession.getTutor()).thenReturn(mockTutor);
      when(mockSession.getDescription()).thenReturn("Valid Description");
      when(mockSession.getPrice()).thenReturn(-10.0);
      when(mockSession.getLocation()).thenReturn("Library");

      boolean result = manager.createSession(mockSession);

      assertFalse(result);
    }

    @Test
    void invalidData_ZeroPrice_ReturnsFalse() throws SQLException {
      when(mockSession.getTutor()).thenReturn(mockTutor);
      when(mockSession.getDescription()).thenReturn("Valid Description");
      when(mockSession.getPrice()).thenReturn(0.0);
      when(mockSession.getLocation()).thenReturn("Library");

      boolean result = manager.createSession(mockSession);

      assertFalse(result);
    }
  }

  @Nested
  @DisplayName("Tests for editSession()")
  class EditSessionTests {

    @Test
    void validUpdate_ReturnsTrue() throws Exception {
      int sessionId = 1;
      when(mockSession.getId()).thenReturn(sessionId);
      when(mockSession.getTutor()).thenReturn(mockTutor);
      when(mockSession.getDescription()).thenReturn("New Description");
      when(mockSession.getPrice()).thenReturn(60.0);
      when(mockSession.getLocation()).thenReturn("Online");
      when(mockSessionDao.readByInt(sessionId)).thenReturn(existingSession);

      boolean result = manager.editSession(mockSession);

      assertTrue(result);
      verify(existingSession).setDescription("New Description");
      verify(existingSession).setPrice(60.0);
      verify(existingSession).setLocation("Online");
    }

    @Test
    void invalidData_ReturnsFalse() throws Exception {
      when(mockSession.getTutor()).thenReturn(mockTutor);
      when(mockSession.getDescription()).thenReturn("New Description");
      when(mockSession.getPrice()).thenReturn(-5.0);

      boolean result = manager.editSession(mockSession);

      assertFalse(result);
    }
  }

  @Nested
  @DisplayName("Tests for deleteSession()")
  class DeleteSessionTests {

    @Test
    void sessionExists_ReturnsTrue() throws Exception {
      int sessionId = 1;
      when(mockSessionDao.readByInt(sessionId)).thenReturn(existingSession);

      boolean result = manager.deleteSession(sessionId);

      assertTrue(result);
    }

    @Test
    void databaseThrowsException_ReturnsFalse() throws Exception {
      int sessionId = 1;
      when(mockSessionDao.readByInt(sessionId)).thenReturn(existingSession);
      doThrow(new RuntimeException("DB Connection Lost")).when(mockSessionDao).delete(existingSession);

      boolean result = manager.deleteSession(sessionId);

      assertFalse(result);
    }

    @Test
    void sessionNotFound_ReturnsFalse() throws Exception {
      int sessionId = 99;
      when(mockSessionDao.readByInt(sessionId)).thenThrow(new java.sql.SQLException("No session found"));

      boolean result = manager.deleteSession(sessionId);

      assertFalse(result);
    }
  }
}
