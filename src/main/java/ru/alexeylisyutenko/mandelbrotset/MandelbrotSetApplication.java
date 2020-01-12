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
        Pane root = new Pane();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        draw(canvas.getGraphicsContext2D());
        root.getChildren().add(canvas);

        return root;
    }

    private void draw(GraphicsContext gc) {
        // Histogram coloring.
        int maxIterations = 1000;

        // 1st pass.
        int[][] iterationCounts = new int[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                iterationCounts[x][y] = calculateMandelbrotConvergenceLevel(pixelXToCoordinate(x), pixelYToCoordinate(y), maxIterations);
            }
        }

        // 2nd pass.
        int[] numIterationsPerPixel = new int[maxIterations + 1];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                numIterationsPerPixel[iterationCounts[x][y]]++;
            }
        }

        // 3rd pass.
        int total = 0;
        for (int i = 0; i < numIterationsPerPixel.length; i++) {
            total += numIterationsPerPixel[i];
        }

        // 4th pass.
        double[][] hue = new double[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int iteration = iterationCounts[x][y];
                for (int i = 0; i <= iteration; i++) {
                    hue[x][y] += (double) numIterationsPerPixel[i] / total;
                }
            }
        }

        // 5th pass.
        PixelWriter pixelWriter = gc.getPixelWriter();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Color color = Color.LIGHTBLUE.interpolate(Color.BLACK, Math.min(hue[x][y], 1.0));
                pixelWriter.setColor(x, y, color);
            }
        }
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
