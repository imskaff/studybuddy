package server.model;

/**
 * Defines the contract for all states a {@link Session} can be in.
 * 
 * Implements the State design pattern. Each concrete state
 * ({@link Available}, {@link Book}, {@link Cancel}, {@link Complete})
 * decides which transitions are legal and which are silently rejected.
 * 
 *
 * @author
 */
public interface SessionState
{
    /**
     * Returns the display name of this state, used to persist the status in the database.
     *
     * @return the status name string (e.g. {@code "Available"}, {@code "Booked"})
     */
    String statusName();

    /**
     * Attempts to mark the session as complete.
     * Only allowed from the {@link Book} state when the end date has passed.
     *
     * @param session the session to transition
     */
    void complete(Session session);

    /**
     * Attempts to book the session.
     * Only allowed from the {@link Available} state.
     *
     * @param session the session to transition
     */
    void book(Session session);

    /**
     * Attempts to cancel the session.
     * Only allowed from the {@link Book} state.
     *
     * @param session the session to transition
     */
    void cancel(Session session);
}
