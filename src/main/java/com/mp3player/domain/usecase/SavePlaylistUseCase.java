package com.mp3player.domain.usecase;

import com.mp3player.domain.entity.Song;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SavePlaylistUseCase {

    public void execute(List<Song> songs, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write M3U header
            writer.write("#EXTM3U");
            writer.newLine();

            // Write each song
            for (Song song : songs) {
                // Write extended info (#EXTINF:duration,artist - title)
                writer.write(String.format("#EXTINF:0,%s - %s", song.getArtist(), song.getTitle()));
                writer.newLine();

                // Write file path
                writer.write(song.getFilePath());
                writer.newLine();
            }
        }
    }
}
