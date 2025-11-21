package com.mp3player.domain.usecase;

import com.mp3player.domain.entity.Song;
import com.mp3player.domain.repository.MusicPlayerRepository;

public class PlaySongUseCase {
    private final MusicPlayerRepository repository;

    public PlaySongUseCase(MusicPlayerRepository repository) {
        this.repository = repository;
    }

    public void execute(Song song) {
        if (song != null) {
            repository.play(song);
        }
    }
}
