package client.viewmodel;

import client.model.Model;

public class CreateTutorProfileViewModel extends CreateProfileViewModel
{
  public CreateTutorProfileViewModel(Model model)
  {
    super(model, UserType.TUTOR);
  }

  public void register(){}
}
