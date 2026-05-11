package server.model;

//import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionListTest {
    private SessionList list;

    @BeforeEach
    void init(){
            list = new SessionList();
        }

    @Test
    public void listEmptyWhenNoSessionAvailable(){
        ArrayList<Session> sessions = list.getAllSessions();

        assertTrue(sessions.isEmpty(), () -> "SessionList should be empty");
    }
}
