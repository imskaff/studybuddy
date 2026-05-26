package client.viewmodel.student;

import client.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.model.Student;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

  public class CreateStudentProfileViewModelTest {

    @Mock
    private Model model;

    @InjectMocks
    private CreateStudentProfileViewModel viewModel;

    @Captor
    private ArgumentCaptor<Student> studentCaptor;

    @BeforeEach
    public void setUp() {
      MockitoAnnotations.openMocks(this);


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
    public void testRegister_CreatesAndRegistersStudentCorrectly() throws SQLException {

      viewModel.register();


      verify(model).registerStudent(studentCaptor.capture());
      Student capturedStudent = studentCaptor.getValue();


      assertEquals("Bob", capturedStudent.getFirst_name(), "First name should be trimmed");
      assertEquals("Johnson", capturedStudent.getLast_name(), "Last name should be trimmed");
      assertEquals(LocalDate.of(0, 1, 1), capturedStudent.getDate_of_birth(), "Date should be parsed correctly");
      assertEquals("bob@student.com", capturedStudent.getEmail(), "Email should be trimmed");
      assertEquals("1122334455", capturedStudent.getPhone(), "Phone number should be trimmed");
      assertEquals("StudentPass", capturedStudent.getPassword(), "Password should NOT be trimmed");
      assertEquals("Male", capturedStudent.getGender(), "Gender should be trimmed");
      assertEquals(1, capturedStudent.getLanguages().size(), "Languages list size should match");
      assertEquals("STUDENT", capturedStudent.getType(), "User type should be STUDENT");
      assertEquals(0, capturedStudent.getId(), "Default ID should be 0 on creation");
    }
  }
