package server.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class SessionModelManager implements SessionModel, PropertyChangeListener {
    private final SessionList list;
    private PropertyChangeSupport support;
  private ArrayList<Student> students;
  private ArrayList<Tutor> tutors;
  private ArrayList<Tutor> pendingTutors;

    public SessionModelManager() {
        list = new SessionList();
      students = new ArrayList<>();
      tutors = new ArrayList<>();
      pendingTutors = new ArrayList<>();
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void bookSession(Student student, Tutor tutor, Session sessionId) {
        list.addSession(sessionId);
    }



    @Override
    public ArrayList<Session> getAllSessions() {
        return list.getAllSessions();
    }

    @Override
    public Session getSession(int index) {
        return list.getSession(index);
    }

    @Override
    public Session getSessionById(int id) {
        return list.getSessionById(id);
    }

  @Override
    public int getSize() {
        return list.getSize();
    }

    @Override
    public void removeSession(int index) {
        list.removeSession(index);
    }

    @Override
    public void removeSessionById(int id) {
        list.removeSessionById(id);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

  @Override public boolean bookSession(Student student, Session session)
  {
    if(student == null || session == null)
    {
      return false;
    }

    // Check if the session is already booked
    if(session.getStudent() != null)
    {
      return false;
    }

    // Assign the student to the session
    session.setStudent(student);

    return true;
  }

  @Override public boolean createSession(Session session)
  {
    if(session == null
        || session.getTutor() == null
        || session.getDescription() == null
        || session.getDescription().isEmpty()
        || session.getPrice() <= 0
        || session.getLocation() == null)
    {
      return false;
    }

    list.addSession(session);

    return true;
  }

  @Override public boolean editSession(Session updatedSession)
  {
    // Checks if the received session is null
    if(updatedSession == null
        || updatedSession.getTutor() == null
        || updatedSession.getDescription() == null
        || updatedSession.getDescription().isEmpty()
        || updatedSession.getPrice() <= 0
        || updatedSession.getLocation() == null
        || updatedSession.getLocation().isEmpty())
    {
      return false;
    }

    // Finds the existing session using the session ID
    Session existing = list.getSessionById(updatedSession.getId());

    // Updates the selected session
    existing.setDescription(updatedSession.getDescription());
    existing.setPrice(updatedSession.getPrice());
    existing.setLocation(updatedSession.getLocation());
    existing.setStart_date(updatedSession.getStart_date());
    existing.setEnd_date(updatedSession.getEnd_date());

    return true;
  }

  @Override public boolean deleteSession(int sessionId)
  {
    Session session = list.getSessionById(sessionId);

    if(session == null)
    {
      return false;
    }

    list.removeSessionById(sessionId);

    return true;
  }

  @Override public boolean createStudentProfile(Student student)
  {
    // Checks if the student object exists
    if(student == null
        || student.getFirst_name() == null //the object/value does not exist at all
        || student.getFirst_name().isEmpty() //the object exists, but contains nothing
        || student.getLast_name() == null
        || student.getLast_name().isEmpty()
        || student.getDate_of_birth() == null
        || student.getEmail() == null
        || student.getEmail().isEmpty()
        || student.getPhone() == null
        || student.getPhone().isEmpty()
        || student.getPassword() == null
        || student.getPassword().isEmpty()
        || student.getGender() == null
        || student.getGender().isEmpty()
        || student.getLanguages() == null
        || student.getLanguages().isEmpty())
    {
      return false;
    }

    // Checks if another student already uses the same email
    for(int i = 0; i < students.size(); i++)
    {
      if(students.get(i).getEmail()
          .equals(student.getEmail()))
      {
        return false;
      }
    }

    // Adds the student to the student list
    students.add(student);

    return true;
  }

  @Override public boolean createTutorProfile(Tutor tutor)
  {
    // Checks if the tutor object exists
    if(tutor == null
        || tutor.getFirst_name() == null
        || tutor.getFirst_name().isEmpty()
        || tutor.getLast_name() == null
        || tutor.getLast_name().isEmpty()
        || tutor.getDate_of_birth() == null
        || tutor.getEmail() == null
        || tutor.getEmail().isEmpty()
        || tutor.getPhone() == null
        || tutor.getPhone().isEmpty()
        || tutor.getPassword() == null
        || tutor.getPassword().isEmpty()
        || tutor.getGender() == null
        || tutor.getGender().isEmpty()
        || tutor.getLanguages() == null
        || tutor.getLanguages().isEmpty()
        || tutor.getEducation_level() == null
        || tutor.getEducation_level().isEmpty())
    {
      return false;
    }

    // Checks if another tutor already uses the same email
    for(int i = 0; i < tutors.size(); i++)
    {
      if(tutors.get(i).getEmail()
          .equals(tutor.getEmail()))
      {
        return false;
      }
    }

    // Checks if another pending tutor request already uses the same email
    for(int i = 0; i < pendingTutors.size(); i++)
    {
      if(pendingTutors.get(i).getEmail()
          .equals(tutor.getEmail()))
      {
        return false;
      }
    }

    // Sets the tutor status to pending
    tutor.setStatus("pending");

    // Adds the tutor to the pending tutor registration list
    pendingTutors.add(tutor);

    return true;
  }

  @Override public boolean handleTutorRegistration(int tutorId, String action)
  {
    // Searches through the pending tutor requests
    for (int i = 0; i < pendingTutors.size(); i++)
    {
      Tutor tutor = pendingTutors.get(i);

      // Checks if this is the selected tutor
      if (tutor.getId() == tutorId)
      {
        // Approves the tutor registration
        if (action.equals("APPROVE"))
        {
          // Sets the tutor status to approved
          tutor.setStatus("approved");

          // Adds the tutor to the approved tutor list
          tutors.add(tutor);

          // Removes the tutor from the pending requests list
          pendingTutors.remove(i);

          return true;
        }

        // Declines the tutor registration
        else if (action.equals("DECLINE"))
        {
          // Removes the tutor from the pending requests list
          pendingTutors.remove(i);

          return true;
        }
      }
    }
    // Returns false if no pending tutor request was found
    return false;
  }

  @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }
}
