package client.viewmodel.tutor;

import client.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.model.Session;

public class SessionTutorViewModel implements PropertyChangeListener
{
    private final Model model;
    private Session selectedSession;

    private final StringProperty description;
    private final StringProperty tutorName;
    private final StringProperty subject;
    private final StringProperty location;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final StringProperty price;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SessionTutorViewModel(Model model)
    {
        this.model = model;

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

    public Session getSelectedSession() { return selectedSession; }

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
