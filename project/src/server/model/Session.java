package server.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;

public class Session {
    private int id;
    private String description;
    private double price;
    private String subject;
    private String location;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Tutor tutor;
    private Student student;

    private boolean toBeCanceled;
    private boolean toBeRemoved;
    private transient SessionState state;
    private transient PropertyChangeSupport support;

    public Session(int id, String description, double price, String subject, String location, LocalDateTime start_date, LocalDateTime end_date, Tutor tutor, Student student) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.subject = subject;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.tutor = tutor;
        this.student = student;
        this.toBeCanceled = false;
        this.toBeRemoved = false;
        this.state = new Available();

        this.support = new PropertyChangeSupport(this);
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public SessionState getState() {
        return state;
    }

    public boolean isToBeCanceled()
    {
        return toBeCanceled;
    }

    public void setToBeCanceled(boolean toBeCanceled)
    {
        this.toBeCanceled = toBeCanceled;
    }

    public boolean isToBeRemoved()
    {
      return toBeRemoved;
    }

    public void setToBeRemoved(boolean toBeRemoved)
    {
      this.toBeRemoved = toBeRemoved;
    }

    public void setState(SessionState newState) {

        SessionState oldState = this.state;
        this.state = newState;

        support.firePropertyChange("state", oldState, newState);
    }

    public void complete(Session session)
    {
      state.complete(this);
    }

    public void book(Session session)
    {
      state.book(this);
    }

    public void cancel(Session session)
    {
      state.cancel(this);
    }

    public void remove(Session session)
    {
      state.remove(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        support.removePropertyChangeListener(listener);
    }
}
