package server.model;

public record Cancel() implements SessionState {

 @Override public String statusName() { return "Cancel"; }

 @Override
 public void complete(Session session)
 {
     System.out.println("Session cannot be completed if it was canceled");
 }

 @Override
 public void book(Session session)
 {
     System.out.println("Session cannot be booked if it was canceled");
 }

 @Override
    public void cancel(Session session)
 {
    session.setState(this);
 }

  @Override
  public void remove(Session session)
  {
    session.setToBeRemoved(true);
  }

}

