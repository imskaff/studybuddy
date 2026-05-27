package server.mediator;

import server.model.Tutor;

/**
 * Request sent from the client when a tutor updates their profile information.
 *
 *  @author Adam Terelak
 *  @author Kamila Jeremicz
 */
public class EditTutorProfileRequest
{
    private String type = "EDIT_TUTOR_PROFILE";
    private Tutor tutor;

    /**
     * Constructs an {@code EditTutorProfileRequest} wrapping the updated tutor data.
     *
     * @param tutor the tutor with updated field values
     */
    public EditTutorProfileRequest(Tutor tutor) {
        this.tutor = tutor;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "EDIT_TUTOR_PROFILE"}
     */
    public String getType() { return type; }

    /**
     * Returns the tutor with the updated field values.
     *
     * @return the updated tutor
     */
    public Tutor getTutor() { return tutor; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with the tutor
     */
    @Override
    public String toString()
    {
        return "EDIT_TUTOR_PROFILE{" +
            "tutor=" + tutor + '}';
    }
}
