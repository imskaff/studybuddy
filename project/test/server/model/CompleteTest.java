package server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CompleteTest
{
    private Session session;

    @BeforeEach
    void setUp()
    {
        session = makeSession();
        session.setState(new Complete());
    }

    @Test
    void bookDoesNotChangeState()
    {
        new Complete().book(session);

        assertInstanceOf(Complete.class, session.getState());
    }

    @Test
    void cancelDoesNotMarkSessionToBeCanceled()
    {
        new Complete().cancel(session);

        assertFalse(session.isToBeCanceled());
    }

    @Test
    void cancelDoesNotChangeState()
    {
        new Complete().cancel(session);

        assertInstanceOf(Complete.class, session.getState());
    }

    @Test
    void removeMarksSessionToBeRemoved()
    {
        new Complete().remove(session);

        assertTrue(session.isToBeRemoved());
    }

    @Test
    void removeDoesNotChangeState()
    {
        new Complete().remove(session);

        assertInstanceOf(Complete.class, session.getState());
    }

    @Test
    void completeWhenNowIsAfterEndDateKeepsStateComplete()
    {
        LocalDateTime pastEnd = LocalDateTime.now().minusHours(1);
        session.setEnd_date(pastEnd);

        new Complete().complete(session);

        assertInstanceOf(Complete.class, session.getState());
    }

    @Test
    void completeWhenNowIsBeforeEndDateDoesNotChangeState()
    {
        LocalDateTime futureEnd = LocalDateTime.now().plusHours(2);
        session.setEnd_date(futureEnd);

        new Complete().complete(session);

        assertInstanceOf(Complete.class, session.getState());
    }

    private static Session makeSession()
    {
        Tutor tutor = new Tutor("John", "Doe", LocalDate.of(1985, 1, 1),
                "john@example.com", "111111111", "pass", "Male",
                new ArrayList<>(), "Master's", "tutor", 10);
        return new Session(1, "desc", 50.0, "Math", "Room",
                LocalDateTime.now().minusHours(3), LocalDateTime.now().minusHours(1), tutor, null);
    }
}
