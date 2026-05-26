package server.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.User;
import server.model.Tutor;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOImplTest {

    private UserDAOImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        dao = UserDAOImpl.getInstance();
    }

    @Test
    void getInstance_doesNotThrow() {
        assertDoesNotThrow(UserDAOImpl::getInstance);
    }

    @Test
    void getInstance_returnsSameInstance() throws SQLException {
        UserDAOImpl a = UserDAOImpl.getInstance();
        UserDAOImpl b = UserDAOImpl.getInstance();
        assertSame(a, b);
    }

    @Test
    void readByInt_throwsSQLException_whenUserNotFound() {
        assertThrows(SQLException.class, () -> dao.readByInt(-999));
    }

    @Test
    void readByEmail_returnsNull_whenEmailDoesNotExist() throws SQLException {
        User result = dao.readByEmail("nonexistent_xyz_@nowhere.com");
        assertNull(result);
    }

    @Test
    void readByEmailAndPassword_returnsNull_whenCredentialsDoNotMatch() throws SQLException {
        User result = dao.readByEmailAndPassword("nonexistent_xyz_@nowhere.com", "wrongpassword");
        assertNull(result);
    }

    @Test
    void readPendingTutors_returnsNonNullList() throws SQLException {
        ArrayList<Tutor> result = dao.readPendingTutors();
        assertNotNull(result);
    }

    @Test
    void getAdminId_returnsMinusOne_whenCredentialsDoNotMatch() throws SQLException {
        int result = dao.getAdminId("nonexistent_admin", "wrongpassword");
        assertEquals(-1, result);
    }

    @Test
    void readAll_returnsNonNullList() throws SQLException {
        assertNotNull(dao.readAll());
    }
}
