package server.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BookedTest
{
    private static final LocalDateTime END_DATE = LocalDateTime.of(2026, 5, 20, 10, 0);

    @Test
    void whenNowIsAfterEndDate_setStateToComplete()
    {
        Session session = makeSession(END_DATE);
        LocalDateTime fixedNow = LocalDateTime.of(2026, 5, 20, 12, 0);

        new Book().complete(session, fixedNow);

        assertInstanceOf(Complete.class, session.getState());
    }

    @Test
    void whenNowIsEqualEndDate_setStateToComplete()
    {
        Session session = makeSession(END_DATE);

        new Book().complete(session, END_DATE);

        assertInstanceOf(Complete.class, session.getState());
    }

    @Test
    void whenNowIsBeforeEndDate_shouldNotChangeState()
    {
        Session session = makeSession(END_DATE);
        LocalDateTime fixedNow = LocalDateTime.of(2026, 5, 19, 10, 0);

        new Book().complete(session, fixedNow);

        assertFalse(session.getState() instanceof Complete);
    }

    private static Session makeSession(LocalDateTime endDate)
    {
        Tutor tutor = new Tutor("John", "Doe", LocalDate.of(1985, 1, 1),
                "john@example.com", "111111111", "pass", "Male",
                new ArrayList<>(), "Master's", "tutor", 10);
        return new Session(1, "desc", 10.0, "Math", "Room",
                endDate.minusHours(2), endDate, tutor, null);
    }
}
