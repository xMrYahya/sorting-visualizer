package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private VisualizerView visualizer;

    @Override
    public void start(Stage stage) {
        visualizer = new VisualizerView();

        MenuItem openParams = new MenuItem("Settings…");
        openParams.setOnAction(e -> {
            ParametersDialog dialog = new ParametersDialog(stage);
            dialog.showAndWait().ifPresent(params -> {
                visualizer.setAlgorithm(params.algorithm);
                visualizer.setData(params.data);
                visualizer.setDelay(params.delayMs);
            });
        });

        Menu menu = new Menu("Settings");
        menu.getItems().addAll(openParams);
        MenuBar bar = new MenuBar(menu);

        BorderPane root = new BorderPane();
        root.setTop(bar);
        root.setCenter(visualizer);

        Scene scene = new Scene(root, 900, 520);
        stage.setTitle("Sorting Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}