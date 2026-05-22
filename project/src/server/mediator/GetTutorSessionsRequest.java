package server.mediator;

public class GetTutorSessionsRequest {
    private String type;
    private int tutorId;

    public GetTutorSessionsRequest(int tutorId) {
        this.type = "GET_TUTOR_SESSIONS";
        this.tutorId = tutorId;
    }

    public String getType() {
        return type;
    }

    public int getTutorId() {
        return tutorId;
    }
}
