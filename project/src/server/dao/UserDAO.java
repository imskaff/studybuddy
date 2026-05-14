package server.dao;

import server.model.Tutor;
import server.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface UserDAO {
    User create(String first_name, String last_name, LocalDate date_of_birth, String email, String phone,
            String password, String gender, ArrayList<String> languages, String type) throws SQLException;

    User readByInt(int id) throws SQLException;

    void update(User user) throws SQLException;

    void delete(User user) throws SQLException;

    List<User> readAll() throws SQLException;

    User readByEmailAndPassword(String email, String password) throws SQLException;

    ArrayList<Tutor> readPendingTutors() throws SQLException;

    void updateApprovalStatus(int tutorId, boolean approved, int adminId) throws SQLException;

    void declineTutor(int tutorId) throws SQLException;

    int getAdminId(String username, String password) throws SQLException;

    User readByEmail(String email) throws SQLException;

    boolean updateTutorApproval(int tutorId, boolean approved) throws SQLException;
}
