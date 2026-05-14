package client.viewmodel;

import server.model.Session;

public class ViewState
{
    private Session selectedSession;

    public ViewState()
    {
      this.selectedSession = null;
    }

    public Session getSelectedSession()
    {
      return selectedSession;
    }

    public void setSelectedSession(Session selectSession)
    {
      this.selectedSession = selectSession;
    }
  }

