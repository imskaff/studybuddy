package server.dao;

import java.sql.Connection;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/studybuddy";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    private static Connection connection;
}
