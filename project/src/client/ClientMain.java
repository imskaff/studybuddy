package client;

import client.model.ModelManager;
import client.view.ViewHandler;
import client.viewmodel.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Application entry point for the JavaFX client.
 * 
 * Constructs the client-side object graph (model, view-model factory, view handler),
 * connects to the server, and launches the primary stage.
 * 
 *
 * @author
 */
public class ClientMain extends Application {

    /**
     * JavaFX entry point. Builds the object graph and shows the initial view.
     *
     * @param primaryStage the primary window provided by the JavaFX runtime
     * @throws Exception if the server connection or view initialisation fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ModelManager model = new ModelManager();
        model.connect();
        ViewModelFactory factory = new ViewModelFactory(model);
        ViewHandler viewHandler = new ViewHandler(factory);
        viewHandler.start(primaryStage);
    }

    /**
     * Main method — delegates to {@link Application#launch(String...)}.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
