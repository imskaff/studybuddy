package client.viewmodel.tutor;

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

public class CreateTutorProfileViewModelTest {

  private FakeModel fakeModel;
  private CreateTutorProfileViewModel viewModel;

  @BeforeEach
  public void setUp() {
    fakeModel = new FakeModel();
    viewModel = new CreateTutorProfileViewModel(fakeModel);

    viewModel.firstNameProperty().set("  Alice  ");
    viewModel.lastNameProperty().set(" Smith ");
    viewModel.dateOfBirthProperty().set("1985-10-20");
    viewModel.emailProperty().set(" alice@tutor.com ");
    viewModel.phoneNoProperty().set(" 9876543210 ");
    viewModel.passwordProperty().set("TutorPass");
    viewModel.genderProperty().set(" Female ");
    viewModel.educationLevelProperty().set(" Master Degree ");
    viewModel.getLanguages().addAll(Arrays.asList("English", "French"));
  }

  @Test
  public void testRegister_CreatesAndRegistersTutorCorrectly() {
    viewModel.register();

    Tutor registered = fakeModel.lastRegisteredTutor;
    assertNotNull(registered);
    assertEquals("Alice", registered.getFirst_name());
    assertEquals("Smith", registered.getLast_name());
    assertEquals(LocalDate.of(1985, 10, 20), registered.getDate_of_birth());
    assertEquals("alice@tutor.com", registered.getEmail());
    assertEquals("9876543210", registered.getPhone());
    assertEquals("TutorPass", registered.getPassword());
    assertEquals("Female", registered.getGender());
    assertEquals("Master Degree", registered.getEducation_level());
    assertEquals(2, registered.getLanguages().size());
    assertEquals("TUTOR", registered.getType());
    assertEquals(0, registered.getId());
  }

  @Test
  public void register_returnsFalse_whenFirstNameIsBlank() {
    viewModel.firstNameProperty().set("   ");

    boolean result = viewModel.register();

    assertFalse(result);
  }

  @Test
  public void register_returnsFalse_whenDateFormatIsInvalid() {
    viewModel.dateOfBirthProperty().set("not-a-date");

    boolean result = viewModel.register();

    assertFalse(result);
  }

  @Test
  public void register_returnsFalse_whenModelReturnsFailure() {
    fakeModel.registerReturn = false;

    boolean result = viewModel.register();

    assertFalse(result);
  }

  @Test
  public void register_returnsTrue_whenModelSucceeds() {
    fakeModel.registerReturn = true;

    boolean result = viewModel.register();

    assertTrue(result);
  }

  private static class FakeModel implements Model {
    Tutor lastRegisteredTutor;
    boolean registerReturn = false;

    @Override public boolean registerTutor(Tutor tutor) { lastRegisteredTutor = tutor; return registerReturn; }

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
    @Override public boolean registerStudent(Student s) { return false; }
    @Override public boolean editStudentProfile(Student s) { return false; }
    @Override public boolean editTutorProfile(Tutor t) { return false; }
    @Override public List<String> getSubjects() { return new ArrayList<>(); }
    @Override public String login(String e, String p) { return null; }
    @Override public void complete(Session s) {}
    @Override public void book(Session s) {}
    @Override public void cancel(Session s) {}
    @Override public void remove(Session s) {}
  }
}
