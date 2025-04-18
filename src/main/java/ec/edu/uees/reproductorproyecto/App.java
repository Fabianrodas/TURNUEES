package ec.edu.uees.reproductorproyecto;

import ec.edu.uees.Modelo.StageSingleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

public class App extends Application {

    private static Scene scene;
    private StageSingleton stageSingleton = StageSingleton.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("mediaplayer"));
        String css = getClass().getResource("/css/styles.css").toExternalForm();
        Image icon = new Image(getClass().getResourceAsStream("/ec/edu/uees/imagenes/ueeslogo.png"));
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        stage.getIcons().add(icon);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    public Scene getScene() {
        return this.scene;
    }

    public void abrirVentanas() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ventana Emergentes");
        alert.setHeaderText("¿Desea abrir el portal?");
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Stage stage2 = new Stage();
            stage2.initStyle(javafx.stage.StageStyle.UNDECORATED);
            scene = new Scene(loadFXML("ventanahome"));
            String css = getClass().getResource("/css/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage2.setScene(scene);
            stageSingleton.registrarVentana(stage2);
            stage2.show();
        }
    }
}