package com.mp3player.presentation.viewmodel;

import com.mp3player.domain.entity.Playlist;
import com.mp3player.domain.entity.PlaylistEntity;
import com.mp3player.domain.entity.PlaylistManager;
import com.mp3player.domain.entity.Song;
import com.mp3player.domain.usecase.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.Duration;
import java.util.List;

public class MusicPlayerViewModel {
    private final PlaySongUseCase playSongUseCase;
    private final PauseSongUseCase pauseSongUseCase;
    private final ResumeSongUseCase resumeSongUseCase;
    private final StopSongUseCase stopSongUseCase;
    private final SetVolumeUseCase setVolumeUseCase;
    private final SetPlaybackSpeedUseCase setPlaybackSpeedUseCase;
    private final LoadSongsUseCase loadSongsUseCase;
    private final SavePlaylistUseCase savePlaylistUseCase;
    private final LoadPlaylistFileUseCase loadPlaylistFileUseCase;

    private final PlaylistManager playlistManager;
    private final Playlist playlist;

    // Observable properties for View binding
    private final ObjectProperty<Song> currentSong = new SimpleObjectProperty<>();
    private final ObservableList<Song> songs = FXCollections.observableArrayList();
    private final ObservableList<Song> favoriteSongs = FXCollections.observableArrayList();
    private final ObservableList<PlaylistEntity> playlists = FXCollections.observableArrayList();
    private final ObjectProperty<PlaylistEntity> currentPlaylistEntity = new SimpleObjectProperty<>();
    private final DoubleProperty volume = new SimpleDoubleProperty(0.5);
    private final DoubleProperty playbackSpeed = new SimpleDoubleProperty(1.0);
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    private final BooleanProperty isShuffled = new SimpleBooleanProperty(false);
    private final BooleanProperty isRepeat = new SimpleBooleanProperty(false);
    private final StringProperty currentTimeString = new SimpleStringProperty("00:00");
    private final StringProperty totalTimeString = new SimpleStringProperty("00:00");
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);
    private final StringProperty searchText = new SimpleStringProperty("");

    public MusicPlayerViewModel(
            PlaySongUseCase playSongUseCase,
            PauseSongUseCase pauseSongUseCase,
            ResumeSongUseCase resumeSongUseCase,
            StopSongUseCase stopSongUseCase,
            SetVolumeUseCase setVolumeUseCase,
            SetPlaybackSpeedUseCase setPlaybackSpeedUseCase,
            LoadSongsUseCase loadSongsUseCase,
            SavePlaylistUseCase savePlaylistUseCase,
            LoadPlaylistFileUseCase loadPlaylistFileUseCase
    ) {
        this.playSongUseCase = playSongUseCase;
        this.pauseSongUseCase = pauseSongUseCase;
        this.resumeSongUseCase = resumeSongUseCase;
        this.stopSongUseCase = stopSongUseCase;
        this.setVolumeUseCase = setVolumeUseCase;
        this.setPlaybackSpeedUseCase = setPlaybackSpeedUseCase;
        this.loadSongsUseCase = loadSongsUseCase;
        this.savePlaylistUseCase = savePlaylistUseCase;
        this.loadPlaylistFileUseCase = loadPlaylistFileUseCase;

        // Initialize playlist manager and playlist
        this.playlistManager = new PlaylistManager();
        this.playlist = new Playlist(playlistManager.getCurrentPlaylist());

        // Initialize playlists observable list
        playlists.setAll(playlistManager.getAllPlaylists());
        currentPlaylistEntity.set(playlistManager.getCurrentPlaylist());

        // Listen to search text changes
        searchText.addListener((obs, oldVal, newVal) -> filterSongs(newVal));

        // Listen to playlist changes
        currentPlaylistEntity.addListener((obs, oldPlaylist, newPlaylist) -> {
            if (newPlaylist != null) {
                playlistManager.setCurrentPlaylist(newPlaylist);
                playlist.setPlaylistEntity(newPlaylist);
                updateSongsList();
                updateFavoritesList();
            }
        });
    }

    public void loadSongs(String directoryPath) {
        List<Song> loadedSongs = loadSongsUseCase.execute(directoryPath);
        for (Song song : loadedSongs) {
            playlist.addSong(song);
        }
        updateSongsList();
        updateFavoritesList();
        if (!playlist.isEmpty()) {
            currentSong.set(playlist.getCurrentSong());
        }
    }

    public void addSingleFile(String filePath) {
        // Create a Song from the single file
        java.io.File file = new java.io.File(filePath);
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            String title = fileName.substring(0, fileName.lastIndexOf('.'));
            String artist = "Unknown Artist";

            // Parse title and artist from filename (e.g., "Artist - Title.mp3")
            if (title.contains(" - ")) {
                String[] parts = title.split(" - ", 2);
                artist = parts[0].trim();
                title = parts[1].trim();
            }

            Song song = new Song(
                java.util.UUID.randomUUID().toString(),
                title,
                artist,
                filePath,
                Duration.ZERO // Duration will be set when playing
            );

            playlist.addSong(song);
            updateSongsList();
            updateFavoritesList();

            if (playlist.size() == 1) {
                currentSong.set(playlist.getCurrentSong());
            }
        }
    }

    public void removeSong(Song song) {
        if (song != null) {
            int index = songs.indexOf(song);
            if (index >= 0) {
                playlist.removeSong(index);
                updateSongsList();
                updateFavoritesList();
            }
        }
    }

    private void filterSongs(String filter) {
        List<Song> filtered = playlist.getFilteredSongs(filter);
        songs.setAll(filtered);
    }

    private void updateSongsList() {
        String currentSearch = searchText.get();
        if (currentSearch == null || currentSearch.trim().isEmpty()) {
            songs.setAll(playlist.getAllSongs());
        } else {
            filterSongs(currentSearch);
        }
    }

    public void playSong(Song song) {
        playSongUseCase.execute(song);
        currentSong.set(song);
        playlist.setCurrentSong(song);  // Update playlist's currentIndex
        isPlaying.set(true);
        isPaused.set(false);
    }

    public void playCurrentSong() {
        Song song = playlist.getCurrentSong();
        if (song != null) {
            playSong(song);
        }
    }

    public void pause() {
        pauseSongUseCase.execute();
        isPlaying.set(false);
        isPaused.set(true);
    }

    public void resume() {
        resumeSongUseCase.execute();
        isPlaying.set(true);
        isPaused.set(false);
    }

    public void stop() {
        stopSongUseCase.execute();
        isPlaying.set(false);
        isPaused.set(false);
        progress.set(0.0);
        currentTimeString.set("00:00");
    }

    public void nextSong() {
        Song next = playlist.nextSong();
        if (next != null) {
            playSong(next);
        } else if (!playlist.isRepeat()) {
            // Reached end of playlist without repeat
            stop();
        }
    }

    public void previousSong() {
        Song previous = playlist.previousSong();
        if (previous != null) {
            playSong(previous);
        }
    }

    public void setVolume(double value) {
        setVolumeUseCase.execute(value);
        volume.set(value);
    }

    public void selectSong(int index) {
        playlist.setCurrentIndex(index);
        playCurrentSong();
    }

    public void setShuffle(boolean shuffle) {
        playlist.setShuffle(shuffle);
        isShuffled.set(shuffle);
    }

    public void setRepeat(boolean repeat) {
        playlist.setRepeat(repeat);
        isRepeat.set(repeat);
    }

    public void setSearchText(String text) {
        searchText.set(text);
    }

    public void toggleFavorite(Song song) {
        if (song != null) {
            playlist.toggleFavorite(song);
            updateFavoritesList();
            updateSongsList();
        }
    }

    public void clearFavorites() {
        playlist.clearFavorites();
        updateFavoritesList();
        updateSongsList();
    }

    private void updateFavoritesList() {
        favoriteSongs.setAll(playlist.getFavoriteSongs());
    }

    public void setPlaybackSpeed(double speed) {
        if (speed >= 0.5 && speed <= 2.0) {
            setPlaybackSpeedUseCase.execute(speed);
            playbackSpeed.set(speed);
        }
    }

    public void savePlaylist(String filePath) throws java.io.IOException {
        savePlaylistUseCase.execute(playlist.getAllSongs(), filePath);
    }

    public void loadPlaylistFromFile(String filePath) throws java.io.IOException {
        List<Song> loadedSongs = loadPlaylistFileUseCase.execute(filePath);
        for (Song song : loadedSongs) {
            playlist.addSong(song);
        }
        updateSongsList();
        updateFavoritesList();
        if (!playlist.isEmpty() && currentSong.get() == null) {
            currentSong.set(playlist.getCurrentSong());
        }
    }

    public void updateProgress(double currentSeconds, double totalSeconds) {
        if (totalSeconds > 0) {
            progress.set(currentSeconds / totalSeconds);
        }
        currentTimeString.set(formatDuration(Duration.ofSeconds((long) currentSeconds)));
        totalTimeString.set(formatDuration(Duration.ofSeconds((long) totalSeconds)));
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    // Playlist Management Methods
    public void createNewPlaylist(String name) {
        PlaylistEntity newPlaylist = playlistManager.createPlaylist(name);
        playlists.setAll(playlistManager.getAllPlaylists());
        switchToPlaylist(newPlaylist);
    }

    public void deletePlaylist(PlaylistEntity playlistToDelete) {
        if (playlistManager.deletePlaylist(playlistToDelete)) {
            playlists.setAll(playlistManager.getAllPlaylists());
            currentPlaylistEntity.set(playlistManager.getCurrentPlaylist());
        }
    }

    public void renamePlaylist(PlaylistEntity playlistToRename, String newName) {
        if (playlistManager.renamePlaylist(playlistToRename, newName)) {
            playlists.setAll(playlistManager.getAllPlaylists());
        }
    }

    public void switchToPlaylist(PlaylistEntity playlistEntity) {
        currentPlaylistEntity.set(playlistEntity);
    }

    // Property getters
    public ObjectProperty<Song> currentSongProperty() {
        return currentSong;
    }

    public ObservableList<Song> getSongs() {
        return songs;
    }

    public ObservableList<Song> getFavoriteSongs() {
        return favoriteSongs;
    }

    public ObservableList<PlaylistEntity> getPlaylists() {
        return playlists;
    }

    public ObjectProperty<PlaylistEntity> currentPlaylistEntityProperty() {
        return currentPlaylistEntity;
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public DoubleProperty playbackSpeedProperty() {
        return playbackSpeed;
    }

    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public BooleanProperty isPausedProperty() {
        return isPaused;
    }

    public BooleanProperty isShuffledProperty() {
        return isShuffled;
    }

    public BooleanProperty isRepeatProperty() {
        return isRepeat;
    }

    public StringProperty currentTimeStringProperty() {
        return currentTimeString;
    }

    public StringProperty totalTimeStringProperty() {
        return totalTimeString;
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public StringProperty searchTextProperty() {
        return searchText;
    }
}
