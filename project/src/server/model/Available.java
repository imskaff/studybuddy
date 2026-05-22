package server.model;

public record Available() implements SessionState
{

  @Override public void complete(Session session)
  {
    throw new IllegalStateException("Session cannot be completed if it's not booked");
  }

  @Override public void book(Session session)
  {
    session.setState(new Book());
  }

  @Override public void cancel(Session session)
  {
    throw new IllegalStateException("Session cannot be canceled if it wasn't booked");
  }

  @Override
  public void remove(Session session)
  {
    session.setToBeRemoved(true);
  }
}
