package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.CreateTutorProfileViewModel;
import javafx.scene.layout.Region;

public class CreateTutorProfileController
{
  private Region root;
  private ViewHandler viewHandler;
  private CreateTutorProfileViewModel viewModel;

  public CreateTutorProfileController() {}

  public void init(ViewHandler viewHandler, CreateTutorProfileViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;
  }
}
