package com.mp3player.presentation.view;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.util.Random;

public class VisualizerController {

    @FXML private Canvas visualizerCanvas;
    @FXML private ComboBox<String> visualizerTypeComboBox;
    @FXML private ComboBox<String> colorSchemeComboBox;
    @FXML private CheckBox enableCheckBox;
    @FXML private Slider sensitivitySlider;
    @FXML private Label sensitivityLabel;

    private GraphicsContext gc;
    private AnimationTimer animationTimer;
    private Random random = new Random();

    // Simulated audio data
    private double[] spectrumData = new double[64];
    private int currentType = 0;
    private String currentColorScheme = "Green Gradient";

    @FXML
    public void initialize() {
        gc = visualizerCanvas.getGraphicsContext2D();

        // Setup ComboBox items
        visualizerTypeComboBox.getItems().addAll(
            "Spectrum Bars", "Waveform", "Circular", "Particle Effect"
        );
        colorSchemeComboBox.getItems().addAll(
            "Green Gradient", "Blue Gradient", "Rainbow", "Purple Gradient", "Fire"
        );

        // Initialize with default values
        visualizerTypeComboBox.setValue("Spectrum Bars");
        colorSchemeComboBox.setValue("Green Gradient");

        // Setup listeners
        visualizerTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            switch (newVal) {
                case "Spectrum Bars": currentType = 0; break;
                case "Waveform": currentType = 1; break;
                case "Circular": currentType = 2; break;
                case "Particle Effect": currentType = 3; break;
            }
        });

        colorSchemeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentColorScheme = newVal;
        });

        sensitivitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            sensitivityLabel.setText(String.format("%.1fx", newVal.doubleValue()));
        });

        enableCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                startAnimation();
            } else {
                stopAnimation();
                clearCanvas();
            }
        });

        // Start animation
        startAnimation();
    }

    private void startAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (enableCheckBox.isSelected()) {
                    updateSpectrumData();
                    draw();
                }
            }
        };
        animationTimer.start();
    }

    private void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    private void updateSpectrumData() {
        // Simulate audio spectrum data (in real implementation, get from MediaPlayer)
        double sensitivity = sensitivitySlider.getValue();
        for (int i = 0; i < spectrumData.length; i++) {
            // Smooth decay
            spectrumData[i] *= 0.85;
            // Add random peaks to simulate audio
            if (random.nextDouble() > 0.7) {
                spectrumData[i] = Math.min(1.0, spectrumData[i] + random.nextDouble() * 0.5 * sensitivity);
            }
        }
    }

    private void draw() {
        clearCanvas();

        switch (currentType) {
            case 0: drawSpectrumBars(); break;
            case 1: drawWaveform(); break;
            case 2: drawCircular(); break;
            case 3: drawParticleEffect(); break;
        }
    }

    private void clearCanvas() {
        gc.setFill(Color.rgb(15, 15, 30));
        gc.fillRect(0, 0, visualizerCanvas.getWidth(), visualizerCanvas.getHeight());
    }

    private void drawSpectrumBars() {
        double width = visualizerCanvas.getWidth();
        double height = visualizerCanvas.getHeight();
        double barWidth = width / spectrumData.length;

        for (int i = 0; i < spectrumData.length; i++) {
            double barHeight = spectrumData[i] * height * 0.8;
            double x = i * barWidth;
            double y = height - barHeight;

            gc.setFill(getColorForValue(spectrumData[i], i));
            gc.fillRoundRect(x + 1, y, barWidth - 2, barHeight, 3, 3);
        }
    }

    private void drawWaveform() {
        double width = visualizerCanvas.getWidth();
        double height = visualizerCanvas.getHeight();
        double centerY = height / 2;

        gc.setStroke(getColorForValue(0.5, 0));
        gc.setLineWidth(2);
        gc.beginPath();
        gc.moveTo(0, centerY);

        for (int i = 0; i < spectrumData.length; i++) {
            double x = (i / (double) spectrumData.length) * width;
            double y = centerY + (spectrumData[i] - 0.5) * height * 0.6;
            gc.lineTo(x, y);
        }

        gc.stroke();
    }

    private void drawCircular() {
        double centerX = visualizerCanvas.getWidth() / 2;
        double centerY = visualizerCanvas.getHeight() / 2;
        double baseRadius = Math.min(centerX, centerY) * 0.3;

        for (int i = 0; i < spectrumData.length; i++) {
            double angle = (i / (double) spectrumData.length) * 2 * Math.PI;
            double radius = baseRadius + spectrumData[i] * 80;

            double x1 = centerX + Math.cos(angle) * baseRadius;
            double y1 = centerY + Math.sin(angle) * baseRadius;
            double x2 = centerX + Math.cos(angle) * radius;
            double y2 = centerY + Math.sin(angle) * radius;

            gc.setStroke(getColorForValue(spectrumData[i], i));
            gc.setLineWidth(3);
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    private void drawParticleEffect() {
        double width = visualizerCanvas.getWidth();
        double height = visualizerCanvas.getHeight();

        for (int i = 0; i < spectrumData.length; i++) {
            if (spectrumData[i] > 0.3) {
                int particleCount = (int) (spectrumData[i] * 5);
                for (int j = 0; j < particleCount; j++) {
                    double x = (i / (double) spectrumData.length) * width;
                    double y = height - random.nextDouble() * spectrumData[i] * height;
                    double size = 3 + random.nextDouble() * 4;

                    gc.setFill(getColorForValue(spectrumData[i], i));
                    gc.fillOval(x + random.nextDouble() * 20 - 10, y, size, size);
                }
            }
        }
    }

    private Color getColorForValue(double value, int index) {
        switch (currentColorScheme) {
            case "Green Gradient":
                return Color.hsb(120, 0.7 + value * 0.3, 0.4 + value * 0.6);

            case "Blue Gradient":
                return Color.hsb(200, 0.7 + value * 0.3, 0.4 + value * 0.6);

            case "Rainbow":
                double hue = (index / (double) spectrumData.length) * 360;
                return Color.hsb(hue, 0.8, 0.5 + value * 0.5);

            case "Purple Gradient":
                return Color.hsb(280, 0.7 + value * 0.3, 0.4 + value * 0.6);

            case "Fire":
                return Color.hsb(30 - value * 30, 1.0, 0.5 + value * 0.5);

            default:
                return Color.web("#4ecca3");
        }
    }

    public void cleanup() {
        stopAnimation();
    }
}
