package server.mediator;

/**
 * Server response to a {@link LoginRequest}.
 * 
 * On success, carries the authenticated user's type (e.g. {@code "student"},
 * {@code "tutor"}, {@code "admin"}) and a JSON-serialised representation of
 * the user object (or the admin ID string for admin logins).
 * On failure, carries an error message.
 * 
 * 
 * Use the static factory methods {@link #ok(String, String)} and
 * {@link #error(String)} to construct instances.
 * 
 *
 * @author
 */
public class LoginResponse {
    private String status;
    private String userType;
    private String userJson;
    private String message;

    /**
     * Creates a successful login response.
     *
     * @param userType the authenticated user's type (e.g. {@code "student"}, {@code "tutor"}, {@code "admin"})
     * @param userJson a JSON string representing the user object, or the admin ID for admin logins
     * @return a {@code LoginResponse} with status {@code "OK"}
     */
    public static LoginResponse ok(String userType, String userJson) {
        LoginResponse r = new LoginResponse();
        r.status = "OK";
        r.userType = userType;
        r.userJson = userJson;
        return r;
    }

    /**
     * Creates a failed login response.
     *
     * @param message a human-readable description of why the login failed
     * @return a {@code LoginResponse} with status {@code "ERROR"}
     */
    public static LoginResponse error(String message) {
        LoginResponse r = new LoginResponse();
        r.status = "ERROR";
        r.message = message;
        return r;
    }

    /**
     * Returns the response status ({@code "OK"} or {@code "ERROR"}).
     *
     * @return the status string
     */
    public String getStatus()   { return status; }

    /**
     * Returns the authenticated user's type string.
     *
     * @return the user type, or {@code null} on error
     */
    public String getUserType() { return userType; }

    /**
     * Returns the JSON representation of the authenticated user.
     *
     * @return the user JSON string, or {@code null} on error
     */
    public String getUserJson() { return userJson; }

    /**
     * Returns the error message for a failed login.
     *
     * @return the error message, or {@code null} on success
     */
    public String getMessage()  { return message; }

    /**
     * Returns a string representation of this response for debugging purposes.
     *
     * @return a formatted string with all fields
     */
    @Override
    public String toString()
    {
        return "LoginResponse{" +
            "status='" + status + '\'' +
            ", userType='" + userType + '\'' +
            ", userJson='" + userJson + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
