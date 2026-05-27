package client.viewmodel;

import client.model.Model;

/**
 * ViewModel for the platform rules screen shown before user registration.
 * 
 * Currently serves as a placeholder; the view confirms rule acceptance
 * directly through the registration request flag.
 * 
 *
 * @author
 */
public class RulesViewModel
{
    private Model model;

    /**
     * Constructs a {@code RulesViewModel} with the given model.
     *
     * @param model the client-side model
     */
    public RulesViewModel(Model model)
    {
        this.model = model;
    }
}
