package server.mediator;

/**
 * A lightweight helper used to identify the type of an incoming JSON request.
 * 
 * The server deserialises every incoming message into this class first in order
 * to read the {@code type} field, then re-deserialises the same JSON into the
 * appropriate concrete request class (e.g. {@link BookSessionRequest},
 * {@link CreateSessionRequest}).
 * 
 *
 * @author
 */
public class RequestType
{
    private String type;

    /**
     * Returns the request type string (e.g. {@code "BOOK_SESSION"}).
     *
     * @return the type identifier
     */
    public String getType()
    {
        return type;
    }
}
