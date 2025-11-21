module com.mp3player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.mp3player to javafx.fxml;
    opens com.mp3player.presentation.view to javafx.fxml;

    exports com.mp3player;
    exports com.mp3player.domain.entity;
    exports com.mp3player.domain.repository;
    exports com.mp3player.domain.usecase;
    exports com.mp3player.data.repository;
    exports com.mp3player.presentation.view;
    exports com.mp3player.presentation.viewmodel;
}
