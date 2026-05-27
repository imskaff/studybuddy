package client.viewmodel.student;

import client.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.Session;
import server.model.Student;
import server.model.Tutor;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreateStudentProfileViewModelTest {

  private FakeModel fakeModel;
  private CreateStudentProfileViewModel viewModel;

  @BeforeEach
  public void setUp() {
    fakeModel = new FakeModel();
    viewModel = new CreateStudentProfileViewModel(fakeModel);

    viewModel.firstNameProperty().set("Bob");
    viewModel.lastNameProperty().set("Johnson");
    viewModel.dateOfBirthProperty().set("2000-01-01");
    viewModel.emailProperty().set("bob@student.com");
    viewModel.phoneNoProperty().set("1122334455");
    viewModel.passwordProperty().set("StudentPass");
    viewModel.genderProperty().set("Male");
    viewModel.getLanguages().addAll(Arrays.asList("English"));
  }

  @Test
  public void testRegister_CreatesAndRegistersStudentCorrectly() {
    viewModel.register();

    Student registered = fakeModel.lastRegisteredStudent;
    assertNotNull(registered);
    assertEquals("Bob", registered.getFirst_name());
    assertEquals("Johnson", registered.getLast_name());
    assertEquals(LocalDate.of(2000, 1, 1), registered.getDate_of_birth());
    assertEquals("bob@student.com", registered.getEmail());
    assertEquals("1122334455", registered.getPhone());
    assertEquals("StudentPass", registered.getPassword());
    assertEquals("Male", registered.getGender());
    assertEquals(1, registered.getLanguages().size());
    assertEquals("STUDENT", registered.getType());
    assertEquals(0, registered.getId());
  }

  private static class FakeModel implements Model {
    Student lastRegisteredStudent;
    boolean registerReturn = false;

    @Override public boolean registerStudent(Student s) { lastRegisteredStudent = s; return registerReturn; }

    @Override public void connect() {}
    @Override public void disconnect() {}
    @Override public void addPropertyChangeListener(PropertyChangeListener l) {}
    @Override public ArrayList<Session> getAllSessions() { return new ArrayList<>(); }
    @Override public ArrayList<Session> getTutorSessions() { return new ArrayList<>(); }
    @Override public ArrayList<Session> getBookedSessions() { return new ArrayList<>(); }
    @Override public void bookSession(Student s, Tutor t, Session se) {}
    @Override public void setLoggedInStudent(Student s) {}
    @Override public Student getLoggedInStudent() { return null; }
    @Override public void setLoggedInAdminId(int id) {}
    @Override public int getLoggedInAdminId() { return 0; }
    @Override public void setLoggedInTutor(Tutor t) {}
    @Override public Tutor getLoggedInTutor() { return null; }
    @Override public ArrayList<Tutor> getPendingTutors() { return new ArrayList<>(); }
    @Override public boolean createSession(Session s) { return false; }
    @Override public boolean editSession(Session s) { return false; }
    @Override public boolean cancelSession(int u, int s, String t) { return false; }
    @Override public boolean deleteSession(int id) { return false; }
    @Override public boolean approveTutor(int id) { return false; }
    @Override public boolean declineTutor(int id) { return false; }
    @Override public boolean registerTutor(Tutor t) { return false; }
    @Override public boolean editStudentProfile(Student s) { return false; }
    @Override public boolean editTutorProfile(Tutor t) { return false; }
    @Override public List<String> getSubjects() { return new ArrayList<>(); }
    @Override public String login(String e, String p) { return null; }
  }
}
