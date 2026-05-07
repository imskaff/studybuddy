package client.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import client.viewmodel.TutorMainPageViewModel;

public class TutorMainPageViewController {
    // fxml shit \/

    // end

    private Region root;
    private ViewHandler viewHandler;
    private TutorMainPageViewModel viewModel;

    public TutorMainPageViewController() {}

    public void init(ViewHandler viewHandler, TutorMainPageViewModel viewModel, Region root) {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;

        // whatever fxml needs here \/
        
    }

    public Region getRoot() {
        return root;
    }

    // fxml methods here \/
}
