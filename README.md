# 🎵 MP3 Player - Clean Architecture & MVVM

A modern, feature-rich MP3 Player built with **Java** and **JavaFX**, following **Clean Architecture** principles and **MVVM Pattern**.

## 🎨 Modern UI Features

- **Gradient Backgrounds**: Beautiful color gradients throughout the UI
- **Glass Morphism**: Semi-transparent elements with blur effects
- **Smooth Animations**: Hover effects and transitions
- **Album Art Placeholder**: Visual representation for currently playing song
- **Responsive Design**: Adapts to different window sizes
- **Dark Theme**: Easy on the eyes with vibrant accent colors

## ✨ Features

### 🎵 Playback Controls
- ▶️ **Play/Pause/Stop**: Full playback control
- ⏭️ **Next/Previous**: Navigate through playlist
- 🔀 **Shuffle Mode**: Randomize playback order
- 🔁 **Repeat Mode**: Loop through playlist automatically
- ⏱️ **Seek Functionality**: Click/drag progress bar to jump to any position

### 📋 Playlist Management
- 📁 **Load Songs**: Import MP3/WAV/M4A files from any folder
- 🔍 **Search**: Real-time search by song title or artist
- 📊 **ListView**: See all songs with selection highlighting
- 🎯 **Double-click to Play**: Quick song selection

### 🔊 Audio Controls
- **Volume Slider**: Precise volume control (0-100%)
- **Volume Percentage**: Visual feedback of current volume
- **Dynamic Volume Icon**: Changes based on volume level (🔇 🔈 🔉 🔊)
- **Application-Level Volume**: Independent from system volume

### 📊 Progress Tracking
- **Real-time Progress Bar**: Visual playback position
- **Time Display**: Current time / Total duration (MM:SS format)
- **Interactive Seeking**: Click anywhere on progress bar to seek

## 🏗️ Architecture

This project follows **Clean Architecture** with three distinct layers:

### 1️⃣ Domain Layer (Business Logic)
```
📂 domain/
├── entity/         # Core entities (Song, Playlist)
├── repository/     # Repository interfaces
└── usecase/        # Business operations
    ├── PlaySongUseCase
    ├── PauseSongUseCase
    ├── SetVolumeUseCase
    └── LoadSongsUseCase
```

### 2️⃣ Data Layer (Implementation)
```
📂 data/
└── repository/     # Concrete implementations
    ├── JavaFXMusicPlayerRepository  # JavaFX Media API
    └── FilePlaylistRepository       # File system ops
```

### 3️⃣ Presentation Layer (UI - MVVM)
```
📂 presentation/
├── viewmodel/      # Observable properties & commands
│   └── MusicPlayerViewModel
└── view/           # FXML + Controllers
    ├── MainController
    ├── main-view.fxml
    └── style.css
```

## 📂 Project Structure

```
src/
├── main/
│   ├── java/com/mp3player/
│   │   ├── Main.java                 # Application entry point
│   │   ├── domain/                   # Business logic layer
│   │   ├── data/                     # Data access layer
│   │   └── presentation/             # UI layer (MVVM)
│   └── resources/
│       └── com/mp3player/presentation/view/
│           ├── main-view.fxml        # UI layout
│           └── style.css             # Modern styling
└── pom.xml                           # Maven configuration
```

## 🚀 Quick Start

### Prerequisites
- **Java 17+** ([Download JDK](https://adoptium.net/))
- **Maven 3.6+**

### Installation & Run

```bash
# 1. Navigate to project directory
cd "c:\Users\Panos\Desktop\MP3 player"

# 2. Install dependencies (downloads JavaFX automatically)
mvn clean install

# 3. Run the application
mvn javafx:run
```

That's it! 🎉

## 🎮 How to Use

1. **📁 Load Songs**: Click "📁 Load Songs" button and select a folder containing MP3 files
2. **🔍 Search**: Type in search box to filter songs by title or artist
3. **🎵 Play**: Double-click any song to start playing
4. **⏯ Controls**: Use playback buttons to control music
5. **🔀 Shuffle**: Toggle shuffle button to randomize playback
6. **🔁 Repeat**: Toggle repeat button to loop playlist
7. **🔊 Volume**: Adjust volume slider (independent from system volume)
8. **⏱️ Seek**: Click/drag progress bar to jump to any position

## 🎨 UI Screenshots

### Main Interface
- **Top Bar**: App title with gradient effect
- **Now Playing**: Album art placeholder + song info
- **Search Bar**: Real-time song filtering
- **Playlist**: Scrollable list with hover effects
- **Controls**: Circular buttons with glow effects
- **Progress**: Interactive seek bar with time display

## 🔧 Technologies

| Technology | Purpose |
|-----------|---------|
| **Java 17** | Core language |
| **JavaFX 21** | UI framework & Media playback |
| **JavaFX Media API** | MP3/WAV/M4A playback |
| **Maven** | Build & dependency management |
| **MVVM Pattern** | Presentation layer architecture |
| **Clean Architecture** | Overall project structure |

## 📖 Architecture Benefits

### ✅ Testability
Each layer can be tested independently with mock implementations.

### ✅ Maintainability
Clear separation makes code easy to understand and modify.

### ✅ Flexibility
Swap implementations easily (e.g., switch from JavaFX Media to another library).

### ✅ Scalability
Add new features without affecting existing code.

## 🎯 Key Design Patterns

1. **Repository Pattern**: Abstraction for data access
2. **Use Case Pattern**: Single responsibility for each business operation
3. **MVVM Pattern**: Separation of UI logic from business logic
4. **Dependency Inversion**: High-level modules don't depend on low-level modules
5. **Observer Pattern**: JavaFX Observable properties for reactive UI

## 🔊 Volume Control Explanation

### Question: "Why doesn't the app volume affect system volume?"

**Answer**: This is **intentional and correct behavior**!

The volume slider in the app controls **application-level volume** only, similar to how Spotify, VLC, or any professional music player works:

- **App Volume = 0**: No sound from app (even if system volume is 100%)
- **App Volume = 100**: Maximum app volume (limited by system volume)
- **Final Volume**: `System Volume × App Volume`

**Benefits**:
- ✅ Control each app's volume independently
- ✅ Don't interfere with other applications
- ✅ More precise volume control
- ✅ Safer (no accidental system-wide volume changes)

## 🎨 New Features in v2.0

### What's New?
- ✨ **Modern UI**: Gradient backgrounds, glass effects, glow animations
- 🔍 **Search**: Real-time song filtering
- 🔀 **Shuffle**: Randomized playback
- 🔁 **Repeat**: Loop playlist automatically
- ⏱️ **Seek**: Click progress bar to jump to position
- 🎨 **Album Art**: Visual placeholder for current song
- 🔊 **Smart Volume Icon**: Changes based on volume level
- 📊 **Better Progress**: Interactive seek slider
- 🎯 **Improved Layout**: More space, better organization

## 📝 Supported Formats

- 🎵 **MP3** - MPEG Audio Layer 3
- 🎵 **WAV** - Waveform Audio File Format
- 🎵 **M4A** - MPEG-4 Audio

## 🐛 Troubleshooting

### ❌ "javafx cannot be resolved"
**Solution**: Run `mvn clean install` to download JavaFX dependencies.

### ❌ "Java version not supported"
**Solution**: Install Java 17+ and ensure `JAVA_HOME` is set correctly.

### ❌ IDE Errors
**Solution**: All import errors are normal before running Maven. They will disappear after `mvn clean install`.

## 🔮 Future Enhancements

- [ ] ID3 tag reading for metadata (cover art, album info)
- [ ] Playlist persistence (save/load playlists)
- [ ] Equalizer with presets
- [ ] Lyrics display
- [ ] Multiple playlists support
- [ ] Keyboard shortcuts
- [ ] Mini player mode
- [ ] System tray integration
- [ ] Theme customization

## 📚 Documentation

- 📖 **[README.md](README.md)** - This file
- ⚡ **[QUICKSTART.md](QUICKSTART.md)** - 3-step quickstart guide
- 🏗️ **[ARCHITECTURE.md](ARCHITECTURE.md)** - Detailed architecture diagrams
- 📝 **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - Project overview

## 💡 Learning Resources

This project demonstrates:
- ✅ Clean Architecture in practice
- ✅ MVVM pattern with JavaFX
- ✅ Dependency Injection
- ✅ Use Case-driven design
- ✅ Repository pattern
- ✅ Observable properties for reactive UI
- ✅ Modern CSS styling in JavaFX

## 🤝 Contributing

Feel free to:
- Report bugs
- Suggest features
- Submit pull requests
- Improve documentation

## 📄 License

This is an educational project demonstrating Clean Architecture and MVVM patterns.

---

**Built with** ❤️ **using Clean Architecture & MVVM**

**Tech Stack**: Java 17 • JavaFX 21 • Maven • MVVM • Clean Architecture
