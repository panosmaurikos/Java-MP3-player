# Quick Start Guide

## Γρήγορη Εκκίνηση σε 3 Βήματα

### 1️⃣ Βεβαιώσου ότι έχεις Java 17+

```bash
java -version
```

Αν δεν έχεις Java 17+, κατέβασέ το από: https://adoptium.net/

### 2️⃣ Install Maven Dependencies

Άνοιξε terminal στον φάκελο του project και τρέξε:

```bash
mvn clean install
```

Αυτό θα:
- Κατεβάσει όλα τα JavaFX dependencies
- Θα compile το project
- Θα λύσει όλα τα import errors

### 3️⃣ Τρέξε την εφαρμογή

```bash
mvn javafx:run
```

🎉 **Έτοιμο!** Η εφαρμογή θα ανοίξει σε νέο παράθυρο.

---

## Πως να χρησιμοποιήσεις το MP3 Player

1. **Κλικ στο "Load Songs"** - Επέλεξε φάκελο με MP3 files
2. **Διπλό κλικ σε τραγούδι** - Για να παίξει
3. **Χρησιμοποίησε τα controls:**
   - ⏮ Previous
   - ⏯ Play/Pause
   - ⏹ Stop
   - ⏭ Next
   - 🔊 Volume slider

---

## Troubleshooting

### ❌ "javafx cannot be resolved"
**Λύση**: Τρέξε `mvn clean install` για να κατέβασει τα dependencies

### ❌ "Java version not supported"
**Λύση**: Χρειάζεσαι Java 17 ή νεότερο. Κατέβασέ το και ενημέρωσε το `JAVA_HOME`

### ❌ "Module javafx.controls not found"
**Λύση**: Αν τρέχεις από IDE (IntelliJ/Eclipse), add JavaFX VM options:
```
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.media
```

Ή απλά χρησιμοποίησε Maven:
```bash
mvn javafx:run
```

---

## Για Development στο IDE

### IntelliJ IDEA

1. Open project as Maven project
2. Maven → Reload project (να κατεβάσει τα dependencies)
3. Right-click `Main.java` → Run (θα προσθέσει αυτόματα τα JavaFX modules)

### Eclipse

1. File → Import → Maven → Existing Maven Project
2. Right-click project → Maven → Update Project
3. Run configurations → Add VM arguments:
```
--module-path ${JAVAFX_HOME}/lib --add-modules javafx.controls,javafx.fxml,javafx.media
```

### VS Code

1. Install "Extension Pack for Java"
2. Install "JavaFX Support"
3. Open project folder
4. Terminal → `mvn javafx:run`

---

## Επόμενα Βήματα

- Διάβασε το [README.md](README.md) για full documentation
- Δες το [ARCHITECTURE.md](ARCHITECTURE.md) για να καταλάβεις την αρχιτεκτονική
- Προσθέσε δικά σου features!

---

**Απόλαυσε το MP3 Player σου!** 🎵
