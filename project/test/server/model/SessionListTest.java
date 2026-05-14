package server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionListTest {
    private SessionList list;

    @BeforeEach
    void init(){
            list = new SessionList();
        }

    @Test
    public void listEmptyWhenNoSessionAvailable(){
        ArrayList<Session> sessions = list.getAllSessions();

        assertTrue(sessions.isEmpty(), () -> "SessionList should be empty");
    }

    @Test
    void addSessionIncrementsSizeByOne() {
        Session session = mock(Session.class);

        list.addSession(session);

        assertEquals(1, list.getSize());
    }

    @Test
    void getSessionByIdReturnsCorrectSession() {
        Session session = mock(Session.class);
        when(session.getId()).thenReturn(42);
        list.addSession(session);

        Session result = list.getSessionById(42);

        assertEquals(session, result);
    }

    @Test
    void getSessionByIdWithNonExistentIdReturnsNull() {
        Session session = mock(Session.class);
        when(session.getId()).thenReturn(1);
        list.addSession(session);

        assertNull(list.getSessionById(99));
    }

    @Test
    void removeSessionByIndexWhenOutOfBoundsThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeSession(0));
    }

    @Test
    void removeSessionByIdRemovesOnlyTargetSession() {
        Session s1 = mock(Session.class);
        Session s2 = mock(Session.class);
        when(s1.getId()).thenReturn(1);
        when(s2.getId()).thenReturn(2);
        list.addSession(s1);
        list.addSession(s2);

        list.removeSessionById(1);

        assertEquals(1, list.getSize());
        assertNull(list.getSessionById(1));
        assertEquals(s2, list.getSessionById(2));
    }
}
