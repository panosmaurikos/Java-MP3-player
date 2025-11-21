package com.mp3player.domain.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Playlist {
    private PlaylistEntity playlistEntity;
    private final List<Song> shuffledSongs;
    private int currentIndex;
    private boolean isShuffled;
    private boolean isRepeat;
    private final Random random;

    public Playlist() {
        this.playlistEntity = new PlaylistEntity("My Playlist");
        this.shuffledSongs = new ArrayList<>();
        this.currentIndex = -1;
        this.isShuffled = false;
        this.isRepeat = false;
        this.random = new Random();
    }

    public Playlist(PlaylistEntity playlistEntity) {
        this.playlistEntity = playlistEntity;
        this.shuffledSongs = new ArrayList<>();
        this.currentIndex = -1;
        this.isShuffled = false;
        this.isRepeat = false;
        this.random = new Random();
    }

    public void setPlaylistEntity(PlaylistEntity playlistEntity) {
        if (playlistEntity != null) {
            this.playlistEntity = playlistEntity;
            this.shuffledSongs.clear();
            this.currentIndex = playlistEntity.isEmpty() ? -1 : 0;
            if (isShuffled) {
                shuffledSongs.addAll(playlistEntity.getSongs());
                Collections.shuffle(shuffledSongs, random);
            }
        }
    }

    public PlaylistEntity getPlaylistEntity() {
        return playlistEntity;
    }

    public void addSong(Song song) {
        playlistEntity.addSong(song);
        if (isShuffled) {
            shuffledSongs.add(song);
        }
        if (currentIndex == -1) {
            currentIndex = 0;
        }
    }

    public void removeSong(int index) {
        List<Song> songs = playlistEntity.getSongs();
        if (index >= 0 && index < songs.size()) {
            Song songToRemove = songs.get(index);
            playlistEntity.removeSongAt(index);
            shuffledSongs.remove(songToRemove);
            if (currentIndex >= playlistEntity.getSongCount()) {
                currentIndex = playlistEntity.getSongCount() - 1;
            }
        }
    }

    public Song getCurrentSong() {
        List<Song> activeList = isShuffled ? shuffledSongs : playlistEntity.getSongs();
        if (currentIndex >= 0 && currentIndex < activeList.size()) {
            return activeList.get(currentIndex);
        }
        return null;
    }

    public Song nextSong() {
        if (playlistEntity.isEmpty()) {
            return null;
        }

        List<Song> activeList = isShuffled ? shuffledSongs : playlistEntity.getSongs();

        if (isRepeat) {
            // Repeat mode: loop the playlist
            currentIndex = (currentIndex + 1) % activeList.size();
        } else {
            // No repeat: stop at the end
            if (currentIndex < activeList.size() - 1) {
                currentIndex++;
            } else {
                // Reached end, stay at last song
                return null;
            }
        }

        return getCurrentSong();
    }

    public Song previousSong() {
        if (playlistEntity.isEmpty()) {
            return null;
        }

        List<Song> activeList = isShuffled ? shuffledSongs : playlistEntity.getSongs();

        if (isRepeat) {
            currentIndex = (currentIndex - 1 + activeList.size()) % activeList.size();
        } else {
            if (currentIndex > 0) {
                currentIndex--;
            }
        }

        return getCurrentSong();
    }

    public void setShuffle(boolean shuffle) {
        if (shuffle && !isShuffled) {
            // Enable shuffle - get current song BEFORE changing isShuffled
            Song current = getCurrentSong();

            shuffledSongs.clear();
            shuffledSongs.addAll(playlistEntity.getSongs());
            Collections.shuffle(shuffledSongs, random);
            isShuffled = true;

            // Find current song in shuffled list
            if (current != null) {
                currentIndex = shuffledSongs.indexOf(current);
            } else {
                currentIndex = 0;
            }
        } else if (!shuffle && isShuffled) {
            // Disable shuffle - get current song BEFORE changing isShuffled
            Song current = getCurrentSong();
            isShuffled = false;

            // Find current song in original list
            if (current != null) {
                currentIndex = playlistEntity.getSongs().indexOf(current);
            } else {
                currentIndex = 0;
            }
        }
    }

    public void setRepeat(boolean repeat) {
        this.isRepeat = repeat;
    }

    public boolean isShuffled() {
        return isShuffled;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public List<Song> getAllSongs() {
        return Collections.unmodifiableList(playlistEntity.getSongs());
    }

    public List<Song> getFilteredSongs(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return getAllSongs();
        }

        String lowerSearch = searchText.toLowerCase();
        List<Song> filtered = new ArrayList<>();

        for (Song song : playlistEntity.getSongs()) {
            if (song.getTitle().toLowerCase().contains(lowerSearch) ||
                song.getArtist().toLowerCase().contains(lowerSearch)) {
                filtered.add(song);
            }
        }

        return filtered;
    }

    public List<Song> getFavoriteSongs() {
        List<Song> favorites = new ArrayList<>();
        for (Song song : playlistEntity.getSongs()) {
            if (song.isFavorite()) {
                favorites.add(song);
            }
        }
        return favorites;
    }

    public void toggleFavorite(Song song) {
        if (song != null) {
            song.toggleFavorite();
        }
    }

    public void clearFavorites() {
        for (Song song : playlistEntity.getSongs()) {
            song.setFavorite(false);
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        List<Song> activeList = isShuffled ? shuffledSongs : playlistEntity.getSongs();
        if (index >= 0 && index < activeList.size()) {
            this.currentIndex = index;
        }
    }

    public void setCurrentSong(Song song) {
        if (song == null) {
            return;
        }

        List<Song> activeList = isShuffled ? shuffledSongs : playlistEntity.getSongs();
        int index = activeList.indexOf(song);
        if (index >= 0) {
            this.currentIndex = index;
        }
    }

    public boolean isEmpty() {
        return playlistEntity.isEmpty();
    }

    public int size() {
        return playlistEntity.getSongCount();
    }
}
