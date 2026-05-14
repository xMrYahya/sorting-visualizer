package app;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Optional;

public class ParametersDialog extends Dialog<ParametersDialog.Params> {

    public static class Params {
        public final int[] data;
        public final String algorithm;
        public final int delayMs;
        public Params(int[] data, String algorithm, int delayMs) {
            this.data = data; this.algorithm = algorithm; this.delayMs = delayMs;
        }
    }

    private final ComboBox<String> algoBox = new ComboBox<>();
    private final TextField dataField = new TextField();
    private final ComboBox<String> speedBox = new ComboBox<>();

    public ParametersDialog(Stage owner) {
        setTitle("Paramètres de tri");
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);

        algoBox.getItems().addAll("Quick sort", "Merge sort");
        algoBox.getSelectionModel().selectFirst();

        dataField.setPromptText("50,87,56,12,75,100,20,34,9");
        dataField.setText("50,87,56,12,75,100,20,34,9");

        speedBox.getItems().addAll("Lente", "Moyenne", "Rapide");
        speedBox.getSelectionModel().select("Rapide");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(15));
        grid.add(new Label("Algorithme de tri :"), 0, 0);
        grid.add(algoBox, 1, 0);
        grid.add(new Label("Collection d'entiers à trier :"), 0, 1);
        grid.add(dataField, 1, 1);
        grid.add(new Label("Vitesse de la simulation :"), 0, 2);
        grid.add(speedBox, 1, 2);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    int[] data = Arrays.stream(dataField.getText().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    if (data.length == 0) throw new IllegalArgumentException("Liste vide");

                    String algo = Optional.ofNullable(algoBox.getValue()).orElse("Quick sort");
                    String speed = Optional.ofNullable(speedBox.getValue()).orElse("Rapide");
                    int delay = switch (speed) {
                        case "Lente" -> 400;
                        case "Moyenne" -> 200;
                        default -> 60;
                    };
                    return new Params(data, algo, delay);
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Entrée invalide : " + ex.getMessage(), ButtonType.OK).showAndWait();
                    return null;
                }
            }
            return null;
        });
    }
}