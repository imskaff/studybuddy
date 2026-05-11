package server.model;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class BookedTest
{
  @Test void whenNowIsAfterEndDate_setStateToCompleted()
  {
    //1. Arrange
    Booked booked = new Booked();
    Session mockSession = mock(Session.class);

    LocalDateTime endDate = LocalDateTime.of(2026, 5, 20, 10, 0);
    LocalDateTime fixedNow = LocalDateTime.of(2026, 5, 20, 12, 0);

    when(mockSession.getEnd_date()).thenReturn(endDate);

    // Mock the static LocalDateTime.now() to return fixed time
    try (MockedStatic<LocalDateTime> mockedTime = mockStatic(
        LocalDateTime.class))
    {
      mockedTime.when(LocalDateTime::now).thenReturn(fixedNow);

      // 2.Act
      booked.completed(mockSession);
    }

    //3. Assert
    // Verify that setState was called with an instance of Completed
    verify(mockSession, times(1)).setState(any(Completed.class));
  }

  @Test
  void whenNowIsEqualEndDate_setStateToCompleted()
  {
    Booked booked = new Booked();
    Session mockSession = mock(Session.class);

    LocalDateTime exactTime = LocalDateTime.of(2026, 5, 20, 10, 0);

    when(mockSession.getEnd_date()).thenReturn(exactTime);

    try(MockedStatic<LocalDateTime> mockedTime = mockStatic(
        LocalDateTime.class))
    {
      mockedTime.when(LocalDateTime::now).thenReturn(exactTime);

      booked.completed(mockSession);
    }

    verify(mockSession, times(1)).setState(any(Completed.class));
  }

  @Test
  void whenNowIsBeforeEndDate_shouldNotChangeState()
  {
    Booked booked = new Booked();
    Session mockSession = mock(Session.class);

    LocalDateTime endDate = LocalDateTime.of(2026, 5, 20, 10, 0);
    LocalDateTime fixedNow = LocalDateTime.of(2026, 5, 19, 10, 0);

    when(mockSession.getEnd_date()).thenReturn(endDate);

    try (MockedStatic<LocalDateTime> mockedTime = mockStatic(LocalDateTime.class))
    {
      mockedTime.when(LocalDateTime::now).thenReturn(fixedNow);
      booked.completed(mockSession);
    }
    verify(mockSession, never()).setState(any());
  }
}
