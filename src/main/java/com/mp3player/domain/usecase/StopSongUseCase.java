package com.mp3player.domain.usecase;

import com.mp3player.domain.repository.MusicPlayerRepository;

public class StopSongUseCase {
    private final MusicPlayerRepository repository;

    public StopSongUseCase(MusicPlayerRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        repository.stop();
    }
}
