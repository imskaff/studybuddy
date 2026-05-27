package server.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;

/**
 * Represents a tutoring session in the system.
 * 
 * A session is created by a {@link Tutor} and can be booked by a {@link Student}.
 * The lifecycle of a session is managed by the State pattern through
 * {@link SessionState} implementations ({@link Available}, {@link Book},
 * {@link Cancel}, {@link Complete}).
 * 
 * 
 * The {@code state} and {@code support} fields are marked {@code transient}
 * so that Gson excludes them during JSON serialisation.
 * 
 *
 * @author
 */
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

    private String status;

    private transient SessionState state;

    private transient PropertyChangeSupport support;

    /**
     * Constructs a fully populated {@code Session} with a known database ID.
     * The initial state is set to {@link Available}.
     *
     * @param id          the unique database identifier
     * @param description a short description of the session content
     * @param price       the price charged for the session
     * @param subject     the subject covered in the session
     * @param location    the physical or virtual location of the session
     * @param start_date  the date and time the session starts
     * @param end_date    the date and time the session ends
     * @param tutor       the tutor who owns the session
     * @param student     the student who has booked the session, or {@code null} if unbooked
     */
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

        this.state = new Available();
        this.status = "Available";

        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Constructs a new {@code Session} without a database ID, used when creating
     * a session that has not yet been persisted. The initial state is {@link Available}.
     *
     * @param description a short description of the session content
     * @param price       the price charged for the session
     * @param subject     the subject covered in the session
     * @param location    the physical or virtual location of the session
     * @param start_date  the date and time the session starts
     * @param end_date    the date and time the session ends
     * @param tutor       the tutor who owns the session
     */
    public Session(String description, double price, String subject, String location, LocalDateTime start_date, LocalDateTime end_date, Tutor tutor)
    {
        this.description = description;
        this.price = price;
        this.subject = subject;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.tutor = tutor;

        this.state = new Available();
        this.status = "Available";
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Returns the unique database identifier of this session.
     *
     * @return the session ID
     */
    public int getId() { return id; }

    /**
     * Returns the subject covered in this session.
     *
     * @return the subject name
     */
    public String getSubject() { return subject; }

    /**
     * Sets the subject covered in this session.
     *
     * @param subject the new subject name
     */
    public void setSubject(String subject) { this.subject = subject; }

    /**
     * Returns the description of this session.
     *
     * @return the session description
     */
    public String getDescription() { return description; }

    /**
     * Sets the description of this session.
     *
     * @param description the new session description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Returns the price of this session.
     *
     * @return the price
     */
    public double getPrice() { return price; }

    /**
     * Sets the price of this session.
     *
     * @param price the new price
     */
    public void setPrice(double price) { this.price = price; }

    /**
     * Returns the location of this session.
     *
     * @return the location string
     */
    public String getLocation() { return location; }

    /**
     * Sets the location of this session.
     *
     * @param location the new location
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * Returns the start date and time of this session.
     *
     * @return the start date/time
     */
    public LocalDateTime getStart_date() { return start_date; }

    /**
     * Sets the start date and time of this session.
     *
     * @param start_date the new start date/time
     */
    public void setStart_date(LocalDateTime start_date) { this.start_date = start_date; }

    /**
     * Returns the end date and time of this session.
     *
     * @return the end date/time
     */
    public LocalDateTime getEnd_date() { return end_date; }

    /**
     * Sets the end date and time of this session.
     *
     * @param end_date the new end date/time
     */
    public void setEnd_date(LocalDateTime end_date) { this.end_date = end_date; }

    /**
     * Returns the tutor who owns this session.
     *
     * @return the tutor
     */
    public Tutor getTutor() { return tutor; }

    /**
     * Sets the tutor who owns this session.
     *
     * @param tutor the new tutor
     */
    public void setTutor(Tutor tutor) { this.tutor = tutor; }

    /**
     * Returns the student who has booked this session, or {@code null} if unbooked.
     *
     * @return the booked student, or {@code null}
     */
    public Student getStudent() { return student; }

    /**
     * Sets the student who has booked this session.
     * Pass {@code null} to mark the session as unbooked.
     *
     * @param student the new student, or {@code null}
     */
    public void setStudent(Student student) { this.student = student; }

    /**
     * Returns the status string of this session (e.g. {@code "Available"},
     * {@code "Booked"}, {@code "Cancel"}, {@code "Complete"}).
     *
     * @return the status string
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status string of this session directly.
     * Prefer using {@link #setState(SessionState)} which keeps the status
     * and state object in sync.
     *
     * @param status the new status string
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Transitions this session to a new state using the State pattern.
     * The {@code status} field is updated automatically from the new state's
     * {@link SessionState#statusName()} and a property-change event is fired.
     *
     * @param newState the new {@link SessionState} to apply
     */
    public void setState(SessionState newState) {
        SessionState oldState = this.state;
        this.state = newState;

        if (newState != null) {
            this.status = newState.statusName();
        }

        support.firePropertyChange("state", oldState, newState);
    }

    /**
     * Returns the current state object of this session.
     *
     * @return the current {@link SessionState}
     */
    public SessionState getState() {
        return state;
    }

    /**
     * Delegates the complete transition to the current state.
     * Only sessions in the {@link Book} state can be completed.
     *
     * @param session this session (passed to the state for mutation)
     */
    public void complete(Session session) {
        if (state != null) state.complete(this);
    }

    /**
     * Delegates the book transition to the current state.
     * Only sessions in the {@link Available} state can be booked.
     *
     * @param session this session (passed to the state for mutation)
     */
    public void book(Session session) {
        if (state != null) state.book(this);
    }

    /**
     * Delegates the cancel transition to the current state.
     * Only sessions in the {@link Book} state can be cancelled.
     *
     * @param session this session (passed to the state for mutation)
     */
    public void cancel(Session session) {
        if (state != null) state.cancel(this);
    }

    /**
     * Registers a listener to receive property-change events fired by this session.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a previously registered property-change listener.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
