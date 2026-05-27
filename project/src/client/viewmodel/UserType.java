package client.viewmodel;

/**
 * Enum representing the two self-registration user types in the application.
 * Used by {@link CreateProfileViewModel} to distinguish whether a registration
 * form belongs to a student or a tutor.
 *
 * @author
 */
public enum UserType
{
    /** Represents a student user. */
    STUDENT,

    /** Represents a tutor user. */
    TUTOR;
}
