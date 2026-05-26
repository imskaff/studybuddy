package server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CancelTest
{
    private Session session;

    @BeforeEach
    void setUp()
    {
        session = makeSession();
        session.setState(new Cancel());
    }

    @Test
    void cancelKeepsStateAsCancel()
    {
        new Cancel().cancel(session);

        assertInstanceOf(Cancel.class, session.getState());
    }

    @Test
    void bookDoesNotChangeState()
    {
        new Cancel().book(session);

        assertInstanceOf(Cancel.class, session.getState());
    }

    @Test
    void completeDoesNotChangeState()
    {
        new Cancel().complete(session);

        assertInstanceOf(Cancel.class, session.getState());
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
