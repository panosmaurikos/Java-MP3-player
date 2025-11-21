package com.mp3player.domain.entity;

import java.time.Duration;
import java.util.Objects;

public class Song {
    private final String id;
    private final String title;
    private final String artist;
    private final String filePath;
    private final Duration duration;
    private boolean isFavorite;

    public Song(String id, String title, String artist, String filePath, Duration duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.duration = duration;
        this.isFavorite = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getFilePath() {
        return filePath;
    }

    public Duration getDuration() {
        return duration;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void toggleFavorite() {
        isFavorite = !isFavorite;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }

    public String toDetailedString() {
        return (isFavorite ? "⭐ " : "") + title + " - " + artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
