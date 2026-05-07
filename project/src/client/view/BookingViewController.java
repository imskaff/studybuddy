package client.view;

import client.viewmodel.BookingViewModel;
import javafx.scene.layout.Region;

public class BookingViewController {
    // fxml shit \/

    // end

    private Region root;
    private ViewHandler viewHandler;
    private BookingViewModel viewModel;

    public BookingViewController() {}

    public void init(ViewHandler viewHandler, BookingViewModel viewModel, Region root) {
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
