package client.viewmodel.tutor;

import client.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.model.Tutor;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class CreateTutorProfileViewModelTest {

  @Mock
  private Model model;

  @InjectMocks
  private CreateTutorProfileViewModel viewModel;

  @Captor
  private ArgumentCaptor<Tutor> tutorCaptor;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);


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

    verify(model).registerTutor(tutorCaptor.capture());
    Tutor capturedTutor = tutorCaptor.getValue();

    assertEquals("Alice", capturedTutor.getFirst_name());
    assertEquals("Smith", capturedTutor.getLast_name());
    assertEquals(LocalDate.of(1985, 10, 20), capturedTutor.getDate_of_birth());
    assertEquals("alice@tutor.com", capturedTutor.getEmail());
    assertEquals("9876543210", capturedTutor.getPhone());
    assertEquals("TutorPass", capturedTutor.getPassword());
    assertEquals("Female", capturedTutor.getGender());
    assertEquals("Master Degree", capturedTutor.getEducation_level());
    assertEquals(2, capturedTutor.getLanguages().size());
    assertEquals("TUTOR", capturedTutor.getType());
    assertEquals(0, capturedTutor.getId());
  }
}