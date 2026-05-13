package client.viewmodel;

import client.model.Model;

public class CreateStudentProfileViewModel extends CreateProfileViewModel
{
  public CreateStudentProfileViewModel(Model model, UserType type){
    super(model, UserType.STUDENT);
  }

  public void register(){}
}
