package client.viewmodel.tutor;

import client.model.TutorModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Session;

public class SessionTutorViewModel implements PropertyChangeListener
{
    private final TutorModel model;
    private Session selectedSession;

    private final ObservableList<String> subjectOptions;

    private final StringProperty description;
    private final StringProperty tutorName;
    private final StringProperty subject;
    private final StringProperty location;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final StringProperty price;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SessionTutorViewModel(TutorModel model)
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

    public boolean deleteSession()
    {
        if (selectedSession == null) return false;
        return model.deleteSession(selectedSession.getId());
    }

    public Session getSelectedSession() { return selectedSession; }

    public void loadSubjects()
    {
        subjectOptions.clear();
        subjectOptions.addAll(model.getSubjects());
    }

    public ObservableList<String> getSubjectOptions() { return subjectOptions; }

    public StringProperty descriptionProperty() { return description; }
    public StringProperty tutorProperty()        { return tutorName;   }
    public StringProperty subjectProperty()      { return subject;     }
    public StringProperty locationProperty()     { return location;    }
    public StringProperty startDateProperty()    { return startDate;   }
    public StringProperty endDateProperty()      { return endDate;     }
    public StringProperty priceProperty()        { return price;       }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}
