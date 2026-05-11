package server.mediator;

public class BookSessionRequest {
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
