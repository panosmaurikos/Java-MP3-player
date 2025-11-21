# Architecture Documentation

## Clean Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  View (FXML + Controller)                            │   │
│  │  - main-view.fxml                                     │   │
│  │  - MainController.java                               │   │
│  │  - style.css                                          │   │
│  └────────────────┬─────────────────────────────────────┘   │
│                   │ Observes & Commands                     │
│  ┌────────────────▼─────────────────────────────────────┐   │
│  │  ViewModel (MVVM)                                     │   │
│  │  - MusicPlayerViewModel.java                         │   │
│  │    * Observable Properties (currentSong, isPlaying)  │   │
│  │    * Commands (play, pause, stop, etc.)              │   │
│  └────────────────┬─────────────────────────────────────┘   │
└───────────────────┼──────────────────────────────────────────┘
                    │ Uses
┌───────────────────▼──────────────────────────────────────────┐
│                     DOMAIN LAYER                             │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Entities                                             │   │
│  │  - Song.java (id, title, artist, filePath)           │   │
│  │  - Playlist.java (songs, currentIndex)               │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Use Cases (Business Logic)                          │   │
│  │  - PlaySongUseCase                                    │   │
│  │  - PauseSongUseCase                                   │   │
│  │  - ResumeSongUseCase                                  │   │
│  │  - StopSongUseCase                                    │   │
│  │  - SetVolumeUseCase                                   │   │
│  │  - LoadSongsUseCase                                   │   │
│  └────────────────┬─────────────────────────────────────┘   │
│                   │ Depends on                              │
│  ┌────────────────▼─────────────────────────────────────┐   │
│  │  Repository Interfaces (Abstractions)                │   │
│  │  - MusicPlayerRepository                             │   │
│  │  - PlaylistRepository                                │   │
│  └──────────────────────────────────────────────────────┘   │
└───────────────────┬──────────────────────────────────────────┘
                    │ Implemented by
┌───────────────────▼──────────────────────────────────────────┐
│                     DATA LAYER                               │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Repository Implementations                          │   │
│  │  - JavaFXMusicPlayerRepository                       │   │
│  │    * Uses JavaFX Media API                           │   │
│  │    * MediaPlayer, Media                              │   │
│  │  - FilePlaylistRepository                            │   │
│  │    * File system operations                          │   │
│  │    * Loads MP3/WAV/M4A files                         │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────┘
```

## MVVM Pattern Flow

```
┌──────────┐          ┌──────────────┐          ┌──────────┐
│          │  Binding │              │   Uses   │          │
│   View   ├─────────►│  ViewModel   ├─────────►│  Model   │
│  (FXML)  │          │              │          │(Entities)│
│          │◄─────────┤  Observable  │          │          │
└──────────┘  Updates └──────────────┘          └──────────┘
     │                       │
     │ User Actions         │ Business Logic
     │ (onClick)            │ (Use Cases)
     ▼                       ▼
┌──────────┐          ┌──────────────┐
│Controller│─────────►│  Use Cases   │
│          │  Calls   │              │
└──────────┘          └──────────────┘
```

## Data Flow Example: Playing a Song

```
1. User clicks on song in ListView (View)
        ↓
2. MainController.onMouseClicked() handles event (Controller)
        ↓
3. Calls viewModel.selectSong(index) (ViewModel)
        ↓
4. ViewModel calls PlaySongUseCase.execute(song) (Use Case)
        ↓
5. Use Case calls repository.play(song) (Repository Interface)
        ↓
6. JavaFXMusicPlayerRepository.play() creates MediaPlayer (Implementation)
        ↓
7. MediaPlayer starts playing (JavaFX Media API)
        ↓
8. ViewModel updates Observable properties:
   - currentSong.set(song)
   - isPlaying.set(true)
        ↓
9. View automatically updates via bindings (FXML)
   - currentSongLabel text changes
   - Play button changes state
```

## Dependency Rule

```
Presentation ──► Domain ──► Data
     │            ▲
     │            │
     └────────────┘
   (Dependency Inversion)
```

**Κανόνας**: Τα dependencies δείχνουν **πάντα προς τα μέσα**
- Presentation depends on Domain
- Data depends on Domain (implements interfaces)
- Domain δεν εξαρτάται από κανέναν (pure business logic)

## Benefits

### 1. **Testability**
```java
// Mock repository για testing
class MockMusicPlayerRepository implements MusicPlayerRepository {
    @Override
    public void play(Song song) {
        // Test implementation
    }
}

// Test use case χωρίς πραγματικό player
PlaySongUseCase useCase = new PlaySongUseCase(new MockMusicPlayerRepository());
```

### 2. **Flexibility**
```java
// Μπορείς εύκολα να αλλάξεις implementation
// π.χ. από JavaFX Media σε JLayer ή άλλο library

class JLayerMusicPlayerRepository implements MusicPlayerRepository {
    // Different implementation, same interface
}
```

### 3. **Separation of Concerns**
- **Domain**: Τι κάνει το app (business rules)
- **Data**: Πως το κάνει (technical details)
- **Presentation**: Πως το δείχνει (UI)

### 4. **Independent Development**
- UI developers → Work on Presentation layer
- Backend developers → Work on Data layer
- Business logic → Domain layer (shared understanding)

## Key Design Patterns Used

1. **Repository Pattern**: Abstraction για data access
2. **Use Case Pattern**: Single responsibility per business operation
3. **MVVM Pattern**: Separation of UI logic
4. **Dependency Inversion**: High-level modules don't depend on low-level
5. **Observer Pattern**: JavaFX Observable properties για reactive UI

---

Αυτή η αρχιτεκτονική κάνει το code:
- ✅ Testable
- ✅ Maintainable
- ✅ Scalable
- ✅ Flexible
- ✅ Clear & Organized
