package server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=studybuddy";

        String user = "postgres";
        String password = "password";

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Connecting to: " + url);
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected successfully!");

            // Test: list all tables
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'studybuddy' ORDER BY table_name"
            );

            System.out.println("\nTables found:");
            while (rs.next()) {
                System.out.println("  - " + rs.getString("table_name"));
            }

            rs.close();
            stmt.close();
            connection.close();
            System.out.println("\nConnection closed. Everything works!");

        } catch (Exception e) {
            System.err.println("Connection FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
