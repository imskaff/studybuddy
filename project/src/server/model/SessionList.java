package server.model;

import java.util.ArrayList;

public class SessionList {
    private ArrayList<Session> sessions;
    private int size;

    public SessionList() {
        sessions = new ArrayList<>();
        size = 0;
    }

    public ArrayList<Session> getAllSessions() {
        return sessions;
    }

    public void addSession(Session session) {
        sessions.add(session);
        size++;
    }

    public Session getSession(int index) {
        return sessions.get(index);
    }

    public Session getSessionById(int id) {
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getId() == id) {
                return sessions.get(i);
            }
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    public void removeSession(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Error. Index out of bounds.");
        }
        sessions.remove(index);
        size--;
    }

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
