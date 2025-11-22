package com.mp3player.presentation.view;

import com.mp3player.data.repository.JavaFXMusicPlayerRepository;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.media.MediaPlayer;
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
    private JavaFXMusicPlayerRepository playerRepository;

    // Audio spectrum data from MediaPlayer
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
        if (playerRepository == null) {
            return;
        }

        MediaPlayer mediaPlayer = playerRepository.getMediaPlayer();
        if (mediaPlayer == null) {
            // No active player, decay to zero
            for (int i = 0; i < spectrumData.length; i++) {
                spectrumData[i] *= 0.8;
            }
            return;
        }

        // Setup listener if not already set for this player
        setupAudioListener(mediaPlayer);
    }

    private void setupAudioListener(MediaPlayer mediaPlayer) {
        if (mediaPlayer == null) {
            return;
        }

        // Configure audio spectrum
        mediaPlayer.setAudioSpectrumNumBands(spectrumData.length);
        mediaPlayer.setAudioSpectrumInterval(0.05); // 50ms refresh

        // Set listener to update spectrum data
        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            for (int i = 0; i < Math.min(spectrumData.length, magnitudes.length); i++) {
                float magnitude = magnitudes[i];
                // Convert dB to 0-1 range (typical range is -60dB to 0dB)
                double normalized = (magnitude + 60.0) / 60.0;
                normalized = Math.max(0.0, Math.min(1.0, normalized));

                // Smooth the data
                double sensitivity = sensitivitySlider.getValue();
                spectrumData[i] = spectrumData[i] * 0.6 + normalized * sensitivity * 0.4;
            }
        });
    }

    public void setPlayerRepository(JavaFXMusicPlayerRepository repository) {
        this.playerRepository = repository;

        if (repository != null && repository.getMediaPlayer() != null) {
            MediaPlayer mediaPlayer = repository.getMediaPlayer();

            // Configure audio spectrum listener
            mediaPlayer.setAudioSpectrumNumBands(spectrumData.length);
            mediaPlayer.setAudioSpectrumInterval(0.05); // 50ms refresh

            // Set listener to update spectrum data
            mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
                for (int i = 0; i < Math.min(spectrumData.length, magnitudes.length); i++) {
                    float magnitude = magnitudes[i];
                    // Convert dB to 0-1 range
                    double normalized = (magnitude + 60.0) / 60.0;
                    normalized = Math.max(0.0, Math.min(1.0, normalized));

                    // Smooth the data
                    double sensitivity = sensitivitySlider.getValue();
                    spectrumData[i] = spectrumData[i] * 0.6 + normalized * sensitivity * 0.4;
                }
            });
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
        // Clamp value to 0-1 range to prevent HSB errors
        value = Math.max(0.0, Math.min(1.0, value));

        switch (currentColorScheme) {
            case "Green Gradient":
                return Color.hsb(120, Math.min(1.0, 0.7 + value * 0.3), Math.min(1.0, 0.4 + value * 0.6));

            case "Blue Gradient":
                return Color.hsb(200, Math.min(1.0, 0.7 + value * 0.3), Math.min(1.0, 0.4 + value * 0.6));

            case "Rainbow":
                double hue = (index / (double) spectrumData.length) * 360;
                return Color.hsb(hue, 0.8, Math.min(1.0, 0.5 + value * 0.5));

            case "Purple Gradient":
                return Color.hsb(280, Math.min(1.0, 0.7 + value * 0.3), Math.min(1.0, 0.4 + value * 0.6));

            case "Fire":
                return Color.hsb(Math.max(0, 30 - value * 30), 1.0, Math.min(1.0, 0.5 + value * 0.5));

            default:
                return Color.web("#4ecca3");
        }
    }

    public void cleanup() {
        stopAnimation();
    }
}
