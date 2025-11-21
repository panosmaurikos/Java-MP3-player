package com.mp3player.domain.usecase;

import com.mp3player.domain.repository.MusicPlayerRepository;

public class ResumeSongUseCase {
    private final MusicPlayerRepository repository;

    public ResumeSongUseCase(MusicPlayerRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        repository.resume();
    }
}
