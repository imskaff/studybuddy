package client.view;

import client.viewmodel.RulesViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

/**
 * JavaFX controller for the platform-rules screen.
 * 
 * Displays the terms / rules of the platform. The only action available is
 * navigating back to the profile-type selection screen via the back button.
 * 
 *
 * @author Kamila Jeremicz
 */
public class RulesViewController
{
  private Region root;
  private ViewHandler viewHandler;
  private RulesViewModel viewModel;

  @FXML private Button back_btn;

  /** No-arg constructor required by the JavaFX FXML loader. */
  public RulesViewController()
  {}

  /**
   * Initialises the controller and wires the back button to return to the
   * create-new-profile screen.
   *
   * @param viewHandler the application navigation handler
   * @param viewModel   the rules ViewModel
   * @param root        the root layout node
   */
  public void init(ViewHandler viewHandler, RulesViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    this.root = root;

    back_btn.setOnAction(event -> viewHandler.openCreateNewProfile());
  }

  /**
   * Returns the root layout node.
   *
   * @return the root {@link Region}
   */
  public Region getRoot()
  {
    return root;
  }
}
