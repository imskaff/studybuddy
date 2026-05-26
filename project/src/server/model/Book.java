package server.model;

import java.time.LocalDateTime;

public record Book() implements SessionState
{
    @Override public String statusName() { return "Booked"; }

    @Override
    public void complete(Session session)
    {
        complete(session, LocalDateTime.now());
    }

    void complete(Session session, LocalDateTime now)
    {
        if(now.isAfter(session.getEnd_date()) || now.isEqual(session.getEnd_date()))
        {
            session.setState(new Complete());
        }
    }

    @Override
    public void book(Session session)
    {
        session.setState(this);
    }

    @Override
    public void cancel(Session session)
    {
        session.setState(new Cancel());
    }
}
