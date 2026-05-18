package server.mediator;

import server.model.Tutor;

public class EditTutorProfileRequest
{
  private String type = "EDIT_TUTOR_PROFILE";
  private Tutor tutor;

  public EditTutorProfileRequest(Tutor tutor){
    this.tutor = tutor;
  }

  public String getType(){
    return type;
  }

  public Tutor getTutor(){
    return tutor;
  }

  @Override
  public String toString()
  {
    return "EDIT_TUTOR_PROFILE{" +
        "tutor=" + tutor + '}';
  }
}
