package com.mp3player.data.repository;

import com.mp3player.domain.entity.Song;
import com.mp3player.domain.repository.MusicPlayerRepository;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class JavaFXMusicPlayerRepository implements MusicPlayerRepository {
    private MediaPlayer mediaPlayer;
    private boolean isPaused = false;

    @Override
    public void play(Song song) {
        stop();

        try {
            File file = new File(song.getFilePath());
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            isPaused = false;
        } catch (Exception e) {
            System.err.println("Error playing song: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            isPaused = true;
        }
    }

    @Override
    public void resume() {
        if (mediaPlayer != null && isPaused) {
            mediaPlayer.play();
            isPaused = false;
        }
    }

    @Override
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            isPaused = false;
        }
    }

    @Override
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    @Override
    public void setPlaybackSpeed(double speed) {
        if (mediaPlayer != null) {
            mediaPlayer.setRate(speed);
        }
    }

    @Override
    public void seek(java.time.Duration position) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.millis(position.toMillis()));
        }
    }

    @Override
    public java.time.Duration getCurrentTime() {
        if (mediaPlayer != null) {
            return java.time.Duration.ofMillis((long) mediaPlayer.getCurrentTime().toMillis());
        }
        return java.time.Duration.ZERO;
    }

    @Override
    public java.time.Duration getTotalDuration() {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            return java.time.Duration.ofMillis((long) mediaPlayer.getTotalDuration().toMillis());
        }
        return java.time.Duration.ZERO;
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
