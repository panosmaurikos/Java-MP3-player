package com.mp3player.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaylistEntity {
    private final String id;
    private String name;
    private final List<Song> songs;

    public PlaylistEntity(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public PlaylistEntity(String id, String name, List<Song> songs) {
        this.id = id;
        this.name = name;
        this.songs = new ArrayList<>(songs);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    public void addSong(Song song) {
        if (song != null && !songs.contains(song)) {
            songs.add(song);
        }
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    public void removeSongAt(int index) {
        if (index >= 0 && index < songs.size()) {
            songs.remove(index);
        }
    }

    public void clearSongs() {
        songs.clear();
    }

    public int getSongCount() {
        return songs.size();
    }

    public boolean isEmpty() {
        return songs.isEmpty();
    }

    @Override
    public String toString() {
        return name + " (" + songs.size() + " songs)";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlaylistEntity that = (PlaylistEntity) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
