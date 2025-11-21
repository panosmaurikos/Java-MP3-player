# 🎵 MP3 Player v3.0 - Complete Documentation

> A professional MP3 Player built with JavaFX using Clean Architecture + MVVM

---

## 📋 Table of Contents

1. [Quick Start](#-quick-start)
2. [Features Overview](#-features-overview)
3. [Architecture](#-architecture)
4. [User Guide](#-user-guide)
5. [Keyboard Shortcuts](#-keyboard-shortcuts)
6. [Technical Implementation](#-technical-implementation)
7. [Recent Updates](#-recent-updates)
8. [Development](#-development)

---

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Run the Application
```bash
# Build and run
mvn clean install
mvn javafx:run

# Or run from IDE
Main class: com.mp3player.MusicPlayerApplication
```

### First Time Use
1. Click `📁 Open Folder` or press `Ctrl+O`
2. Select a folder containing MP3 files
3. Double-click any song to play
4. Enjoy! 🎶

---

## ✨ Features Overview

### 🎛️ Core Playback
| Feature | Description | Shortcut |
|---------|-------------|----------|
| **Play/Pause** | Toggle playback | `Space` |
| **Stop** | Stop playback | `Ctrl+Alt+S` |
| **Next Song** | Play next track | `Ctrl+Right` |
| **Previous Song** | Play previous track | `Ctrl+Left` |
| **Seek** | Click/drag progress bar | Mouse |
| **Volume Control** | 0-100% volume slider | Slider |
| **Playback Speed** | 0.5x - 2.0x speed | Slider |

### 📁 Playlist Management
| Feature | Description | Shortcut |
|---------|-------------|----------|
| **Open Folder** | Load all MP3s from folder | `Ctrl+O` |
| **Add Files** | Add specific MP3 files | `Ctrl+F` |
| **Search/Filter** | Filter songs by title/artist | Type in search |
| **Clear Playlist** | Remove all songs | Menu |
| **Delete Song** | Right-click → Delete | Context Menu |
| **Save Playlist** | Save to .m3u file | `Ctrl+S` |
| **Load Playlist** | Load from .m3u file | `Ctrl+L` |

### ⭐ Advanced Features
| Feature | Description | Shortcut |
|---------|-------------|----------|
| **Favorites** | Mark songs as favorites | Star button |
| **Favorites Tab** | View only favorite songs | `Ctrl+D` |
| **Shuffle Mode** | Randomize playback order | `Ctrl+H` |
| **Repeat Mode** | Loop playlist | `Ctrl+R` |
| **Context Menu** | Right-click for options | Right-click |

### 🎨 User Interface
- **Modern Gradient Design** - Beautiful purple/cyan gradients
- **Tab System** - "All Songs" and "⭐ Favorites" tabs
- **Menu Bar** - File, Playlist, Playback, View, Help menus
- **Tooltips** - Hover hints on all controls
- **Dynamic Icons** - Volume icon changes with level
- **Favorite Star** - ☆ becomes ★ when favorited

---

## 🏗️ Architecture

### Clean Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│  │   FXML     │  │  Controller  │  │   ViewModel      │   │
│  │ (UI View)  │←→│  (UI Logic)  │←→│ (Business Logic) │   │
│  └────────────┘  └──────────────┘  └──────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                            │
│  ┌────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│  │  Entities  │  │  Use Cases   │  │  Repositories    │   │
│  │ Song/List  │  │  (Actions)   │  │  (Interfaces)    │   │
│  └────────────┘  └──────────────┘  └──────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                       DATA LAYER                             │
│  ┌────────────────────────────────────────────────────────┐ │
│  │          Repository Implementations                     │ │
│  │  • JavaFXMusicPlayerRepository (MediaPlayer)          │ │
│  │  • FilePlaylistRepository (File System)               │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### MVVM Pattern

```
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│     View     │   ←─────│  ViewModel   │   ←─────│    Model     │
│   (FXML)     │  Bind   │ (Observable) │  Update │  (Entities)  │
│              │  ──────→│ Properties   │  ──────→│              │
└──────────────┘         └──────────────┘         └──────────────┘
       │                        │                        │
   User Input            Business Logic           Data Access
```

### Project Structure

```
src/main/java/com/mp3player/
├── domain/                          # Domain Layer (Business Rules)
│   ├── entity/
│   │   ├── Song.java               # Song entity with favorites
│   │   └── Playlist.java           # Playlist management
│   ├── usecase/
│   │   ├── PlaySongUseCase.java
│   │   ├── PauseSongUseCase.java
│   │   ├── SetVolumeUseCase.java
│   │   ├── SetPlaybackSpeedUseCase.java
│   │   └── LoadSongsUseCase.java
│   └── repository/
│       └── MusicPlayerRepository.java  # Interface
│
├── data/                            # Data Layer (Implementation)
│   └── repository/
│       ├── JavaFXMusicPlayerRepository.java
│       └── FilePlaylistRepository.java
│
├── presentation/                    # Presentation Layer (UI)
│   ├── viewmodel/
│   │   └── MusicPlayerViewModel.java
│   ├── view/
│   │   └── MainController.java
│   └── view/
│       ├── main-view.fxml
│       └── style.css
│
└── MusicPlayerApplication.java      # Entry Point
```

---

## 📖 User Guide

### Adding Songs

#### Method 1: Open Folder (Load All)
1. Click `📁 Open Folder` or press `Ctrl+O`
2. Select a folder containing MP3 files
3. **All** MP3/WAV/M4A files will be loaded

#### Method 2: Add Files (Select Specific)
1. Click `➕ Add Files` or press `Ctrl+F`
2. Select specific files (Ctrl+Click for multiple)
3. **Only** selected files will be added

### Playing Music

#### Basic Controls
- **Play**: Double-click song or press `Space`
- **Pause**: Press `Space` again
- **Stop**: Click Stop button or press `Ctrl+Alt+S`
- **Next/Previous**: Use arrow buttons or `Ctrl+Left/Right`

#### Advanced Playback
- **Seek**: Click or drag the progress bar
- **Volume**: Use volume slider (0-100%)
- **Speed**: Use speed slider (0.5x - 2.0x)
- **Shuffle**: Toggle 🔀 button or press `Ctrl+H`
- **Repeat**: Toggle 🔁 button or press `Ctrl+R`

### Managing Favorites

#### Adding Favorites
1. Play a song
2. Click the **☆ star button** in "Now Playing"
3. Star turns gold **★**
4. Song appears in "⭐ Favorites" tab

#### Viewing Favorites
- Click "⭐ Favorites" tab
- Or press `Ctrl+D`

#### Removing Favorites
- Click the star button again (★ → ☆)
- Or right-click in Favorites tab → "Remove from Favorites"

### Context Menu (Right-Click)

#### In "All Songs" Tab:
- **Play** - Play the song
- **Toggle Favorite** - Add/remove ⭐
- **Delete from Playlist** - Remove from list

#### In "⭐ Favorites" Tab:
- **Play** - Play the song
- **Remove from Favorites** - Remove ⭐ (keeps in playlist)
- **Delete from Playlist** - Remove completely

### Saving & Loading Playlists

#### Save Playlist
1. Click `Playlist` → `Save Playlist...` or press `Ctrl+S`
2. Choose location and name
3. Saves as `.m3u` file

#### Load Playlist
1. Click `Playlist` → `Load Playlist...` or press `Ctrl+L`
2. Select `.m3u` file
3. Songs will be loaded

### Searching Songs
- Type in the search box
- Filters by title and artist
- Results update instantly

---

## ⌨️ Keyboard Shortcuts

### File Operations
| Shortcut | Action |
|----------|--------|
| `Ctrl+O` | Open Folder |
| `Ctrl+F` | Add Files |
| `Ctrl+S` | Save Playlist |
| `Ctrl+L` | Load Playlist |
| `Alt+F4` | Exit |

### Playback Control
| Shortcut | Action |
|----------|--------|
| `Space` | Play/Pause |
| `Ctrl+Alt+S` | Stop |
| `Ctrl+Right` | Next Song |
| `Ctrl+Left` | Previous Song |

### Playlist Management
| Shortcut | Action |
|----------|--------|
| `Ctrl+D` | Show Favorites |
| `Ctrl+H` | Toggle Shuffle |
| `Ctrl+R` | Toggle Repeat |

---

## 🔧 Technical Implementation

### Technologies Used
- **JavaFX 21** - UI Framework
- **JavaFX Media API** - Audio Playback
- **Maven** - Build Tool
- **Java 17+** - Programming Language

### Key Design Patterns
1. **Clean Architecture** - Separation of concerns
2. **MVVM** - Model-View-ViewModel
3. **Repository Pattern** - Data abstraction
4. **Use Case Pattern** - Business logic encapsulation
5. **Dependency Inversion** - Depend on abstractions
6. **Observer Pattern** - Observable properties

### JavaFX Media Features
```java
// Playback control
mediaPlayer.play();
mediaPlayer.pause();
mediaPlayer.stop();

// Speed control
mediaPlayer.setRate(1.5); // 1.5x speed

// Volume control
mediaPlayer.setVolume(0.75); // 75% volume

// Seeking
mediaPlayer.seek(Duration.seconds(30));
```

### Observable Properties (MVVM)
```java
// ViewModel
private final ObservableList<Song> songs = FXCollections.observableArrayList();
private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);
private final DoubleProperty playbackSpeed = new SimpleDoubleProperty(1.0);

// View binds to these properties
playlistView.setItems(viewModel.getSongs());
viewModel.isPlayingProperty().addListener((obs, old, playing) -> {
    playButton.setText(playing ? "⏸" : "▶");
});
```

### File Format Support
- **MP3** - MPEG-1 Audio Layer 3
- **WAV** - Waveform Audio
- **M4A** - MPEG-4 Audio

### Playlist Format (.m3u)
```
#EXTM3U
#EXTINF:245,Artist - Title
C:\Music\Artist - Title.mp3
#EXTINF:189,Another Artist - Another Song
C:\Music\Another Artist - Another Song.mp3
```

---

## 🆕 Recent Updates

### Version 3.0 - Full Feature Release

#### New Features Added:
1. ✅ **Menu Bar** - Complete menu system
2. ✅ **Favorites System** - Star button + dedicated tab
3. ✅ **Playback Speed** - 0.5x to 2.0x control
4. ✅ **Keyboard Shortcuts** - 11+ shortcuts
5. ✅ **Context Menu** - Right-click options
6. ✅ **Save/Load Playlist** - .m3u format support
7. ✅ **Add Individual Files** - Not just folders
8. ✅ **Delete Songs** - Remove from playlist
9. ✅ **Tab System** - All Songs / Favorites tabs
10. ✅ **All English UI** - No Greek text

#### Bug Fixes:
1. ✅ Fixed CSS gradient syntax for JavaFX
2. ✅ Fixed "Add Files" to only add selected files
3. ✅ Added proper delete functionality

#### Architecture Improvements:
1. ✅ Added `SetPlaybackSpeedUseCase`
2. ✅ Added `removeSong()` to ViewModel
3. ✅ Added `addSingleFile()` to ViewModel
4. ✅ Context menus for better UX

---

## 🛠️ Development

### Building from Source

```bash
# Clone the repository
git clone <repository-url>
cd mp3-player

# Build with Maven
mvn clean install

# Run the application
mvn javafx:run

# Run tests
mvn test
```

### Project Configuration

#### pom.xml
```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-media</artifactId>
        <version>21.0.1</version>
    </dependency>
</dependencies>
```

#### module-info.java
```java
module com.mp3player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.mp3player.presentation.view to javafx.fxml;
    exports com.mp3player;
}
```

### Adding New Features

#### Example: Adding a New Use Case
```java
// 1. Create the use case
public class NewFeatureUseCase {
    private final MusicPlayerRepository repository;

    public NewFeatureUseCase(MusicPlayerRepository repository) {
        this.repository = repository;
    }

    public void execute(/* parameters */) {
        // Implementation
    }
}

// 2. Add to ViewModel
private final NewFeatureUseCase newFeatureUseCase;

// 3. Add method
public void newFeature() {
    newFeatureUseCase.execute();
}

// 4. Add to Controller
@FXML
private void onNewFeature() {
    viewModel.newFeature();
}

// 5. Add to FXML
<MenuItem text="New Feature" onAction="#onNewFeature"/>
```

---

## 📊 Statistics

- **Total Classes**: 20+
- **Lines of Code**: 2000+
- **Features**: 25+
- **Keyboard Shortcuts**: 11
- **Menu Items**: 15+
- **Design Patterns**: 6
- **Supported Formats**: 3 (MP3, WAV, M4A)

---

## 🎯 Future Enhancements

### Planned Features (Coming Soon):
- [ ] 10-Band Equalizer
- [ ] Spectrum Analyzer Visualizer
- [ ] ID3 Tag Reading (album, year, genre)
- [ ] Album Art Display
- [ ] Mini Player Mode
- [ ] System Tray Integration
- [ ] Sleep Timer
- [ ] Crossfade between songs
- [ ] Gapless Playback
- [ ] Lyrics Display
- [ ] Themes/Skins

---

## 🐛 Troubleshooting

### Common Issues

#### "No sound when playing"
- Check volume slider (not at 0%)
- Check system volume
- Verify file format is supported

#### "Songs not loading"
- Ensure folder contains MP3/WAV/M4A files
- Check file permissions
- Try "Add Files" instead of "Open Folder"

#### "Application won't start"
- Verify Java 17+ is installed
- Run `mvn clean install` first
- Check console for errors

#### "Keyboard shortcuts not working"
- Make sure the window has focus
- Check if another app is capturing the shortcut
- Try clicking inside the window first

---

## 📄 License

This is an educational project demonstrating Clean Architecture and MVVM patterns with JavaFX.

---

## 🙏 Credits

**Architecture**: Clean Architecture by Robert C. Martin
**Pattern**: MVVM (Model-View-ViewModel)
**Framework**: JavaFX
**Audio**: JavaFX Media API

---

## 📞 Support

For issues or questions:
1. Check the [Keyboard Shortcuts](#-keyboard-shortcuts) section
2. Review the [User Guide](#-user-guide)
3. Check [Troubleshooting](#-troubleshooting)

---

**Enjoy your music!** 🎵✨

Built with ❤️ using JavaFX and Clean Architecture
