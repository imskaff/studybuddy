package client.view;

import client.viewmodel.CreateProfileViewModel;
import javafx.scene.layout.Region;

public class CreateProfileController
{

  private Region root;
  private ViewHandler viewHandler;
  private CreateProfileViewModel viewModel;

  public void init(ViewHandler viewHandler, CreateProfileViewModel viewModel, Region root)
  {
    this.root = root;
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
  }
}
