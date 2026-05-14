package app;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

import sort.*;

import java.util.Arrays;

public class VisualizerView extends BorderPane {

    private final Canvas canvas = new Canvas(800, 420);
    private final Button startBtn = new Button("Démarrer");
    private final Button stopBtn = new Button("Arrêt");
    private Thread worker;

    private int[] data = new int[0];
    private String algorithm = "Quick sort";
    private int delayMs = 100;

    public VisualizerView() {
        setPadding(new Insets(10));
        setCenter(canvas);

        startBtn.setOnAction(e -> start());
        stopBtn.setOnAction(e -> stop());
        stopBtn.setDisable(true);

        ToolBar tb = new ToolBar(startBtn, stopBtn);
        setBottom(tb);

        drawBars(data, -1, -1);
        widthProperty().addListener((obs, o, n) -> canvas.setWidth(getWidth() - 20));
        heightProperty().addListener((obs, o, n) -> canvas.setHeight(getHeight() - 70));
    }

    public void setData(int[] data) {
        this.data = Arrays.copyOf(data, data.length);
        drawBars(this.data, -1, -1);
    }
    public void setAlgorithm(String name) { this.algorithm = name; }
    public void setDelay(int ms) { this.delayMs = ms; }

    public void start() {
        if (data == null || data.length == 0 || worker != null) return;

        startBtn.setDisable(true);
        stopBtn.setDisable(false);

        Sorter sorter = switch (algorithm) {
            case "Merge sort" -> new MergeSort();
            case "Insertion sort" -> new InsertionSort();
            default -> new QuickSort();
        };

        worker = new Thread(() -> {
            try {
                sorter.sort(Arrays.copyOf(data, data.length), this::updateFromSorter, delayMs);
            } catch (InterruptedException ignored) {
            } finally {
                Platform.runLater(() -> {
                    if (sorter.getArray() != null) {
                        drawBars(sorter.getArray(), -1, -1);
                    }
                    startBtn.setDisable(false);
                    stopBtn.setDisable(true);
                });
                worker = null;
            }
        });
        worker.setDaemon(true);
        worker.start();
    }

    public void stop() {
        if (worker != null) {
            worker.interrupt();
        }
    }

    private void updateFromSorter(int[] a, int i, int j) {
        int[] snapshot = Arrays.copyOf(a, a.length);
        Platform.runLater(() -> drawBars(snapshot, i, j));
        try {
            Thread.sleep(Math.max(0, delayMs));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void drawBars(int[] a, int iHi, int jHi) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double w = canvas.getWidth(), h = canvas.getHeight();
        g.clearRect(0, 0, w, h);

        if (a == null || a.length == 0) return;

        double topMargin = 20;
        double bottomMargin = 40;
        double leftMargin = 20;
        double rightMargin = 20;

        int max = Arrays.stream(a).max().orElse(1);
        double innerW = Math.max(1, w - leftMargin - rightMargin);
        double innerH = Math.max(1, h - topMargin - bottomMargin);

        double step = innerW / a.length;
        double bw = Math.max(1, step * 0.8);

        g.setStroke(javafx.scene.paint.Color.GRAY);
        g.setLineWidth(1);
        double baselineY = h - bottomMargin;
        g.strokeLine(leftMargin, baselineY, w - rightMargin, baselineY);

        double fontSize = Math.max(10, Math.min(18, step * 0.5));
        g.setFont(Font.font(fontSize));
        g.setTextAlign(TextAlignment.CENTER);
        g.setTextBaseline(VPos.TOP);

        for (int idx = 0; idx < a.length; idx++) {
            double xCenter = leftMargin + idx * step + step / 2.0;
            double bh = (a[idx] / (double) max) * innerH;
            double x = xCenter - bw / 2.0;
            double y = baselineY - bh;

            if (idx == iHi || idx == jHi) {
                g.setFill(javafx.scene.paint.Color.MEDIUMPURPLE);
            } else {
                g.setFill(javafx.scene.paint.Color.GREY);
            }
            g.fillRect(x, y, bw, bh);

            g.setFill(javafx.scene.paint.Color.DIMGRAY);
            g.fillText(String.valueOf(a[idx]), xCenter, baselineY + 4);
        }
    }
}
