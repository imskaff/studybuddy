package server.dao;

import server.model.Tutor;
import server.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object interface for {@link User} persistence.
 * 
 * Covers both {@link server.model.Student} and {@link Tutor} records,
 * as well as administrator authentication and tutor approval operations.
 * 
 * @author Adam Terelak
 */
public interface UserDAO {

    /**
     * Inserts a new user (student or tutor) into the database and returns the created entity.
     *
     * @param first_name     the user's first name
     * @param last_name      the user's last name
     * @param date_of_birth  the user's date of birth
     * @param email          the user's email address
     * @param phone          the user's phone number
     * @param password       the user's password
     * @param gender         the user's gender
     * @param languages      the list of languages spoken by the user
     * @param type           the user type ({@code "Student"} or {@code "Tutor"})
     * @param educationLevel the tutor's education level; {@code null} for students
     * @return the newly created {@link User} with its generated database ID
     * @throws SQLException if the insert fails
     */
    User create(String first_name, String last_name, LocalDate date_of_birth, String email, String phone,
            String password, String gender, ArrayList<String> languages, String type, String educationLevel) throws SQLException;

    /**
     * Fetches a single user by their database ID.
     *
     * @param id the user ID to look up
     * @return the matching {@link User} (cast to {@link server.model.Student} or {@link Tutor})
     * @throws SQLException if no user is found or a database error occurs
     */
    User readByInt(int id) throws SQLException;

    /**
     * Persists updated user information to the database.
     * Also replaces the user's language records and, for tutors,
     * updates the {@code tutors} table.
     *
     * @param user the user to update (identified by ID)
     * @throws SQLException if the update fails
     */
    void update(User user) throws SQLException;

    /**
     * Deletes the given user from the database.
     *
     * @param user the user to delete (identified by ID)
     * @throws SQLException if the deletion fails
     */
    void delete(User user) throws SQLException;

    /**
     * Returns all users in the database.
     *
     * @return a list of all users
     * @throws SQLException if the query fails
     */
    List<User> readAll() throws SQLException;

    /**
     * Fetches a user by matching both email and password.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return the matching {@link User}, or {@code null} if no match is found
     * @throws SQLException if the query fails
     */
    User readByEmailAndPassword(String email, String password) throws SQLException;

    /**
     * Returns all tutors whose registration is pending administrator approval.
     *
     * @return a list of unapproved {@link Tutor} objects
     * @throws SQLException if the query fails
     */
    ArrayList<Tutor> readPendingTutors() throws SQLException;

    /**
     * Updates a tutor's approval status and records which administrator approved them.
     *
     * @param tutorId  the ID of the tutor to approve
     * @param approved {@code true} to approve; {@code false} to revoke approval
     * @param adminId  the ID of the administrator performing the action
     * @throws SQLException if the update fails
     */
    void updateApprovalStatus(int tutorId, boolean approved, int adminId) throws SQLException;

    /**
     * Permanently removes a tutor registration from the {@code tutors} table.
     * Used when an administrator declines a pending registration.
     *
     * @param tutorId the ID of the tutor to decline
     * @throws SQLException if the deletion fails
     */
    void declineTutor(int tutorId) throws SQLException;

    /**
     * Looks up an administrator by credentials and returns their ID.
     *
     * @param username the administrator's username
     * @param password the administrator's password
     * @return the administrator's ID, or {@code -1} if no match is found
     * @throws SQLException if the query fails
     */
    int getAdminId(String username, String password) throws SQLException;

    /**
     * Fetches a user by their email address alone.
     * Used to check for duplicate email addresses during registration.
     *
     * @param email the email address to search for
     * @return the matching {@link User}, or {@code null} if no match is found
     * @throws SQLException if the query fails
     */
    User readByEmail(String email) throws SQLException;

    /**
     * Updates the approval flag for a tutor without recording an admin ID.
     *
     * @param tutorId  the ID of the tutor
     * @param approved the new approval value
     * @return {@code true} if one or more rows were updated; {@code false} otherwise
     * @throws SQLException if the update fails
     */
    boolean updateTutorApproval(int tutorId, boolean approved) throws SQLException;
}
