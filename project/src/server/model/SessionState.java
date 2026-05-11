package server.model;

public interface SessionState
{
  public void completed(Session session);
  public void booked(Session session);
  public void canceled(Session session);
}
