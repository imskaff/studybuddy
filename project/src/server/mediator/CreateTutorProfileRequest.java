package server.mediator;

import server.model.Tutor;

public class CreateTutorProfileRequest
{
  // Identifies the request type
  private String type = "CREATE_TUTOR_PROFILE";

  // Tutor object containing the registration information
  private Tutor tutor;

  // Indicates whether the user confirmed the rules
  private boolean rulesConfirmed;

  public CreateTutorProfileRequest(Tutor tutor,
      boolean rulesConfirmed)
  {
    this.tutor = tutor;
    this.rulesConfirmed = rulesConfirmed;
  }

  public String getType()
  {
    return type;
  }

  public Tutor getTutor()
  {
    return tutor;
  }

  public boolean isRulesConfirmed()
  {
    return rulesConfirmed;
  }

  @Override
  public String toString()
  {
    return "CREATE_TUTOR_PROFILE{" +
        "tutor=" + tutor +
        ", rulesConfirmed=" + rulesConfirmed +
        '}';
  }
}
