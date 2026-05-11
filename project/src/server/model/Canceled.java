package server.model;

public record Canceled() implements SessionState {

 @Override
 public void completed(Session session)
 {
     System.out.println("Session cannot be completed if it was canceled");
 }

 @Override
 public void booked(Session session)
 {
     System.out.println("Session cannot be booked if it was canceled");
 }

 @Override
    public void canceled(Session session)
 {
    session.setState(this);
 }

}

