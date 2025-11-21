package com.mp3player.domain.usecase;

import com.mp3player.domain.entity.Song;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoadPlaylistFileUseCase {

    public List<Song> execute(String filePath) throws IOException {
        List<Song> songs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentTitle = null;
            String currentArtist = "Unknown Artist";

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("#EXTM3U")) {
                    // M3U playlist header, skip
                    continue;
                } else if (line.startsWith("#EXTINF:")) {
                    // Extended info line: #EXTINF:duration,artist - title
                    String info = line.substring(8); // Remove "#EXTINF:"
                    int commaIndex = info.indexOf(',');

                    if (commaIndex > 0) {
                        String titleArtist = info.substring(commaIndex + 1).trim();

                        // Parse "Artist - Title" format
                        if (titleArtist.contains(" - ")) {
                            String[] parts = titleArtist.split(" - ", 2);
                            currentArtist = parts[0].trim();
                            currentTitle = parts[1].trim();
                        } else {
                            currentTitle = titleArtist;
                            currentArtist = "Unknown Artist";
                        }
                    }
                } else if (!line.startsWith("#") && !line.isEmpty()) {
                    // This is a file path
                    java.io.File file = new java.io.File(line);

                    if (file.exists()) {
                        // Use info from #EXTINF if available
                        String title = currentTitle != null ? currentTitle : getFileNameWithoutExtension(file.getName());
                        String artist = currentArtist;

                        Song song = new Song(
                                UUID.randomUUID().toString(),
                                title,
                                artist,
                                line,
                                Duration.ZERO
                        );

                        songs.add(song);

                        // Reset for next song
                        currentTitle = null;
                        currentArtist = "Unknown Artist";
                    }
                }
            }
        }

        return songs;
    }

    private String getFileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }
}
