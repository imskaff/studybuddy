package client.view;

import client.viewmodel.CreateNewProfileViewModel;
import javafx.scene.layout.Region;

public class CreateNewProfileController
{

  private Region root;
  private ViewHandler viewHandler;
  private CreateNewProfileViewModel viewModel;

  public void init(ViewHandler viewHandler, CreateNewProfileViewModel viewModel, Region root)
  {
    this.root = root;
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
  }
}
