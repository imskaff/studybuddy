package server.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class BookedTest
{
  @Test void whenNowIsAfterEndDate_setStateToComplete()
  {
    Book book = new Book();
    Session mockSession = mock(Session.class);

    LocalDateTime endDate = LocalDateTime.of(2026, 5, 20, 10, 0);
    LocalDateTime fixedNow = LocalDateTime.of(2026, 5, 20, 12, 0);

    when(mockSession.getEnd_date()).thenReturn(endDate);

    book.complete(mockSession, fixedNow);

    verify(mockSession, times(1)).setState(any(Complete.class));
  }

  @Test
  void whenNowIsEqualEndDate_setStateToComplete()
  {
    Book book = new Book();
    Session mockSession = mock(Session.class);

    LocalDateTime exactTime = LocalDateTime.of(2026, 5, 20, 10, 0);

    when(mockSession.getEnd_date()).thenReturn(exactTime);

    book.complete(mockSession, exactTime);

    verify(mockSession, times(1)).setState(any(Complete.class));
  }

  @Test
  void whenNowIsBeforeEndDate_shouldNotChangeState()
  {
    Book book = new Book();
    Session mockSession = mock(Session.class);

    LocalDateTime endDate = LocalDateTime.of(2026, 5, 20, 10, 0);
    LocalDateTime fixedNow = LocalDateTime.of(2026, 5, 19, 10, 0);

    when(mockSession.getEnd_date()).thenReturn(endDate);

    book.complete(mockSession, fixedNow);

    verify(mockSession, never()).setState(any());
  }
}
