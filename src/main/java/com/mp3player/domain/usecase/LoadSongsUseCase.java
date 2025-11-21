package com.mp3player.domain.usecase;

import com.mp3player.domain.entity.Song;
import com.mp3player.domain.repository.PlaylistRepository;
import java.util.List;

public class LoadSongsUseCase {
    private final PlaylistRepository repository;

    public LoadSongsUseCase(PlaylistRepository repository) {
        this.repository = repository;
    }

    public List<Song> execute(String directoryPath) {
        return repository.loadSongsFromDirectory(directoryPath);
    }
}
