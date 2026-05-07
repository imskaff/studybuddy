package client.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import client.viewmodel.WelcomeViewModel;

public class WelcomeViewController {
    // fxml shit \/

    // end

    private Region root;
    private WelcomeViewModel viewModel;
    private ViewHandler viewHandler;

    public WelcomeViewController(){}

    public void init(ViewHandler viewHandler, WelcomeViewModel viewModel, Region root) {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;

        // whatever fxml needs here \/

    }

    public Region getRoot(){
        return root;
    }

    // fxml methods here \/
}
