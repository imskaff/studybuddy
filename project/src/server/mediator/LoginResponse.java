package server.mediator;

public class LoginResponse {
    private String status;
    private String userType;
    private String userJson;
    private String message;

    public static LoginResponse ok(String userType, String userJson) {
        LoginResponse r = new LoginResponse();
        r.status = "OK";
        r.userType = userType;
        r.userJson = userJson;
        return r;
    }

    public static LoginResponse error(String message) {
        LoginResponse r = new LoginResponse();
        r.status = "ERROR";
        r.message = message;
        return r;
    }

    public String getStatus()   { return status; }
    public String getUserType() { return userType; }
    public String getUserJson() { return userJson; }
    public String getMessage()  { return message; }
}
