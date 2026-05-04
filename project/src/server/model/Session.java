package server.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Session {
    private int id;
    private String description;
    private double price;
    private String location;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Tutor tutor;
    private Student student;

    public Session(int id, String description, double price, String location, LocalDateTime start_date, LocalDateTime end_date, Tutor tutor, Student student) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.tutor = tutor;
        this.student = student;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
