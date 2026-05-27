package server.model;

import java.time.LocalDateTime;

/**
 * Represents the {@code Complete} state of a {@link Session}.
 * 
 * A session reaches this state once it has been booked and its end date has
 * passed. No further transitions (book, cancel) are allowed from this state.
 * 
 *
 * @author Kamila Jeremicz
 */
public record Complete() implements SessionState
{
    /**
     * Returns the status name for this state.
     *
     * @return {@code "Complete"}
     */
    @Override public String statusName() { return "Complete"; }

    /**
     * Re-applies the complete state if the current time is at or after
     * the session's end date (no-op if the session is already complete).
     *
     * @param session the session on which the transition was attempted
     */
    @Override
    public void complete(Session session)
    {
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(session.getEnd_date()) || now.isEqual(session.getEnd_date()))
        {
            session.setState(this);
        }
    }

    /**
     * Booking a completed session is not permitted.
     * Prints a warning message and leaves the session unchanged.
     *
     * @param session the session on which the transition was attempted
     */
    @Override
    public void book(Session session)
    {
        System.out.println("Session cannot be booked if it's completed");
    }

    /**
     * Cancelling a completed session is not permitted.
     * Prints a warning message and leaves the session unchanged.
     *
     * @param session the session on which the transition was attempted
     */
    @Override
    public void cancel(Session session)
    {
        System.out.println("Session cannot be canceled if it's completed");
    }
}
