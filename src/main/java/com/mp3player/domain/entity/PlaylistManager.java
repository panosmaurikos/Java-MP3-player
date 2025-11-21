package com.mp3player.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaylistManager {
    private final List<PlaylistEntity> playlists;
    private PlaylistEntity currentPlaylist;

    public PlaylistManager() {
        this.playlists = new ArrayList<>();
        // Create default playlist
        PlaylistEntity defaultPlaylist = new PlaylistEntity("My Playlist");
        playlists.add(defaultPlaylist);
        currentPlaylist = defaultPlaylist;
    }

    public PlaylistEntity createPlaylist(String name) {
        PlaylistEntity playlist = new PlaylistEntity(name);
        playlists.add(playlist);
        return playlist;
    }

    public boolean deletePlaylist(PlaylistEntity playlist) {
        if (playlists.size() <= 1) {
            // Don't allow deleting the last playlist
            return false;
        }

        boolean removed = playlists.remove(playlist);
        if (removed && currentPlaylist.equals(playlist)) {
            // Switch to first available playlist
            currentPlaylist = playlists.get(0);
        }
        return removed;
    }

    public boolean renamePlaylist(PlaylistEntity playlist, String newName) {
        if (playlist != null && newName != null && !newName.trim().isEmpty()) {
            playlist.setName(newName);
            return true;
        }
        return false;
    }

    public void setCurrentPlaylist(PlaylistEntity playlist) {
        if (playlist != null && playlists.contains(playlist)) {
            currentPlaylist = playlist;
        }
    }

    public PlaylistEntity getCurrentPlaylist() {
        return currentPlaylist;
    }

    public List<PlaylistEntity> getAllPlaylists() {
        return new ArrayList<>(playlists);
    }

    public Optional<PlaylistEntity> getPlaylistById(String id) {
        return playlists.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Optional<PlaylistEntity> getPlaylistByName(String name) {
        return playlists.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();
    }

    public int getPlaylistCount() {
        return playlists.size();
    }

    public void addSongToPlaylist(PlaylistEntity playlist, Song song) {
        if (playlist != null && playlists.contains(playlist)) {
            playlist.addSong(song);
        }
    }

    public void addSongToCurrentPlaylist(Song song) {
        currentPlaylist.addSong(song);
    }

    public void removeSongFromPlaylist(PlaylistEntity playlist, Song song) {
        if (playlist != null && playlists.contains(playlist)) {
            playlist.removeSong(song);
        }
    }

    public void removeSongFromCurrentPlaylist(Song song) {
        currentPlaylist.removeSong(song);
    }
}
