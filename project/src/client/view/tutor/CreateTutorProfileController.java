package client.view.tutor;

import client.view.ViewHandler;
import client.viewmodel.tutor.CreateTutorProfileViewModel;
import javafx.scene.layout.Region;

public class CreateTutorProfileController
{
  private CreateTutorProfileViewModel viewModel;
  private ViewHandler viewHandler;
  private Region root;

  public void init(ViewHandler viewHandler, CreateTutorProfileViewModel createTutorViewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;
  }
}
