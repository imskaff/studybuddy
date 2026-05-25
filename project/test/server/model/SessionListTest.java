package server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SessionListTest
{
    private SessionList list;

    @BeforeEach
    void init()
    {
        list = new SessionList();
    }

    @Test
    public void listEmptyWhenNoSessionAvailable()
    {
        assertTrue(list.getAllSessions().isEmpty());
    }

    @Test
    void addSessionIncrementsSizeByOne()
    {
        list.addSession(makeSession(1));

        assertEquals(1, list.getSize());
    }

    @Test
    void getSessionByIdReturnsCorrectSession()
    {
        Session session = makeSession(42);
        list.addSession(session);

        assertEquals(session, list.getSessionById(42));
    }

    @Test
    void getSessionByIdWithNonExistentIdReturnsNull()
    {
        list.addSession(makeSession(1));

        assertNull(list.getSessionById(99));
    }

    @Test
    void removeSessionByIndexWhenOutOfBoundsThrowsException()
    {
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeSession(0));
    }

    @Test
    void removeSessionByIdRemovesOnlyTargetSession()
    {
        Session s1 = makeSession(1);
        Session s2 = makeSession(2);
        list.addSession(s1);
        list.addSession(s2);

        list.removeSessionById(1);

        assertEquals(1, list.getSize());
        assertNull(list.getSessionById(1));
        assertEquals(s2, list.getSessionById(2));
    }

    private static Session makeSession(int id)
    {
        Tutor tutor = new Tutor("John", "Doe", LocalDate.of(1985, 1, 1),
                "john@example.com", "111111111", "pass", "Male",
                new ArrayList<>(), "Master's", "tutor", 10);
        return new Session(id, "desc", 10.0, "Math", "Room",
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), tutor, null);
    }
}
