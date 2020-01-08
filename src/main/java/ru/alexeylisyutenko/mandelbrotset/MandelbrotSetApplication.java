package ru.alexeylisyutenko.mandelbrotset;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static ru.alexeylisyutenko.mandelbrotset.Constants.*;

public class MandelbrotSetApplication extends Application {
    private static double pixelXToCoordinate(int x) {
        return (x * (RIGHT - LEFT) / WIDTH) + LEFT;
    }

    private static double pixelYToCoordinate(int y) {
        return (-y * (TOP - BOTTOM) / HEIGHT) + TOP;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Mandelbrot Set");
        primaryStage.setScene(new Scene(createContent(), WIDTH, HEIGHT));
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMaxWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);
        primaryStage.show();
    }

    private Parent createContent() {
        // X boundaries (-2.5, 1).
        // Y boundaries (-1, 1).
        Pane root = new Pane();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        draw(canvas.getGraphicsContext2D());
        root.getChildren().add(canvas);

        return root;
    }

    private void draw(GraphicsContext gc) {
        PixelWriter pixelWriter = gc.getPixelWriter();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Color color = calculateMandelbrotPixelColor(pixelXToCoordinate(x), pixelYToCoordinate(y));
                pixelWriter.setColor(x, y, color);
            }
        }
    }

    private Color calculateMandelbrotPixelColor(double x0, double y0) {
        int maxIterations = 1000;
        int iteration = calculateMandelbrotConvergenceLevel(x0, y0, maxIterations);

        if (iteration == maxIterations) {
            return Color.BLACK;
        } else {
            return Color.LIGHTBLUE;
        }

        // TODO: Try other coloring options. Histogram coloring.
    }

    private int calculateMandelbrotConvergenceLevel(double x0, double y0, int iterations) {
        double x = 0.0;
        double y = 0.0;
        int iteration = 0;
        while ((x * x + y * y <= 2 * 2) && iteration < iterations) {
            double xTemp = x * x - y * y + x0;
            y = 2 * x * y + y0;
            x = xTemp;
            iteration++;
        }
        return iteration;
    }


}
