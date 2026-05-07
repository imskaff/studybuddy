package server.temp;
//BookSessionRequest is a class that represents the booking data
// sent from the client to the server. It includes the student ID,
// session ID, date, and time, and it is converted to and from JSON
// using Gson in ClientHandler.
public class BookSessionRequest {
    private int studentId;
    // ID of the student who is booking the session

    private int sessionId;
    // ID of the session the student wants to book

    private String date;
    // Selected date for the session

    private String time;
    // Selected time for the session

    // Getters are needed so the server can read the values after converting from JSON
    public BookSessionRequest(int studentId, int sessionId, String date, String time)
    {
        this.studentId = studentId;
        this.sessionId = sessionId;
        this.date = date;
        this.time = time;
    }

    public int getStudentId()
    {
        return studentId;
    }

    public int getSessionId()
    {
        return sessionId;
    }

    public String getDate()
    {
        return date;
    }

    public String getTime()
    {
        return time;
    }

    @Override
    public String toString()
    {
        return "BOOK_SESSION{" +
                "studentId=" + studentId +
                ", sessionId=" + sessionId +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
