package client.viewmodel;

import client.model.Model;
import javafx.beans.property.*;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;

public class BookingViewModel implements PropertyChangeListener {

    private final Model model;

    //  Tutor
    private final StringProperty tutorName = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final StringProperty location = new SimpleStringProperty();

    // Student
    private final StringProperty studentName = new SimpleStringProperty();
    private final StringProperty studentEmail = new SimpleStringProperty();
    private final StringProperty studentPhone = new SimpleStringProperty();

    //  Chosen by the Student time and date
    private final ObjectProperty<LocalDateTime> chosenStartDate =
            new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> chosenEndDate   =
            new SimpleObjectProperty<>();

    //UI feedback
    private final StringProperty  errorMessage   = new SimpleStringProperty();
    private final BooleanProperty bookingSuccess = new SimpleBooleanProperty(false);

    // Internal state
    private Session  selectedSession;
    private Student  loggedInStudent;
    private Tutor sessionTutor;

    public BookingViewModel(Model model) {
        this.model = model;
    }

    //Populates all pre-filled fields automatically.
    public void initWithSessionAndStudent(Session session, Student student) {
        this.selectedSession = session;
        this.loggedInStudent = student;
        this.sessionTutor    = session.getTutor();

        // session info
        description.set(session.getDescription());
        price.set(session.getPrice());
        location.set(session.getLocation());
        tutorName.set(sessionTutor.getFirst_name() + " " + sessionTutor.getLast_name());

        // student info
        studentName.set(student.getFirst_name() + " " + student.getLast_name());
        studentEmail.set(student.getEmail());
        studentPhone.set(student.getPhone());

        // Reset booking state
        chosenStartDate.set(null);
        chosenEndDate.set(null);
        errorMessage.set("");
        bookingSuccess.set(false);
    }

    //validation
    public void bookSession() throws Exception {
        errorMessage.set("");
        bookingSuccess.set(false);

        if (selectedSession == null || loggedInStudent == null) {
            errorMessage.set("No session or student loaded.");
            return;
        }
        if (chosenStartDate.get() == null || chosenEndDate.get() == null) {
            errorMessage.set("Please select both a start and end time.");
            return;
        }
        if (!chosenEndDate.get().isAfter(chosenStartDate.get())) {
            errorMessage.set("End time must be after start time.");
            return;
        }


        Session bookedSession = new Session(
                selectedSession.getId(),
                selectedSession.getDescription(),
                selectedSession.getPrice(),
                selectedSession.getSubject(),
                selectedSession.getLocation(),
                selectedSession.getStart_date(),
                selectedSession.getEnd_date(),
                sessionTutor,
                loggedInStudent
        );

        model.bookSession(loggedInStudent, sessionTutor, bookedSession);
        bookingSuccess.set(true);
    }

    public StringProperty tutorNameProperty()        { return tutorName; }
    public StringProperty descriptionProperty()      { return description; }
    public DoubleProperty priceProperty()            { return price; }
    public StringProperty locationProperty()         { return location; }

    public StringProperty studentNameProperty()      { return studentName; }
    public StringProperty studentEmailProperty()     { return studentEmail; }
    public StringProperty studentPhoneProperty()     { return studentPhone; }

    public ObjectProperty<LocalDateTime> chosenStartDateProperty() { return chosenStartDate; }
    public ObjectProperty<LocalDateTime> chosenEndDateProperty()   { return chosenEndDate; }

    public StringProperty  errorMessageProperty()    { return errorMessage; }
    public BooleanProperty bookingSuccessProperty()  { return bookingSuccess; }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "SessionAdded":
                bookingSuccess.set(true);
                break;
            case "SessionRemoved":
                break;
        }
    }
}
