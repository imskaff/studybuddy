package server.model;

import org.junit.jupiter.api.*;
import server.dao.SessionDAO;
import server.dao.UserDAO;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionModelManagerTest {

  private SessionModelManager manager;
  private Session mockSession;
  private Session existingSession;
  private Tutor mockTutor;
  private SessionDAO mockDao;
  private UserDAO mockUserDao;

  @BeforeEach
  void setUp() {
    mockDao = mock(SessionDAO.class);
    mockUserDao = mock(UserDAO.class);
    manager = new SessionModelManager(mockDao, mockUserDao);

    mockSession = mock(Session.class);
    existingSession = mock(Session.class);
    mockTutor = mock(Tutor.class);
  }

  @Nested
  @DisplayName("Tests for createSession()")
  class CreateSessionTests {

    @Test
    void validData_ReturnsTrue() throws SQLException
    {
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
      verify(mockDao, times(1)).create(
          anyString(),
          anyDouble(),
          anyString(),
          anyString(),
          any(java.time.LocalDateTime.class),
          any(java.time.LocalDateTime.class),
          any(Tutor.class),
          isNull()
      );
    }

    @Test
    void invalidData_NegativePrice_ReturnsFalse() throws SQLException
    {
      when(mockSession.getTutor()).thenReturn(mockTutor);
      when(mockSession.getDescription()).thenReturn("Valid Description");
      when(mockSession.getPrice()).thenReturn(-10.0);
      when(mockSession.getLocation()).thenReturn("Library");

      boolean result = manager.createSession(mockSession);

      assertFalse(result);
      verify(mockDao, never()).create(any(), anyDouble(), any(), any(), any(), any(), any(), any());
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
      when(mockDao.readByInt(sessionId)).thenReturn(existingSession);

      boolean result = manager.editSession(mockSession);

      assertTrue(result);
      verify(mockDao, times(1)).update(existingSession);
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
      verify(mockDao, never()).update(any(Session.class));
    }
  }

  @Nested
  @DisplayName("Tests for deleteSession()")
  class DeleteSessionTests {

    @Test
    void sessionExists_ReturnsTrue() throws Exception {
      int sessionId = 1;
      when(mockDao.readByInt(sessionId)).thenReturn(existingSession);

      boolean result = manager.deleteSession(sessionId);

      assertTrue(result);
      verify(mockDao, times(1)).delete(existingSession);
    }

    @Test
    void databaseThrowsException_ReturnsFalse() throws Exception {
      int sessionId = 1;
      when(mockDao.readByInt(sessionId)).thenReturn(existingSession);
      doThrow(new RuntimeException("DB Connection Lost")).when(mockDao).delete(existingSession);

      boolean result = manager.deleteSession(sessionId);

      assertFalse(result);
    }
  }
}