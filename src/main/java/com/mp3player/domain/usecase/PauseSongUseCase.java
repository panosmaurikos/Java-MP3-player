package com.mp3player.domain.usecase;

import com.mp3player.domain.repository.MusicPlayerRepository;

public class PauseSongUseCase {
    private final MusicPlayerRepository repository;

    public PauseSongUseCase(MusicPlayerRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        repository.pause();
    }
}
