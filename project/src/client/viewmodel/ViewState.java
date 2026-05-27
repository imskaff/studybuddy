package client.viewmodel;

import server.model.Session;

/**
 * Shared state object that holds the currently selected {@link Session}
 * across multiple ViewModels.
 * 
 * Passed by reference through the {@link ViewModelFactory} so that one
 * ViewModel can set the selection and another can read it without coupling.
 * 
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class ViewState
{
    private Session selectedSession;

    /**
     * Constructs a {@code ViewState} with no session selected.
     */
    public ViewState()
    {
        this.selectedSession = null;
    }

    /**
     * Returns the currently selected session.
     *
     * @return the selected {@link Session}, or {@code null} if none is selected
     */
    public Session getSelectedSession()
    {
        return selectedSession;
    }

    /**
     * Sets the currently selected session.
     *
     * @param selectSession the session to select
     */
    public void setSelectedSession(Session selectSession)
    {
        this.selectedSession = selectSession;
    }
}
