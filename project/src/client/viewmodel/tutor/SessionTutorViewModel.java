package client.viewmodel.tutor;

import client.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;

/**
 * ViewModel for the session detail / edit screen shown to a tutor.
 * 
 * Holds the editable properties of a single selected session and provides
 * {@link #editSession()} and {@link #deleteSession()} actions. Also exposes
 * a subject-options list for the subject combo-box.
 * 
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class SessionTutorViewModel implements PropertyChangeListener
{
    private final Model model;
    private Session selectedSession;

    private final ObservableList<String> subjectOptions;

    private final StringProperty description;
    private final StringProperty tutorName;
    private final StringProperty subject;
    private final StringProperty location;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final StringProperty price;

    /** Formatter used to parse and display date-time values as {@code dd/MM/yyyy HH:mm}. */
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Constructs a {@code SessionTutorViewModel} and registers it as a model
     * property-change listener.
     *
     * @param model the client-side model
     */
    public SessionTutorViewModel(Model model)
    {
        this.model = model;

        this.subjectOptions = FXCollections.observableArrayList();

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
     * Populates all display/edit properties from the given session.
     * {@code null} field values are represented as empty strings.
     *
     * @param session the session to load; may be {@code null} (no-op)
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
     * Parses the current property values and sends an edit request to the server.
     *
     * @return {@code true} if the session was updated successfully; {@code false}
     *         if no session is selected, a field cannot be parsed, or the server
     *         rejects the update
     */
    public boolean editSession()
    {
        if (selectedSession == null) return false;
        try
        {
            double parsedPrice = Double.parseDouble(price.get());
            LocalDateTime parsedStart = LocalDateTime.parse(startDate.get(), FMT);
            LocalDateTime parsedEnd   = LocalDateTime.parse(endDate.get(),   FMT);

            Session updated = new Session(
                selectedSession.getId(),
                description.get(),
                parsedPrice,
                subject.get(),
                location.get(),
                parsedStart,
                parsedEnd,
                selectedSession.getTutor(),
                selectedSession.getStudent()
            );
            return model.editSession(updated);
        }
        catch (Exception e)
        {
            System.err.println("Invalid input: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes the currently selected session.
     *
     * @return {@code true} if the deletion succeeded; {@code false} if no
     *         session is selected or the server rejects the request
     */
    public boolean deleteSession()
    {
        if (selectedSession == null) return false;
        return model.deleteSession(selectedSession.getId());
    }

    /**
     * Returns the currently selected session.
     *
     * @return the selected session, or {@code null} if none is loaded
     */
    public Session getSelectedSession() { return selectedSession; }

    /**
     * Refreshes the subject-options list from the server.
     */
    public void loadSubjects()
    {
        subjectOptions.clear();
        subjectOptions.addAll(model.getSubjects());
    }

    /**
     * Returns the observable list of subject names for the subject combo-box.
     *
     * @return the subject-options list
     */
    public ObservableList<String> getSubjectOptions() { return subjectOptions; }

    /** @return the session description property */
    public StringProperty descriptionProperty() { return description; }
    /** @return the tutor full-name property (read-only display) */
    public StringProperty tutorProperty()        { return tutorName;   }
    /** @return the subject property */
    public StringProperty subjectProperty()      { return subject;     }
    /** @return the location property */
    public StringProperty locationProperty()     { return location;    }
    /** @return the start-date property (format {@code dd/MM/yyyy HH:mm}) */
    public StringProperty startDateProperty()    { return startDate;   }
    /** @return the end-date property (format {@code dd/MM/yyyy HH:mm}) */
    public StringProperty endDateProperty()      { return endDate;     }
    /** @return the price property */
    public StringProperty priceProperty()        { return price;       }

    /**
     * No-op implementation — this ViewModel does not currently react to
     * model property-change events.
     *
     * @param evt the property-change event (ignored)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}
