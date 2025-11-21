package com.mp3player.domain.usecase;

import com.mp3player.domain.repository.MusicPlayerRepository;

public class SetVolumeUseCase {
    private final MusicPlayerRepository repository;

    public SetVolumeUseCase(MusicPlayerRepository repository) {
        this.repository = repository;
    }

    public void execute(double volume) {
        if (volume >= 0.0 && volume <= 1.0) {
            repository.setVolume(volume);
        }
    }
}
