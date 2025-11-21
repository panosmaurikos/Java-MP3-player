# MP3 Player - Διορθώσεις Ολοκληρώθηκαν! ✅

## 🔧 Προβλήματα που Λύθηκαν

### 1. ✅ **Διαφορά μεταξύ "Open Folder" και "Add Files"**

**Πρόβλημα:**
- Το "Add Files" είχε λάθος υλοποίηση
- Φόρτωνε **ολόκληρο** τον φάκελο αντί για μόνο τα επιλεγμένα αρχεία

**Λύση:**
- **Open Folder** (`Ctrl+O`): Φορτώνει **ΟΛΑ** τα MP3 αρχεία από έναν φάκελο
- **Add Files** (`Ctrl+F`): Προσθέτει **ΜΟΝΟ** τα επιλεγμένα αρχεία που διαλέγεις

**Αλλαγές στον Κώδικα:**

#### MainController.java
```java
@FXML
private void onAddFiles() {
    javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
    fileChooser.setTitle("Select MP3 Files");
    fileChooser.getExtensionFilters().add(
        new javafx.stage.FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.m4a")
    );
    java.util.List<File> files = fileChooser.showOpenMultipleDialog(playlistView.getScene().getWindow());

    if (files != null && !files.isEmpty()) {
        // Add only the selected files (not entire folders) ✅
        for (File file : files) {
            viewModel.addSingleFile(file.getAbsolutePath());
        }
    }
}
```

#### MusicPlayerViewModel.java - ΝΕΑ ΜΕΘΟΔΟΣ
```java
public void addSingleFile(String filePath) {
    // Create a Song from the single file
    java.io.File file = new java.io.File(filePath);
    if (file.exists() && file.isFile()) {
        String fileName = file.getName();
        String title = fileName.substring(0, fileName.lastIndexOf('.'));
        String artist = "Unknown Artist";

        // Parse title and artist from filename (e.g., "Artist - Title.mp3")
        if (title.contains(" - ")) {
            String[] parts = title.split(" - ", 2);
            artist = parts[0].trim();
            title = parts[1].trim();
        }

        Song song = new Song(
            java.util.UUID.randomUUID().toString(),
            title,
            artist,
            filePath,
            Duration.ZERO
        );

        playlist.addSong(song);
        updateSongsList();
        updateFavoritesList();

        if (playlist.size() == 1) {
            currentSong.set(playlist.getCurrentSong());
        }
    }
}
```

---

### 2. ✅ **Προσθήκη Delete Song Functionality**

**Πρόβλημα:**
- Δεν υπήρχε τρόπος να διαγράψεις τραγούδι από τη λίστα

**Λύση:**
- **Δεξί κλικ (Right-click)** σε οποιοδήποτε τραγούδι
- Εμφανίζεται Context Menu με επιλογές:
  - **Play** - Παίζει το τραγούδι
  - **Toggle Favorite** - Προσθήκη/αφαίρεση από favorites
  - **Delete from Playlist** - Διαγραφή από τη λίστα

**Αλλαγές στον Κώδικα:**

#### MusicPlayerViewModel.java - ΝΕΑ ΜΕΘΟΔΟΣ
```java
public void removeSong(Song song) {
    if (song != null) {
        int index = songs.indexOf(song);
        if (index >= 0) {
            playlist.removeSong(index);
            updateSongsList();
            updateFavoritesList();
        }
    }
}
```

#### MainController.java - Context Menu για All Songs Tab
```java
private void setupPlaylistContextMenu() {
    javafx.scene.control.ContextMenu contextMenu = new javafx.scene.control.ContextMenu();

    javafx.scene.control.MenuItem playItem = new javafx.scene.control.MenuItem("Play");
    playItem.setOnAction(e -> {
        Song selected = playlistView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.playSong(selected);
        }
    });

    javafx.scene.control.MenuItem deleteItem = new javafx.scene.control.MenuItem("Delete from Playlist");
    deleteItem.setOnAction(e -> {
        Song selected = playlistView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.removeSong(selected);
        }
    });

    javafx.scene.control.MenuItem favoriteItem = new javafx.scene.control.MenuItem("Toggle Favorite");
    favoriteItem.setOnAction(e -> {
        Song selected = playlistView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.toggleFavorite(selected);
        }
    });

    contextMenu.getItems().addAll(playItem, favoriteItem, new javafx.scene.control.SeparatorMenuItem(), deleteItem);
    playlistView.setContextMenu(contextMenu);
}
```

#### MainController.java - Context Menu για Favorites Tab
```java
private void setupFavoritesContextMenu() {
    javafx.scene.control.ContextMenu contextMenu = new javafx.scene.control.ContextMenu();

    javafx.scene.control.MenuItem playItem = new javafx.scene.control.MenuItem("Play");
    playItem.setOnAction(e -> {
        Song selected = favoritesView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.playSong(selected);
        }
    });

    javafx.scene.control.MenuItem unfavoriteItem = new javafx.scene.control.MenuItem("Remove from Favorites");
    unfavoriteItem.setOnAction(e -> {
        Song selected = favoritesView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.toggleFavorite(selected);
        }
    });

    javafx.scene.control.MenuItem deleteItem = new javafx.scene.control.MenuItem("Delete from Playlist");
    deleteItem.setOnAction(e -> {
        Song selected = favoritesView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.removeSong(selected);
        }
    });

    contextMenu.getItems().addAll(playItem, unfavoriteItem, new javafx.scene.control.SeparatorMenuItem(), deleteItem);
    favoritesView.setContextMenu(contextMenu);
}
```

---

## 📝 Πώς να Χρησιμοποιήσεις τις Νέες Λειτουργίες

### 🎵 Προσθήκη Τραγουδιών

**Τρόπος 1: Open Folder (Φόρτωση φακέλου)**
1. Κλικ στο `📁 Open Folder` ΄Η πατήστε `Ctrl+O`
2. Διάλεξε φάκελο με MP3
3. **Όλα** τα τραγούδια θα φορτώσουν αυτόματα

**Τρόπος 2: Add Files (Επιλογή συγκεκριμένων αρχείων)**
1. Κλικ στο `➕ Add Files` Ή πατήστε `Ctrl+F`
2. Διάλεξε **συγκεκριμένα** αρχεία (μπορείς να διαλέξεις πολλά με Ctrl+Click)
3. **Μόνο** τα επιλεγμένα αρχεία θα προστεθούν

### 🗑️ Διαγραφή Τραγουδιών

**Από την "All Songs" λίστα:**
1. **Δεξί κλικ** πάνω σε τραγούδι
2. Επίλεξε `Delete from Playlist`
3. Το τραγούδι διαγράφεται

**Από την "Favorites" λίστα:**
1. **Δεξί κλικ** πάνω σε αγαπημένο τραγούδι
2. Επίλεξε `Remove from Favorites` - Αφαιρεί το ⭐ αλλά το κρατάει στη λίστα
3. Ή επίλεξε `Delete from Playlist` - Το διαγράφει εντελώς

---

## ✨ Context Menu Options

### Στην "All Songs" Tab:
- **Play** - Παίζει το τραγούδι
- **Toggle Favorite** - Προσθέτει/αφαιρεί το ⭐
- **━━━━━** (διαχωριστής)
- **Delete from Playlist** - Διαγράφει το τραγούδι

### Στην "⭐ Favorites" Tab:
- **Play** - Παίζει το τραγούδι
- **Remove from Favorites** - Αφαιρεί το ⭐ (κρατάει στη λίστα)
- **━━━━━** (διαχωριστής)
- **Delete from Playlist** - Διαγράφει το τραγούδι εντελώς

---

## 🎯 Σύνοψη Αλλαγών

### Νέες Μέθοδοι στο ViewModel:
1. ✅ `addSingleFile(String filePath)` - Προσθέτει ένα μόνο αρχείο
2. ✅ `removeSong(Song song)` - Διαγράφει τραγούδι

### Νέες Μέθοδοι στο Controller:
1. ✅ `setupPlaylistContextMenu()` - Context menu για All Songs
2. ✅ `setupFavoritesContextMenu()` - Context menu για Favorites

### Ενημερωμένες Μέθοδοι:
1. ✅ `onAddFiles()` - Τώρα προσθέτει μόνο επιλεγμένα αρχεία
2. ✅ `loadSongs()` - Ενημερώνει και favorites μετά τη φόρτωση

---

## 🚀 Για να Τρέξεις την Εφαρμογή

```bash
mvn clean install
mvn javafx:run
```

---

## 🎉 Τι Έγινε Καλύτερα

**Πριν:**
- ❌ Add Files φόρτωνε ολόκληρο φάκελο
- ❌ Δεν μπορούσες να διαγράψεις τραγούδια
- ❌ Έπρεπε να κάνεις "Clear All" για να αδειάσεις τη λίστα

**Τώρα:**
- ✅ Add Files προσθέτει **μόνο** τα επιλεγμένα αρχεία
- ✅ Open Folder φορτώνει **ολόκληρο** φάκελο
- ✅ Δεξί κλικ σε τραγούδι → Delete
- ✅ Context menu με Play, Favorite, Delete επιλογές
- ✅ Λειτουργεί και στα "All Songs" και στα "Favorites"

---

**Τώρα έχεις έναν ΠΛΗΡΗ MP3 Player!** 🎵✨

Όλες οι λειτουργίες δουλεύουν όπως πρέπει:
- ⭐ Favorites
- 🎚️ Speed Control
- ➕ Add individual files
- 📁 Load entire folder
- 🗑️ Delete songs with right-click
- ⌨️ Keyboard shortcuts

**Απόλαυσέ τον!** 🎉
