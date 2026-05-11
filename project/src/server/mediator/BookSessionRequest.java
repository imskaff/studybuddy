package server.mediator;

//BookSessionRequest is a class that represents the booking data
// sent from the client to the server. It includes the student ID,
// session ID, date, and time, and it is converted to and from JSON
// using Gson in ClientHandler.
public class BookSessionRequest {
    private final String type = "BOOK_SESSION";
    private int studentId;
    private int sessionId;
    private String date;
    private String time;

    public BookSessionRequest(int studentId, int sessionId, String date, String time) {
        this.studentId = studentId;
        this.sessionId = sessionId;
        this.date = date;
        this.time = time;
    }

    public String getType()    { return type; }
    public int getStudentId() { return studentId; }
    public int getSessionId() { return sessionId; }
    public String getDate()   { return date; }
    public String getTime()   { return time; }

    @Override
    public String toString() {
        return "BOOK_SESSION{studentId=" + studentId + ", sessionId=" + sessionId
                + ", date='" + date + "', time='" + time + "'}";
    }
}
