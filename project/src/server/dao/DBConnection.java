package server.dao;

import java.sql.Connection;

/**
 * Placeholder class holding the PostgreSQL connection constants.
 * 
 * Connection management is handled directly inside
 * {@link UserDAOImpl} and {@link SessionDAOImpl} via their own
 * {@code getConnection()} methods.
 * The class is kept for reference.
 *
 * @author Adam Terelak
 */
public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/studybuddy";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    private static Connection connection;
}
