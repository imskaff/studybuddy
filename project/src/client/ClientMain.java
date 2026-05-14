package client;

import client.model.ModelManager;
import client.view.ViewHandler;
import client.viewmodel.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {

    // client starts here

    @Override
    public void start(Stage primaryStage) throws Exception {
        ModelManager model = new ModelManager();
        model.connect();
        ViewModelFactory factory = new ViewModelFactory(model);
        ViewHandler viewHandler = new ViewHandler(factory);
        viewHandler.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
