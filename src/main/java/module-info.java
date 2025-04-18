module ec.edu.uees.reproductorproyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    opens ec.edu.uees.reproductorproyecto to javafx.fxml;
    exports ec.edu.uees.reproductorproyecto;
}
