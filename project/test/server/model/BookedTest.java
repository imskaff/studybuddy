package server.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class BookedTest
{
  @Test void whenNowIsAfterEndDate_setStateToCompleted()
  {
    Booked booked = new Booked();
    Session mockSession = mock(Session.class);

    LocalDateTime endDate = LocalDateTime.of(2026, 5, 20, 10, 0);
    LocalDateTime fixedNow = LocalDateTime.of(2026, 5, 20, 12, 0);

    when(mockSession.getEnd_date()).thenReturn(endDate);

    booked.completed(mockSession, fixedNow);

    verify(mockSession, times(1)).setState(any(Completed.class));
  }

  @Test
  void whenNowIsEqualEndDate_setStateToCompleted()
  {
    Booked booked = new Booked();
    Session mockSession = mock(Session.class);

    LocalDateTime exactTime = LocalDateTime.of(2026, 5, 20, 10, 0);

    when(mockSession.getEnd_date()).thenReturn(exactTime);

    booked.completed(mockSession, exactTime);

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

    booked.completed(mockSession, fixedNow);

    verify(mockSession, never()).setState(any());
  }
}
