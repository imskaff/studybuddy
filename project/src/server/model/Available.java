package server.model;

/**
 * Represents the {@code Available} state of a {@link Session}.
 * 
 * A session in this state has not been booked by any student and is visible
 * to all students for booking. The only valid transition out of this state
 * is to {@link Book} via {@link #book(Session)}.
 * 
 * @author Kamila Jeremicz
 */
public record Available() implements SessionState
{
    /**
     * Returns the status name for this state.
     *
     * @return {@code "Available"}
     */
    @Override public String statusName() { return "Available"; }

    /**
     * Completing an available session is not permitted.
     * Prints a warning message and leaves the session unchanged.
     *
     * @param session the session on which the transition was attempted
     */
    @Override public void complete(Session session)
    {
        System.out.println("Session cannot be completed if it's not booked");
    }

    /**
     * Books the session by transitioning it to the {@link Book} state.
     *
     * @param session the session to book
     */
    @Override public void book(Session session)
    {
        session.setState(new Book());
    }

    /**
     * Cancelling an available session is not permitted.
     * Prints a warning message and leaves the session unchanged.
     *
     * @param session the session on which the transition was attempted
     */
    @Override public void cancel(Session session)
    {
        System.out.println("Session cannot be canceled if it wasn't booked");
    }
}
