module brickGame {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;

    exports game;
    opens game to javafx.fxml;
    exports entities;
    opens entities to javafx.fxml;
    exports managers;
    opens managers to javafx.fxml;
    exports io;
    opens io to javafx.fxml;
    exports enums;
    opens enums to javafx.fxml;
    exports ui;
    opens ui to javafx.fxml;
}