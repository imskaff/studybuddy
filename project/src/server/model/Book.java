package server.model;

import java.time.LocalDateTime;

/**
 * Represents the {@code Booked} state of a {@link Session}.
 * 
 * A session in this state has been reserved by a student. It can be
 * completed once its end date has passed, or cancelled by either party.
 * 
 *
 * @author Kamila Jeremicz
 */
public record Book() implements SessionState
{
    /**
     * Returns the status name for this state.
     *
     * @return {@code "Booked"}
     */
    @Override public String statusName() { return "Booked"; }

    /**
     * Attempts to complete the session using the current system time.
     * Delegates to {@link #complete(Session, LocalDateTime)}.
     *
     * @param session the session to complete
     */
    @Override
    public void complete(Session session)
    {
        complete(session, LocalDateTime.now());
    }

    /**
     * Completes the session if the given time is at or after the session's end date.
     * Transitions the session to {@link Complete} if the condition is met.
     *
     * @param session the session to complete
     * @param now     the reference time to compare against the session's end date
     */
    void complete(Session session, LocalDateTime now)
    {
        if (now.isAfter(session.getEnd_date()) || now.isEqual(session.getEnd_date()))
        {
            session.setState(new Complete());
        }
    }

    /**
     * Re-applies the booked state (no-op transition — session stays booked).
     *
     * @param session the session on which the transition was attempted
     */
    @Override
    public void book(Session session)
    {
        session.setState(this);
    }

    /**
     * Cancels the session by transitioning it to the {@link Cancel} state.
     *
     * @param session the session to cancel
     */
    @Override
    public void cancel(Session session)
    {
        session.setState(new Cancel());
    }
}
