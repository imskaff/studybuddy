package server.model;

import java.time.LocalDateTime;

public record Complete() implements SessionState
{
    @Override public String statusName() { return "Complete"; }

    @Override
    public void complete(Session session)
    {
        LocalDateTime now = LocalDateTime.now();

        if(now.isAfter(session.getEnd_date()) || now.isEqual(session.getEnd_date()))
        {
            session.setState(this);
        }
    }

    @Override
    public void book(Session session)
    {
        System.out.println("Session cannot be booked if it's completed");
    }

    @Override
    public void cancel(Session session)
    {
        System.out.println("Session cannot be canceled if it's completed");
    }
}
