package server.dao;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class SessionDAOImplTest {

    @Test
    void constructor_doesNotThrow_withMockedUserDAO() {
        UserDAO mockUserDAO = mock(UserDAO.class);
        assertDoesNotThrow(() -> new SessionDAOImpl(mockUserDAO));
    }

    @Test
    void readByInt_throwsSQLException_whenSessionNotFound() throws SQLException {
        SessionDAOImpl dao = new SessionDAOImpl(UserDAOImpl.getInstance());
        assertThrows(SQLException.class, () -> dao.readByInt(-999));
    }

    @Test
    void readAvailable_returnsNonNullList() throws SQLException {
        SessionDAOImpl dao = new SessionDAOImpl(UserDAOImpl.getInstance());
        assertNotNull(dao.readAvailable());
    }

    @Test
    void readByStudent_returnsEmptyList_forNonExistentStudent() throws SQLException {
        SessionDAOImpl dao = new SessionDAOImpl(UserDAOImpl.getInstance());
        assertTrue(dao.readByStudent(-999).isEmpty());
    }

    @Test
    void readByTutor_returnsEmptyList_forNonExistentTutor() throws SQLException {
        SessionDAOImpl dao = new SessionDAOImpl(UserDAOImpl.getInstance());
        assertTrue(dao.readByTutor(-999).isEmpty());
    }
}
