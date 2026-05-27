package server.mediator;

import server.model.Tutor;

/**
 * Request sent from the client when a user registers a new tutor profile.
 * The registration is pending administrator approval. Rejected by the server
 * if {@code rulesConfirmed} is {@code false}.
 *
 * @author Adam Terelak
 * @author Kamila Jeremicz
 */
public class CreateTutorProfileRequest
{
    private String type = "CREATE_TUTOR_PROFILE";
    private Tutor tutor;
    private boolean rulesConfirmed;

    /**
     * Constructs a {@code CreateTutorProfileRequest}.
     *
     * @param tutor          the tutor data to register
     * @param rulesConfirmed whether the user has confirmed the platform rules
     */
    public CreateTutorProfileRequest(Tutor tutor, boolean rulesConfirmed)
    {
        this.tutor = tutor;
        this.rulesConfirmed = rulesConfirmed;
    }

    /**
     * Returns the request type identifier.
     *
     * @return {@code "CREATE_TUTOR_PROFILE"}
     */
    public String getType() { return type; }

    /**
     * Returns the tutor data to register.
     *
     * @return the tutor
     */
    public Tutor getTutor() { return tutor; }

    /**
     * Returns whether the user has confirmed the platform rules.
     *
     * @return {@code true} if rules were confirmed
     */
    public boolean isRulesConfirmed() { return rulesConfirmed; }

    /**
     * Returns a string representation of this request for debugging purposes.
     *
     * @return a formatted string with the tutor and rules confirmation flag
     */
    @Override
    public String toString()
    {
        return "CREATE_TUTOR_PROFILE{" +
            "tutor=" + tutor +
            ", rulesConfirmed=" + rulesConfirmed +
            '}';
    }
}
