package com.mp3player.domain.repository;

import com.mp3player.domain.entity.Playlist;
import com.mp3player.domain.entity.Song;
import java.util.List;

public interface PlaylistRepository {
    void savePlaylist(Playlist playlist);
    Playlist loadPlaylist();
    List<Song> loadSongsFromDirectory(String directoryPath);
}
