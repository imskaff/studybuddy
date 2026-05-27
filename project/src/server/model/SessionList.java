package server.model;

import java.util.ArrayList;

/**
 * An in-memory list of {@link Session} objects used by the server-side model.
 * 
 * Provides simple add, get and remove operations with an explicit
 * size counter maintained in parallel with the backing list.
 * 
 *
 * @author Adam Terelak
 */
public class SessionList {
    private ArrayList<Session> sessions;
    private int size;

    /**
     * Constructs an empty {@code SessionList}.
     */
    public SessionList() {
        sessions = new ArrayList<>();
        size = 0;
    }

    /**
     * Returns all sessions currently held in this list.
     *
     * @return the list of sessions
     */
    public ArrayList<Session> getAllSessions() {
        return sessions;
    }

    /**
     * Adds a session to this list and increments the size counter.
     *
     * @param session the session to add
     */
    public void addSession(Session session) {
        sessions.add(session);
        size++;
    }

    /**
     * Returns the session at the given index.
     *
     * @param index the zero-based index of the session to retrieve
     * @return the session at {@code index}
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Session getSession(int index) {
        return sessions.get(index);
    }

    /**
     * Returns the session with the given database ID, or {@code null} if not found.
     *
     * @param id the database identifier to search for
     * @return the matching session, or {@code null}
     */
    public Session getSessionById(int id) {
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getId() == id) {
                return sessions.get(i);
            }
        }
        return null;
    }

    /**
     * Returns the number of sessions in this list.
     *
     * @return the session count
     */
    public int getSize() {
        return size;
    }

    /**
     * Removes the session at the given index and decrements the size counter.
     *
     * @param index the zero-based index of the session to remove
     * @throws IndexOutOfBoundsException if the index is negative or &ge; the current size
     */
    public void removeSession(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Error. Index out of bounds.");
        }
        sessions.remove(index);
        size--;
    }

    /**
     * Removes the first session whose database ID matches the given value.
     * Does nothing if no matching session is found.
     *
     * @param id the database identifier of the session to remove
     */
    public void removeSessionById(int id) {
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getId() == id) {
                sessions.remove(i);
                size--;
                break;
            }
        }
    }
}
