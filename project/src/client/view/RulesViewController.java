package client.view;

import client.viewmodel.RulesViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class RulesViewController
{
  private Region root;
  private ViewHandler viewHandler;
  private RulesViewModel viewModel;

  @FXML private Button back_btn;

  public RulesViewController()
  {}
  public void init(ViewHandler viewHandler, RulesViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    back_btn.setOnAction(event -> viewHandler.openCreateNewProfile());
  }

  public Region getRoot()
  {
    return root;
  }
}
