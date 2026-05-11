package server.model;

import java.time.LocalDateTime;

public record Completed() implements SessionState
{
    @Override
    public void completed(Session session)
    {
        LocalDateTime now = LocalDateTime.now();

        if(now.isAfter(session.getEnd_date()) || now.isEqual(session.getEnd_date()))
        {
            session.setState(this);
        }
    }

    @Override
    public void booked(Session session)
    {
        System.out.println("Session cannot be booked if it's completed");
    }

    @Override
    public void canceled(Session session)
    {
        session.setToBeCanceled(false);
        System.out.println("Session cannot be canceled if it's completed");
    }
}
