package server.model;

import java.time.LocalDateTime;

public record Booked() implements SessionState
{
    @Override
    public void completed(Session session)
    {
        completed(session, LocalDateTime.now());
    }

    void completed(Session session, LocalDateTime now)
    {
        if(now.isAfter(session.getEnd_date()) || now.isEqual(session.getEnd_date()))
        {
            session.setState(new Completed());
        }
    }

    @Override
    public void booked(Session session)
    {
        session.setState(this);
    }

    @Override
    public void canceled(Session session)
    {
        session.setToBeCanceled(true);
    }
}
