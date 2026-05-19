package server.dao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

public interface SessionDAO {
    Session create(String description, double price, String subject, String location, LocalDateTime start_date,
                   LocalDateTime end_date, Tutor tutor, Student student) throws SQLException;

    Session readByInt(int id) throws SQLException;

    void update(Session session) throws SQLException;

    void delete(Session session) throws SQLException;

    List<Session> readAll() throws SQLException;

    List<Session> readAvailable() throws SQLException;

    List<Session> readByStudent(int studentId) throws SQLException;

    List<String> readSubjects() throws SQLException;
}
