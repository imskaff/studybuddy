package client.viewmodel.student;

import client.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;
import server.model.Tutor;
import server.model.Student;

/**
 * ViewModel for the session detail screen shown to a student.
 * 
 * Holds the properties that describe a single selected session and provides
 * a {@link #bookSession()} action. Registers as a model listener to keep the
 * internal sessions list in sync with {@code SessionAdded} / {@code SessionRemoved}
 * events.
 * 
 *
 * @author
 */
public class SessionStudentViewModel implements PropertyChangeListener
{
    private Model model;
    private ObservableList<Session> sessions;
    private Session selectedSession;

    private final StringProperty description;
    private final StringProperty tutorName;
    private final StringProperty subject;
    private final StringProperty location;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final StringProperty price;

    /** Formatter used to display date-time values as {@code dd/MM/yyyy HH:mm}. */
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Constructs a {@code SessionStudentViewModel} and registers it as a model
     * property-change listener.
     *
     * @param model the client-side model
     */
    public SessionStudentViewModel(Model model)
    {
        this.model = model;
        this.sessions = FXCollections.observableArrayList();

        this.description = new SimpleStringProperty("");
        this.tutorName   = new SimpleStringProperty("");
        this.subject     = new SimpleStringProperty("");
        this.location    = new SimpleStringProperty("");
        this.startDate   = new SimpleStringProperty("");
        this.endDate     = new SimpleStringProperty("");
        this.price       = new SimpleStringProperty("");
        model.addPropertyChangeListener(this);
    }

    /**
     * Populates all display properties from the given session.
     * Null field values are represented as empty strings.
     *
     * @param session the session to display; may be {@code null} (no-op)
     */
    public void loadSession(Session session)
    {
        this.selectedSession = session;
        if (session != null)
        {
            description.set(session.getDescription() != null ? session.getDescription() : "");
            tutorName.set(session.getTutor() != null
                ? session.getTutor().getFirst_name() + " " + session.getTutor().getLast_name()
                : "");
            subject.set(session.getSubject() != null ? session.getSubject() : "");
            location.set(session.getLocation() != null ? session.getLocation() : "");
            startDate.set(session.getStart_date() != null ? session.getStart_date().format(FMT) : "");
            endDate.set(session.getEnd_date()   != null ? session.getEnd_date().format(FMT)   : "");
            price.set(String.valueOf(session.getPrice()));
        }
    }

    /**
     * Returns the observable list of sessions maintained by this ViewModel.
     *
     * @return the sessions list
     */
    public ObservableList<Session> getSessions() { return sessions; }

    /**
     * Returns the currently selected session.
     *
     * @return the selected session, or {@code null} if none is loaded
     */
    public Session getSelectedSession() { return selectedSession; }

    /**
     * Returns the tutor associated with the currently selected session.
     *
     * @return the tutor, or {@code null} if no session is selected
     */
    public Tutor getSelectedTutor()
    {
        if (selectedSession == null) return null;
        return selectedSession.getTutor();
    }

    /** @return the session description property */
    public StringProperty descriptionProperty() { return description; }
    /** @return the tutor full-name property */
    public StringProperty tutorProperty()        { return tutorName;   }
    /** @return the subject property */
    public StringProperty subjectProperty()      { return subject;     }
    /** @return the location property */
    public StringProperty locationProperty()     { return location;    }
    /** @return the start-date property (formatted as {@code dd/MM/yyyy HH:mm}) */
    public StringProperty startDateProperty()    { return startDate;   }
    /** @return the end-date property (formatted as {@code dd/MM/yyyy HH:mm}) */
    public StringProperty endDateProperty()      { return endDate;     }
    /** @return the price property */
    public StringProperty priceProperty()        { return price;       }

    /**
     * Books the currently selected session for the logged-in student.
     * Logs a message and returns without action if no session is selected or
     * no student is logged in.
     */
    public void bookSession()
    {
        if (selectedSession == null)
        {
            System.out.println("No session selected");
            return;
        }

        Student loggedInStudent = model.getLoggedInStudent();

        if (loggedInStudent == null)
        {
            System.out.println("No student logged in");
            return;
        }

        try
        {
            model.bookSession(loggedInStudent, selectedSession.getTutor(), selectedSession);
            System.out.println("Session booked");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Handles model property-change events.
     * 
     * Adds the session to the list on a {@code SessionAdded} event and
     * removes it on a {@code SessionRemoved} event.
     * 
     *
     * @param evt the property-change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        switch (evt.getPropertyName())
        {
            case "SessionAdded":
                sessions.add((Session) evt.getNewValue());
                break;
            case "SessionRemoved":
                sessions.remove((Session) evt.getNewValue());
                break;
        }
    }
}
