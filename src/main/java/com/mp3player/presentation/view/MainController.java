package com.mp3player.presentation.view;

import com.mp3player.data.repository.FilePlaylistRepository;
import com.mp3player.data.repository.JavaFXMusicPlayerRepository;
import com.mp3player.domain.entity.Song;
import com.mp3player.domain.usecase.*;
import com.mp3player.presentation.viewmodel.MusicPlayerViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.io.File;

public class MainController {
    // UI Components
    @FXML private ListView<Song> playlistView;
    @FXML private ListView<Song> favoritesView;
    @FXML private TabPane playlistTabs;
    @FXML private ComboBox<com.mp3player.domain.entity.PlaylistEntity> playlistComboBox;
    @FXML private Label currentSongTitleLabel;
    @FXML private Label currentSongArtistLabel;
    @FXML private Label currentTimeLabel;
    @FXML private Label totalTimeLabel;
    @FXML private Label volumePercentLabel;
    @FXML private Label volumeIcon;
    @FXML private Label speedLabel;
    @FXML private Slider progressSlider;
    @FXML private Slider volumeSlider;
    @FXML private Slider speedSlider;
    @FXML private TextField searchField;
    @FXML private ToggleButton shuffleButton;
    @FXML private ToggleButton repeatButton;
    @FXML private Button playPauseButton;
    @FXML private Button favoriteButton;
    @FXML private CheckMenuItem shuffleMenuItem;
    @FXML private CheckMenuItem repeatMenuItem;
    @FXML private CheckMenuItem equalizerMenuItem;
    @FXML private CheckMenuItem visualizerMenuItem;

    private MusicPlayerViewModel viewModel;
    private JavaFXMusicPlayerRepository playerRepository;
    private Timeline progressTimeline;

    @FXML
    public void initialize() {
        setupDependencies();
        setupBindings();
        setupProgressUpdater();
        setupSeekFunctionality();
    }

    private void setupDependencies() {
        // Initialize repositories
        playerRepository = new JavaFXMusicPlayerRepository();
        FilePlaylistRepository playlistRepository = new FilePlaylistRepository();

        // Initialize use cases
        PlaySongUseCase playSongUseCase = new PlaySongUseCase(playerRepository);
        PauseSongUseCase pauseSongUseCase = new PauseSongUseCase(playerRepository);
        ResumeSongUseCase resumeSongUseCase = new ResumeSongUseCase(playerRepository);
        StopSongUseCase stopSongUseCase = new StopSongUseCase(playerRepository);
        SetVolumeUseCase setVolumeUseCase = new SetVolumeUseCase(playerRepository);
        SetPlaybackSpeedUseCase setPlaybackSpeedUseCase = new SetPlaybackSpeedUseCase(playerRepository);
        LoadSongsUseCase loadSongsUseCase = new LoadSongsUseCase(playlistRepository);
        SavePlaylistUseCase savePlaylistUseCase = new SavePlaylistUseCase();
        LoadPlaylistFileUseCase loadPlaylistFileUseCase = new LoadPlaylistFileUseCase();

        // Initialize ViewModel
        viewModel = new MusicPlayerViewModel(
                playSongUseCase,
                pauseSongUseCase,
                resumeSongUseCase,
                stopSongUseCase,
                setVolumeUseCase,
                setPlaybackSpeedUseCase,
                loadSongsUseCase,
                savePlaylistUseCase,
                loadPlaylistFileUseCase
        );

        // Setup auto-advance listener for when songs end
        playerRepository.setOnEndOfMediaListener(() -> {
            if (viewModel.isRepeatProperty().get()) {
                // Replay current song
                viewModel.playCurrentSong();
            } else {
                // Advance to next song (handles shuffle internally)
                viewModel.nextSong();
            }
        });
    }

    private void setupBindings() {
        // Bind playlists ComboBox
        playlistComboBox.setItems(viewModel.getPlaylists());
        playlistComboBox.valueProperty().bindBidirectional(viewModel.currentPlaylistEntityProperty());

        // Bind playlist to ListView
        playlistView.setItems(viewModel.getSongs());

        // Bind favorites to ListView
        favoritesView.setItems(viewModel.getFavoriteSongs());

        // Handle double-click to play song (All Songs tab)
        playlistView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int selectedIndex = playlistView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    viewModel.selectSong(selectedIndex);
                }
            }
        });

        // Handle double-click to play song (Favorites tab)
        favoritesView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Song selectedSong = favoritesView.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    viewModel.playSong(selectedSong);
                }
            }
        });

        // Setup context menu for deleting songs (All Songs tab)
        setupPlaylistContextMenu();

        // Setup context menu for deleting songs (Favorites tab)
        setupFavoritesContextMenu();

        // Bind current song info
        viewModel.currentSongProperty().addListener((obs, oldSong, newSong) -> {
            if (newSong != null) {
                currentSongTitleLabel.setText(newSong.getTitle());
                currentSongArtistLabel.setText(newSong.getArtist());
            } else {
                currentSongTitleLabel.setText("No song selected");
                currentSongArtistLabel.setText("Unknown Artist");
            }
        });

        // Bind time labels
        viewModel.currentTimeStringProperty().addListener((obs, oldVal, newVal) ->
                currentTimeLabel.setText(newVal));
        viewModel.totalTimeStringProperty().addListener((obs, oldVal, newVal) ->
                totalTimeLabel.setText(newVal));

        // Bind progress slider (display only, seek is handled separately)
        viewModel.progressProperty().addListener((obs, oldVal, newVal) -> {
            if (!progressSlider.isValueChanging()) {
                progressSlider.setValue(newVal.doubleValue() * 100);
            }
        });

        // Bind volume slider
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            viewModel.setVolume(newVal.doubleValue());
            updateVolumeDisplay(newVal.doubleValue());
        });

        // Bind search field
        searchField.textProperty().bindBidirectional(viewModel.searchTextProperty());

        // Bind shuffle and repeat buttons
        shuffleButton.selectedProperty().bindBidirectional(viewModel.isShuffledProperty());
        repeatButton.selectedProperty().bindBidirectional(viewModel.isRepeatProperty());

        // Make shuffle and repeat mutually exclusive
        shuffleButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal && repeatButton.isSelected()) {
                repeatButton.setSelected(false);
            }
        });
        repeatButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal && shuffleButton.isSelected()) {
                shuffleButton.setSelected(false);
            }
        });

        // Bind play/pause button text
        viewModel.isPlayingProperty().addListener((obs, oldVal, newVal) -> {
            playPauseButton.setText(newVal ? "⏸" : "▶");
        });

        // Bind speed slider
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            viewModel.setPlaybackSpeed(newVal.doubleValue());
            speedLabel.setText(String.format("%.1fx", newVal.doubleValue()));
        });

        // Bind menu items to toggle buttons
        shuffleMenuItem.selectedProperty().bindBidirectional(shuffleButton.selectedProperty());
        repeatMenuItem.selectedProperty().bindBidirectional(repeatButton.selectedProperty());

        // Update favorite button based on current song
        viewModel.currentSongProperty().addListener((obs, oldSong, newSong) -> {
            updateFavoriteButton(newSong);
        });

        // Initialize volume display
        updateVolumeDisplay(volumeSlider.getValue());
    }

    private void setupProgressUpdater() {
        progressTimeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            if (playerRepository.getMediaPlayer() != null) {
                double currentSeconds = playerRepository.getCurrentTime().getSeconds();
                double totalSeconds = playerRepository.getTotalDuration().getSeconds();
                viewModel.updateProgress(currentSeconds, totalSeconds);
            }
        }));
        progressTimeline.setCycleCount(Timeline.INDEFINITE);
        progressTimeline.play();
    }

    private void setupSeekFunctionality() {
        // Enable seeking by clicking/dragging on progress slider
        progressSlider.setOnMousePressed(event -> {
            seekToPosition(progressSlider.getValue());
        });

        progressSlider.setOnMouseDragged(event -> {
            seekToPosition(progressSlider.getValue());
        });

        progressSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                seekToPosition(progressSlider.getValue());
            }
        });
    }

    private void seekToPosition(double sliderValue) {
        if (playerRepository.getMediaPlayer() != null) {
            double totalSeconds = playerRepository.getTotalDuration().getSeconds();
            double seekSeconds = (sliderValue / 100.0) * totalSeconds;
            playerRepository.seek(java.time.Duration.ofSeconds((long) seekSeconds));
        }
    }

    private void setupPlaylistContextMenu() {
        javafx.scene.control.ContextMenu contextMenu = new javafx.scene.control.ContextMenu();

        javafx.scene.control.MenuItem playItem = new javafx.scene.control.MenuItem("Play");
        playItem.setOnAction(e -> {
            Song selected = playlistView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                viewModel.playSong(selected);
            }
        });

        javafx.scene.control.MenuItem deleteItem = new javafx.scene.control.MenuItem("Delete from Playlist");
        deleteItem.setOnAction(e -> {
            Song selected = playlistView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                viewModel.removeSong(selected);
            }
        });

        javafx.scene.control.MenuItem favoriteItem = new javafx.scene.control.MenuItem("Toggle Favorite");
        favoriteItem.setOnAction(e -> {
            Song selected = playlistView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                viewModel.toggleFavorite(selected);
            }
        });

        contextMenu.getItems().addAll(playItem, favoriteItem, new javafx.scene.control.SeparatorMenuItem(), deleteItem);
        playlistView.setContextMenu(contextMenu);
    }

    private void setupFavoritesContextMenu() {
        javafx.scene.control.ContextMenu contextMenu = new javafx.scene.control.ContextMenu();

        javafx.scene.control.MenuItem playItem = new javafx.scene.control.MenuItem("Play");
        playItem.setOnAction(e -> {
            Song selected = favoritesView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                viewModel.playSong(selected);
            }
        });

        javafx.scene.control.MenuItem unfavoriteItem = new javafx.scene.control.MenuItem("Remove from Favorites");
        unfavoriteItem.setOnAction(e -> {
            Song selected = favoritesView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                viewModel.toggleFavorite(selected);
            }
        });

        javafx.scene.control.MenuItem deleteItem = new javafx.scene.control.MenuItem("Delete from Playlist");
        deleteItem.setOnAction(e -> {
            Song selected = favoritesView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                viewModel.removeSong(selected);
            }
        });

        contextMenu.getItems().addAll(playItem, unfavoriteItem, new javafx.scene.control.SeparatorMenuItem(), deleteItem);
        favoritesView.setContextMenu(contextMenu);
    }

    private void updateVolumeDisplay(double volume) {
        // Update percentage label
        volumePercentLabel.setText(String.format("%d%%", (int) (volume * 100)));

        // Update volume icon based on level
        if (volume == 0) {
            volumeIcon.setText("🔇");
        } else if (volume < 0.33) {
            volumeIcon.setText("🔈");
        } else if (volume < 0.66) {
            volumeIcon.setText("🔉");
        } else {
            volumeIcon.setText("🔊");
        }
    }

    @FXML
    private void onLoadSongs() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Music Folder");
        File selectedDirectory = directoryChooser.showDialog(playlistView.getScene().getWindow());

        if (selectedDirectory != null) {
            viewModel.loadSongs(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void onPlayPause() {
        if (viewModel.isPlayingProperty().get()) {
            viewModel.pause();
        } else if (viewModel.isPausedProperty().get()) {
            viewModel.resume();
        } else {
            viewModel.playCurrentSong();
        }
    }

    @FXML
    private void onStop() {
        viewModel.stop();
    }

    @FXML
    private void onNext() {
        viewModel.nextSong();
    }

    @FXML
    private void onPrevious() {
        viewModel.previousSong();
    }

    @FXML
    private void onShuffleToggle() {
        viewModel.setShuffle(shuffleButton.isSelected());
    }

    @FXML
    private void onRepeatToggle() {
        viewModel.setRepeat(repeatButton.isSelected());
    }

    @FXML
    private void onToggleFavorite() {
        Song currentSong = viewModel.currentSongProperty().get();
        if (currentSong != null) {
            viewModel.toggleFavorite(currentSong);
            updateFavoriteButton(currentSong);
        }
    }

    @FXML
    private void onClearFavorites() {
        viewModel.clearFavorites();
    }

    @FXML
    private void onAddFiles() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select MP3 Files");
        fileChooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.m4a")
        );
        java.util.List<File> files = fileChooser.showOpenMultipleDialog(playlistView.getScene().getWindow());

        if (files != null && !files.isEmpty()) {
            // Add only the selected files (not entire folders)
            for (File file : files) {
                viewModel.addSingleFile(file.getAbsolutePath());
            }
        }
    }

    @FXML
    private void onClearPlaylist() {
        // Stop playback and clear
        viewModel.stop();
        playlistView.getItems().clear();
    }

    @FXML
    private void onExit() {
        System.exit(0);
    }

    @FXML
    private void onSavePlaylist() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Save Playlist");
        fileChooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter("M3U Playlist", "*.m3u")
        );
        fileChooser.setInitialFileName("playlist.m3u");

        File file = fileChooser.showSaveDialog(playlistView.getScene().getWindow());

        if (file != null) {
            try {
                viewModel.savePlaylist(file.getAbsolutePath());
                showInfo("Save Playlist", "Playlist saved successfully to:\n" + file.getName());
            } catch (Exception e) {
                showError("Save Playlist Error", "Failed to save playlist:\n" + e.getMessage());
            }
        }
    }

    @FXML
    private void onLoadPlaylist() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Load Playlist");
        fileChooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter("M3U Playlist", "*.m3u")
        );

        File file = fileChooser.showOpenDialog(playlistView.getScene().getWindow());

        if (file != null) {
            try {
                viewModel.loadPlaylistFromFile(file.getAbsolutePath());
                showInfo("Load Playlist", "Playlist loaded successfully!\n" +
                         "Loaded " + viewModel.getSongs().size() + " songs.");
            } catch (Exception e) {
                showError("Load Playlist Error", "Failed to load playlist:\n" + e.getMessage());
            }
        }
    }

    @FXML
    private void onShowFavorites() {
        playlistTabs.getSelectionModel().select(1); // Switch to Favorites tab
    }

    @FXML
    private void onToggleEqualizer() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/com/mp3player/equalizer-view.fxml")
            );
            javafx.scene.Parent root = loader.load();

            // Get the controller and set the player repository
            EqualizerController controller = loader.getController();
            controller.setPlayerRepository(playerRepository);

            javafx.stage.Stage equalizerStage = new javafx.stage.Stage();
            equalizerStage.setTitle("Equalizer");
            equalizerStage.setScene(new javafx.scene.Scene(root));
            equalizerStage.initModality(javafx.stage.Modality.NONE);
            equalizerStage.setResizable(false);
            equalizerStage.show();
        } catch (Exception e) {
            showError("Equalizer Error", "Failed to open Equalizer:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onToggleVisualizer() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/com/mp3player/visualizer-view.fxml")
            );
            javafx.scene.Parent root = loader.load();

            // Get the controller and set the player repository
            VisualizerController controller = loader.getController();
            controller.setPlayerRepository(playerRepository);

            javafx.stage.Stage visualizerStage = new javafx.stage.Stage();
            visualizerStage.setTitle("Audio Visualizer");
            visualizerStage.setScene(new javafx.scene.Scene(root));
            visualizerStage.initModality(javafx.stage.Modality.NONE);
            visualizerStage.setResizable(false);

            // Cleanup when window is closed
            visualizerStage.setOnCloseRequest(event -> controller.cleanup());

            visualizerStage.show();
        } catch (Exception e) {
            showError("Visualizer Error", "Failed to open Visualizer:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onAbout() {
        showInfo("About MP3 Player",
            "MP3 Player v4.0\n\n" +
            "A modern music player built with JavaFX\n\n" +
            "Features:\n" +
            "• Play/Pause/Stop controls\n" +
            "• Shuffle & Repeat modes\n" +
            "• Search & Filter songs\n" +
            "• Favorites system\n" +
            "• Multiple playlists\n" +
            "• Playback speed control (0.5x - 2.0x)\n" +
            "• Save/Load Playlists (.m3u)\n" +
            "• 10-band Equalizer\n" +
            "• Audio Visualizer (4 modes)\n" +
            "• Context menu operations\n" +
            "• Keyboard shortcuts\n\n" +
            "Created with ❤️ using JavaFX");
    }

    @FXML
    private void onShowShortcuts() {
        showInfo("Keyboard Shortcuts",
            "Ctrl+O - Open Folder\n" +
            "Ctrl+F - Add Files\n" +
            "Ctrl+S - Save Playlist\n" +
            "Ctrl+L - Load Playlist\n" +
            "Ctrl+D - Show Favorites\n" +
            "Ctrl+H - Toggle Shuffle\n" +
            "Ctrl+R - Toggle Repeat\n" +
            "Space - Play/Pause\n" +
            "Ctrl+Right - Next Song\n" +
            "Ctrl+Left - Previous Song\n" +
            "Alt+F4 - Exit");
    }

    private void updateFavoriteButton(Song song) {
        if (song != null && song.isFavorite()) {
            favoriteButton.setText("❤");
            favoriteButton.getStyleClass().add("active");
        } else {
            favoriteButton.setText("♡");
            favoriteButton.getStyleClass().remove("active");
        }
    }

    private void showInfo(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Playlist Management Methods
    @FXML
    private void onCreatePlaylist() {
        TextInputDialog dialog = new TextInputDialog("New Playlist");
        dialog.setTitle("Create Playlist");
        dialog.setHeaderText("Create a new playlist");
        dialog.setContentText("Enter playlist name:");

        dialog.showAndWait().ifPresent(name -> {
            if (name != null && !name.trim().isEmpty()) {
                viewModel.createNewPlaylist(name.trim());
            }
        });
    }

    @FXML
    private void onRenamePlaylist() {
        com.mp3player.domain.entity.PlaylistEntity currentPlaylist =
            viewModel.currentPlaylistEntityProperty().get();

        if (currentPlaylist == null) {
            showError("Rename Playlist", "No playlist selected.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(currentPlaylist.getName());
        dialog.setTitle("Rename Playlist");
        dialog.setHeaderText("Rename playlist");
        dialog.setContentText("Enter new name:");

        dialog.showAndWait().ifPresent(newName -> {
            if (newName != null && !newName.trim().isEmpty()) {
                viewModel.renamePlaylist(currentPlaylist, newName.trim());
            }
        });
    }

    @FXML
    private void onDeletePlaylist() {
        com.mp3player.domain.entity.PlaylistEntity currentPlaylist =
            viewModel.currentPlaylistEntityProperty().get();

        if (currentPlaylist == null) {
            showError("Delete Playlist", "No playlist selected.");
            return;
        }

        if (viewModel.getPlaylists().size() <= 1) {
            showError("Delete Playlist", "Cannot delete the last playlist.");
            return;
        }

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Playlist");
        alert.setHeaderText("Delete playlist \"" + currentPlaylist.getName() + "\"?");
        alert.setContentText("This action cannot be undone. Songs will not be deleted from your computer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                viewModel.deletePlaylist(currentPlaylist);
            }
        });
    }
}
