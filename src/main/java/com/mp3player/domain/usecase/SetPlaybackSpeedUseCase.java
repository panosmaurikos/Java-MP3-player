package com.mp3player.domain.usecase;

import com.mp3player.domain.repository.MusicPlayerRepository;

public class SetPlaybackSpeedUseCase {
    private final MusicPlayerRepository musicPlayerRepository;

    public SetPlaybackSpeedUseCase(MusicPlayerRepository musicPlayerRepository) {
        this.musicPlayerRepository = musicPlayerRepository;
    }

    public void execute(double speed) {
        if (speed >= 0.5 && speed <= 2.0) {
            musicPlayerRepository.setPlaybackSpeed(speed);
        }
    }
}
