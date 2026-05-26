package server.model;

public interface SessionState
{
  String statusName();
  public void complete(Session session);
  public void book(Session session);
  public void cancel(Session session);
}
