package system;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.dao.SessionDAOImpl;
import server.dao.UserDAOImpl;
import server.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SystemTest {

    private static ModelManager model;

    @BeforeAll
    static void setUp() throws Exception {
        UserDAOImpl userDAO = new UserDAOImpl();
        SessionDAOImpl sessionDAO = new SessionDAOImpl(userDAO);
        model = new ModelManager(sessionDAO, userDAO);
    }

    @Test
    void loginWithWrongCredentials() {
        User result = model.login("nonexistent@fake.com", "wrongpassword");
        assertNull(result);
    }

    @Test
    void registerStudentWithEmptyFields() {
        Student invalid = new Student("", "", null, "", "", "", "",
            new ArrayList<>(), 0, "Student");
        assertFalse(model.createStudentProfile(invalid));
    }

    @Test
    void createSessionWithNullFails() {
        assertFalse(model.createSession(null));
    }

    @Test
    void createSessionWithEmptyFieldsFails() {
        Session invalid = new Session("", 0, "", "",
            LocalDateTime.now(), LocalDateTime.now().plusHours(1), null);
        assertFalse(model.createSession(invalid));
    }

    @Test
    void getAvailableSessionsReturnsNonNull() {
        List<Session> sessions = model.getAvailableSessions();
        assertNotNull(sessions);
    }

    @Test
    void getSubjectsReturnsNonNull() {
        List<String> subjects = model.getSubjects();
        assertNotNull(subjects);
    }

    @Test
    void bookSessionWithInvalidIds() {
        assertFalse(model.bookSession(-999, -999));
    }

    @Test
    void cancelNonExistentSession() {
        assertFalse(model.cancelSession(-999, -999, "student"));
    }

    @Test
    void deleteNonExistentSession() {
        assertFalse(model.deleteSession(-999));
    }

    @Test
    void editSessionWithEmptyFields() {
        Session invalid = new Session(0, "", 0, "", "",
            LocalDateTime.now(), LocalDateTime.now().plusHours(1), null, null);
        assertFalse(model.editSession(invalid));
    }

    @Test
    void editStudentProfileWithEmptyFields() {
        Student invalid = new Student("", "", null, "", "", "", "",
            new ArrayList<>(), 0, "Student");
        assertFalse(model.editStudentProfile(invalid));
    }

    @Test
    void editTutorProfileWithEmptyFields() {
        Tutor invalid = new Tutor("", "", null, "", "", "", "",
            new ArrayList<>(), "", "Tutor", 0);
        assertFalse(model.editTutorProfile(invalid));
    }

    @Test
    void adminLoginWithWrongCredentials() {
        assertEquals(-1, model.getAdminId("wrong", "wrong"));
    }

    @Test
    void getPendingTutorsReturnsNonNull() {
        assertNotNull(model.getPendingTutors());
    }

    @Test
    void getTutorSessionsForInvalidTutor() {
        List<Session> sessions = model.getTutorSessions(-999);
        assertNotNull(sessions);
        assertTrue(sessions.isEmpty());
    }

    @Test
    void getBookedSessionsForInvalidStudent() {
        List<Session> sessions = model.getBookedSessions(-999);
        assertNotNull(sessions);
        assertTrue(sessions.isEmpty());
    }
}
