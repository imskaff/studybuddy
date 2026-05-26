package server.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AvailableTest
{
    @Test
    void sessionInitialStateIsAvailable()
    {
        Session session = makeSession();

        assertInstanceOf(Available.class, session.getState());
    }

    @Test
    void bookChangesStateToBoo()
    {
        Session session = makeSession();

        new Available().book(session);

        assertInstanceOf(Book.class, session.getState());
    }

    @Test
    void completeLeavesStateUnchanged()
    {
        Session session = makeSession();

        new Available().complete(session);

        assertInstanceOf(Available.class, session.getState());
    }

    @Test
    void cancelLeavesStateUnchanged()
    {
        Session session = makeSession();

        new Available().cancel(session);

        assertInstanceOf(Available.class, session.getState());
    }

    private static Session makeSession()
    {
        Tutor tutor = new Tutor("John", "Doe", LocalDate.of(1985, 1, 1),
                "john@example.com", "111111111", "pass", "Male",
                new ArrayList<>(), "Master's", "tutor", 10);
        return new Session(1, "desc", 50.0, "Math", "Room",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2), tutor, null);
    }
}
