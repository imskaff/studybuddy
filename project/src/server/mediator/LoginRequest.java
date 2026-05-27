package server.mediator;

/**
 * Request sent from the client to authenticate a user by email and password.
 *
 * @author
 */
public class LoginRequest {
    private String type = "LOGIN";
    private String email;
    private String password;

    /**
     * Constructs a {@code LoginRequest} with the given credentials.
     *
     * @param email    the user's email address
     * @param password the user's password
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "LOGIN"}
     */
    public String getType()     { return type; }

    /**
     * Returns the email address.
     *
     * @return the email
     */
    public String getEmail()    { return email; }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public String getPassword() { return password; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with email and password
     */
    @Override
    public String toString()
    {
        return "LOGIN{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
