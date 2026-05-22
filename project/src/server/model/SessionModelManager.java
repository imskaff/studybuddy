package server.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.dao.SessionDAO;
import server.dao.UserDAO;

public class SessionModelManager implements SessionModel, PropertyChangeListener {
    private final SessionList list;
    private PropertyChangeSupport support;
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;

    public SessionModelManager(SessionDAO sessionDAO, UserDAO userDAO) {
        list = new SessionList();
        support = new PropertyChangeSupport(this);
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
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

  @Override public boolean bookSession(int studentId, int sessionId)
  {
    try {
      // Reads the selected session from the database
      Session session = this.sessionDAO.readByInt(sessionId);
      // Reads the student from the database
      Student student = (Student) this.userDAO.readByInt(studentId);
      // Checks if the session or student does not exist
      if(session == null || student == null)
      {
        return false;
      }
      // Checks if the session is already booked
      if(session.getStudent() != null)
      {
        return false;
      }
      // Assigns the student to the session
      session.setStudent(student);
      // Updates the session in the database
      this.sessionDAO.update(session);

      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean createSession(Session session)
  {
    try {
      // Checks if the session data is valid
      if(session == null || session.getTutor() == null || session.getDescription() == null
          || session.getDescription().isEmpty() || session.getPrice() <= 0
          || session.getLocation() == null || session.getLocation().isEmpty())
      {
        return false;
      }
      // Creates the session in the database
      this.sessionDAO.create(session.getDescription(), session.getPrice(),
          session.getSubject(), session.getLocation(), session.getStart_date(),
          session.getEnd_date(), session.getTutor(), session.getStudent());

      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean editSession(Session updatedSession)
  {
    try {
      // Checks if the received session is invalid
      if(updatedSession == null || updatedSession.getTutor() == null || updatedSession.getDescription() == null
          || updatedSession.getDescription().isEmpty() || updatedSession.getPrice() <= 0
          || updatedSession.getLocation() == null || updatedSession.getLocation().isEmpty())
      {
        return false;
      }
      // Reads the existing session from the database
      Session existing = this.sessionDAO.readByInt(updatedSession.getId());

      // Checks if the session exists
      if(existing == null)
      {
        return false;
      }
      // Updates the session data
      existing.setDescription(updatedSession.getDescription());
      existing.setPrice(updatedSession.getPrice());
      existing.setSubject(updatedSession.getSubject());
      existing.setLocation(updatedSession.getLocation());
      existing.setStart_date(updatedSession.getStart_date());
      existing.setEnd_date(updatedSession.getEnd_date());
      // Updates the session in the database
      this.sessionDAO.update(existing);

      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean deleteSession(int sessionId)
  {
    try {
      // Reads the session from the database
      Session session = this.sessionDAO.readByInt(sessionId);
      // Checks if the session exists
      if(session == null)
      {
        return false;
      }
      // Deletes the session from the database
      this.sessionDAO.delete(session);

      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean createStudentProfile(Student student)
  {
    try
    {
      // Checks if required fields are missing or empty
      if(student == null || student.getFirst_name() == null || student.getFirst_name().isEmpty()
          || student.getLast_name() == null || student.getLast_name().isEmpty() || student.getDate_of_birth() == null
          || student.getEmail() == null || student.getEmail().isEmpty() || student.getPhone() == null
          || student.getPhone().isEmpty() || student.getPassword() == null || student.getPassword().isEmpty()
          || student.getGender() == null || student.getGender().isEmpty() || student.getLanguages() == null
          || student.getLanguages().isEmpty())
      {
        return false;
      }
      // Checks if the email already exists in the database
      if(this.userDAO.readByEmail(student.getEmail()) != null)
      {
        return false;
      }
      // Creates the student profile in the database
      this.userDAO.create(student.getFirst_name(), student.getLast_name(),
          student.getDate_of_birth(), student.getEmail(), student.getPhone(),
          student.getPassword(), student.getGender(), student.getLanguages(), "Student", null);

      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean createTutorProfile(Tutor tutor)
  {
    try {
      // Checks if required fields are missing or empty
      if(tutor == null || tutor.getFirst_name() == null || tutor.getFirst_name().isEmpty()
          || tutor.getLast_name() == null || tutor.getLast_name().isEmpty() || tutor.getDate_of_birth() == null
          || tutor.getEmail() == null || tutor.getEmail().isEmpty() || tutor.getPhone() == null
          || tutor.getPhone().isEmpty() || tutor.getPassword() == null || tutor.getPassword().isEmpty()
          || tutor.getGender() == null || tutor.getGender().isEmpty() || tutor.getLanguages() == null
          || tutor.getLanguages().isEmpty() || tutor.getEducation_level() == null || tutor.getEducation_level().isEmpty())
      {
        return false;
      }
      // Checks if the email already exists in the database
      if(this.userDAO.readByEmail(tutor.getEmail()) != null)
      {
        return false;
      }
      this.userDAO.create(tutor.getFirst_name(), tutor.getLast_name(), tutor.getDate_of_birth(),
          tutor.getEmail(), tutor.getPhone(), tutor.getPassword(), tutor.getGender(),
          tutor.getLanguages(), "Tutor", tutor.getEducation_level());

      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean handleTutorRegistration(int tutorId, String action, int adminId)
  {
    try
    {
      // Approves the tutor
      if("APPROVE".equals(action))
      {
        this.userDAO.updateApprovalStatus(tutorId, true, adminId);
        return true;
      }

      // Declines the tutor
      else if("DECLINE".equals(action))
      {
        this.userDAO.declineTutor(tutorId);
        return true;
      }

      return false;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean editStudentProfile(Student student)
  {
    try {
      // Checks if required fields are missing or empty
      if(student == null || student.getFirst_name() == null || student.getFirst_name().isEmpty()
          || student.getLast_name() == null || student.getLast_name().isEmpty()
          || student.getDate_of_birth() == null || student.getEmail() == null || student.getEmail().isEmpty()
          || student.getPhone() == null || student.getPhone().isEmpty()
          || student.getPassword() == null || student.getPassword().isEmpty()
          || student.getGender() == null || student.getGender().isEmpty()
          || student.getLanguages() == null || student.getLanguages().isEmpty())
      {
        return false;
      }
      // Updates the student profile in the database
      this.userDAO.update(student);

      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean editTutorProfile(Tutor tutor)
  {
    try {
      // Checks if required fields are missing or empty
      if(tutor == null || tutor.getFirst_name() == null || tutor.getFirst_name().isEmpty()
          || tutor.getLast_name() == null || tutor.getLast_name().isEmpty()
          || tutor.getDate_of_birth() == null || tutor.getEmail() == null || tutor.getEmail().isEmpty()
          || tutor.getPhone() == null || tutor.getPhone().isEmpty()
          || tutor.getPassword() == null || tutor.getPassword().isEmpty()
          || tutor.getGender() == null || tutor.getGender().isEmpty()
          || tutor.getLanguages() == null || tutor.getLanguages().isEmpty()
          || tutor.getEducation_level() == null || tutor.getEducation_level().isEmpty())
      {
        return false;
      }
      // Updates the common user information in the database
      this.userDAO.update(tutor);

      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public List<String> getSubjects()
  {
    try
    {
      return this.sessionDAO.readSubjects();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }

  @Override
  public ArrayList<Session> getAvailableSessions() throws SQLException {
      return new ArrayList<>(this.sessionDAO.readAvailable());
  }

  @Override
  public ArrayList<Session> getBookedSessions(int studentId) throws SQLException {
      return new ArrayList<>(this.sessionDAO.readByStudent(studentId));
  }

  @Override
  public ArrayList<Session> getTutorSessions(int tutorId) throws SQLException {
      return new ArrayList<>(this.sessionDAO.readByTutor(tutorId));
  }

  @Override
  public ArrayList<Tutor> getPendingTutors() throws SQLException {
      return this.userDAO.readPendingTutors();
  }

  @Override
  public User login(String email, String password) throws SQLException {
      return this.userDAO.readByEmailAndPassword(email, password);
  }

  @Override
  public int getAdminId(String email, String password) throws SQLException {
      return this.userDAO.getAdminId(email, password);
  }
}
