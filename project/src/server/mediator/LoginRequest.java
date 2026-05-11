package server.mediator;

public class LoginRequest {
    private final String type = "LOGIN";
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getType()     { return type; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
}
