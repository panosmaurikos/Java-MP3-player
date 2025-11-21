package com.mp3player.domain.repository;

import com.mp3player.domain.entity.Song;
import java.time.Duration;

public interface MusicPlayerRepository {
    void play(Song song);
    void pause();
    void resume();
    void stop();
    void setVolume(double volume);
    void setPlaybackSpeed(double speed);
    void seek(Duration position);
    Duration getCurrentTime();
    Duration getTotalDuration();
    boolean isPlaying();
    boolean isPaused();
}
