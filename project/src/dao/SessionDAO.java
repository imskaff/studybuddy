package dao;

import model.Session;
import model.Student;
import model.Tutor;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface SessionDAO {
    Session create(String description, double price, String location, DateTimeFormatter start_date,
            DateTimeFormatter end_date, Tutor tutor, Student student) throws SQLException;

    Session readByInt(int id) throws SQLException;

    void update(Session session) throws SQLException;

    void delete(Session session) throws SQLException;

    List<Session> readAll() throws SQLException;
}
