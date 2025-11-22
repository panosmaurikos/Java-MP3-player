package com.mp3player.presentation.view;

import com.mp3player.data.repository.JavaFXMusicPlayerRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;

public class EqualizerController {

    @FXML private ComboBox<String> presetComboBox;
    @FXML private CheckBox enableCheckBox;

    private JavaFXMusicPlayerRepository playerRepository;

    // Frequency band sliders
    @FXML private Slider slider32Hz;
    @FXML private Slider slider64Hz;
    @FXML private Slider slider125Hz;
    @FXML private Slider slider250Hz;
    @FXML private Slider slider500Hz;
    @FXML private Slider slider1kHz;
    @FXML private Slider slider2kHz;
    @FXML private Slider slider4kHz;
    @FXML private Slider slider8kHz;
    @FXML private Slider slider16kHz;

    // Labels for displaying dB values
    @FXML private Label label32Hz;
    @FXML private Label label64Hz;
    @FXML private Label label125Hz;
    @FXML private Label label250Hz;
    @FXML private Label label500Hz;
    @FXML private Label label1kHz;
    @FXML private Label label2kHz;
    @FXML private Label label4kHz;
    @FXML private Label label8kHz;
    @FXML private Label label16kHz;

    private Slider[] sliders;
    private Label[] labels;

    // Preset values (in dB)
    private static final Map<String, double[]> PRESETS = new HashMap<>();

    static {
        PRESETS.put("Flat", new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        PRESETS.put("Pop", new double[]{-1, 1, 3, 4, 3, 1, -1, -2, -2, -1});
        PRESETS.put("Rock", new double[]{4, 3, 2, 1, -1, -2, -1, 1, 3, 4});
        PRESETS.put("Jazz", new double[]{3, 2, 1, 1, -1, -1, 0, 1, 2, 3});
        PRESETS.put("Classical", new double[]{4, 3, 2, 0, 0, 0, -2, -2, -2, -3});
        PRESETS.put("Bass Boost", new double[]{6, 5, 4, 3, 1, 0, 0, 0, 0, 0});
        PRESETS.put("Treble Boost", new double[]{0, 0, 0, 0, 1, 3, 4, 5, 6, 7});
        PRESETS.put("Vocal", new double[]{-2, -1, 1, 3, 4, 4, 3, 1, 0, -1});
    }

    @FXML
    public void initialize() {
        // Initialize slider and label arrays
        sliders = new Slider[]{
            slider32Hz, slider64Hz, slider125Hz, slider250Hz, slider500Hz,
            slider1kHz, slider2kHz, slider4kHz, slider8kHz, slider16kHz
        };

        labels = new Label[]{
            label32Hz, label64Hz, label125Hz, label250Hz, label500Hz,
            label1kHz, label2kHz, label4kHz, label8kHz, label16kHz
        };

        // Setup preset ComboBox items
        presetComboBox.getItems().addAll(
            "Flat", "Pop", "Rock", "Jazz", "Classical",
            "Bass Boost", "Treble Boost", "Vocal"
        );

        // Setup listeners for each slider
        for (int i = 0; i < sliders.length; i++) {
            final int index = i;
            sliders[i].valueProperty().addListener((obs, oldVal, newVal) -> {
                updateLabel(index, newVal.doubleValue());
                updateRepositoryEqualizer();
            });
        }

        // Setup preset selector
        presetComboBox.setValue("Flat");
        presetComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                applyPreset(newVal);
            }
        });

        // Setup enable/disable checkbox
        enableCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            setEqualizerEnabled(newVal);
            updateRepositoryEqualizer();
        });
    }

    public void setPlayerRepository(JavaFXMusicPlayerRepository repository) {
        this.playerRepository = repository;
    }

    private void updateLabel(int index, double value) {
        String sign = value >= 0 ? "+" : "";
        labels[index].setText(String.format("%s%.1f dB", sign, value));
    }

    private void applyPreset(String presetName) {
        double[] values = PRESETS.get(presetName);
        if (values != null) {
            for (int i = 0; i < sliders.length && i < values.length; i++) {
                sliders[i].setValue(values[i]);
            }
        }
    }

    private void setEqualizerEnabled(boolean enabled) {
        for (Slider slider : sliders) {
            slider.setDisable(!enabled);
        }
        presetComboBox.setDisable(!enabled);

        if (!enabled) {
            // Reset all to 0 when disabled
            for (Slider slider : sliders) {
                slider.setValue(0);
            }
        }
    }

    @FXML
    private void onReset() {
        presetComboBox.setValue("Flat");
        applyPreset("Flat");
    }

    // Getter methods for external access
    public double[] getBandValues() {
        double[] values = new double[sliders.length];
        for (int i = 0; i < sliders.length; i++) {
            values[i] = sliders[i].getValue();
        }
        return values;
    }

    public boolean isEnabled() {
        return enableCheckBox.isSelected();
    }

    private void updateRepositoryEqualizer() {
        if (playerRepository != null) {
            playerRepository.setEqualizerEnabled(enableCheckBox.isSelected());
            playerRepository.setAllEqualizerBands(getBandValues());
        }
    }
}
