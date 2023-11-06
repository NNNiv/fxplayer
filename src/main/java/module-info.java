module player.music {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens player.music to javafx.fxml;
    exports player.music;
}