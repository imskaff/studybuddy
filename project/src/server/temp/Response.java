package server.temp;
//The Response class represents the server’s reply to the client.
// It contains a status (OK or ERROR) and a message explaining the result.
// It is converted to and from JSON using Gson in the ClientHandler.
public class Response {
    private String status;
    // Represents the result of the request (e.g., "OK" or "ERROR")

    private String message;
    // Provides additional information about the result

    public Response(String status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public String getStatus()
    {
        return status;
    }

    public String getMessage()
    {
        return message;
    }
    // Getters allow the client to read the values after JSON conversion

    @Override
    public String toString()
    {
        return "Response{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
