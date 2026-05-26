package server.dao;

import org.junit.jupiter.api.Disabled;
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
    @Disabled("Integration — requires PostgreSQL running on localhost:5432")
    void readByInt_throwsSQLException_whenSessionNotFound() throws SQLException {
        SessionDAOImpl dao = new SessionDAOImpl(UserDAOImpl.getInstance());
        assertThrows(SQLException.class, () -> dao.readByInt(-999));
    }

    @Test
    @Disabled("Integration — requires PostgreSQL running on localhost:5432")
    void readAvailable_returnsNonNullList() throws SQLException {
        SessionDAOImpl dao = new SessionDAOImpl(UserDAOImpl.getInstance());
        assertNotNull(dao.readAvailable());
    }

    @Test
    @Disabled("Integration — requires PostgreSQL running on localhost:5432")
    void readByStudent_returnsEmptyList_forNonExistentStudent() throws SQLException {
        SessionDAOImpl dao = new SessionDAOImpl(UserDAOImpl.getInstance());
        assertTrue(dao.readByStudent(-999).isEmpty());
    }

    @Test
    @Disabled("Integration — requires PostgreSQL running on localhost:5432")
    void readByTutor_returnsEmptyList_forNonExistentTutor() throws SQLException {
        SessionDAOImpl dao = new SessionDAOImpl(UserDAOImpl.getInstance());
        assertTrue(dao.readByTutor(-999).isEmpty());
    }
}
