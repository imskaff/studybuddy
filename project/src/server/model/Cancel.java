package server.model;

/**
 * Represents the {@code Cancel} state of a {@link Session}.
 * 
 * A session reaches this state when a tutor cancels a booked session.
 * No further transitions are allowed from this state; the session is
 * effectively closed.
 * 
 *
 * @author Kamila Jeremicz
 * @author Volodymyr Neka
 */
public record Cancel() implements SessionState {

    /**
     * Returns the status name for this state.
     *
     * @return {@code "Cancel"}
     */
    @Override public String statusName() { return "Cancel"; }

    /**
     * Completing a cancelled session is not permitted.
     * Prints a warning message and leaves the session unchanged.
     *
     * @param session the session on which the transition was attempted
     */
    @Override
    public void complete(Session session)
    {
        System.out.println("Session cannot be completed if it was canceled");
    }

    /**
     * Booking a cancelled session is not permitted.
     * Prints a warning message and leaves the session unchanged.
     *
     * @param session the session on which the transition was attempted
     */
    @Override
    public void book(Session session)
    {
        System.out.println("Session cannot be booked if it was canceled");
    }

    /**
     * Re-applies the cancelled state (no-op — session stays cancelled).
     *
     * @param session the session on which the transition was attempted
     */
    @Override
    public void cancel(Session session)
    {
        session.setState(this);
    }
}
