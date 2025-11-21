package com.mp3player.data.repository;

import com.mp3player.domain.entity.Playlist;
import com.mp3player.domain.entity.Song;
import com.mp3player.domain.repository.PlaylistRepository;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FilePlaylistRepository implements PlaylistRepository {

    @Override
    public void savePlaylist(Playlist playlist) {
        // Future implementation: save to JSON/XML file
    }

    @Override
    public Playlist loadPlaylist() {
        // Future implementation: load from JSON/XML file
        return new Playlist();
    }

    @Override
    public List<Song> loadSongsFromDirectory(String directoryPath) {
        List<Song> songs = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".mp3") ||
                name.toLowerCase().endsWith(".wav") ||
                name.toLowerCase().endsWith(".m4a")
            );

            if (files != null) {
                for (File file : files) {
                    String id = UUID.randomUUID().toString();
                    String title = getFileNameWithoutExtension(file.getName());
                    String artist = "Unknown Artist";
                    String filePath = file.getAbsolutePath();
                    Duration duration = Duration.ZERO;

                    Song song = new Song(id, title, artist, filePath, duration);
                    songs.add(song);
                }
            }
        }

        return songs;
    }

    private String getFileNameWithoutExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }
}
