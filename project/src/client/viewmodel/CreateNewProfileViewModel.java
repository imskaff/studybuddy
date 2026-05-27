package client.viewmodel;

import client.model.Model;

/**
 * ViewModel for the "create new profile" selection screen, where the user
 * chooses whether to register as a student or a tutor.
 *
 * @author Kamila Jeremicz
 */
public class CreateNewProfileViewModel
{
    private Model model;

    /**
     * Constructs a {@code CreateNewProfileViewModel} with the given model.
     *
     * @param model the client-side model
     */
    public CreateNewProfileViewModel(Model model)
    {
        this.model = model;
    }
}
