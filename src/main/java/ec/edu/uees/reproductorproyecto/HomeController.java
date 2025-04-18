package ec.edu.uees.reproductorproyecto;

import ec.edu.uees.Modelo.SesionSingleton;
import ec.edu.uees.Modelo.StageSingleton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeController implements Initializable{

    @FXML private AnchorPane root;
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    @FXML private Button minimizar, cerrar;
    private OperacionesArchivo operaciones = new OperacionesArchivo();

      @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        
        root.setOnMouseDragged(e -> {
            stage = (Stage) cerrar.getScene().getWindow();
            if(!stage.isFullScreen()){
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            }
        });
    }
    
    @FXML
    private void cerrarStage() {
        stage = (Stage) cerrar.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void minimizarStage() {
        stage = (Stage) minimizar.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @FXML 
    private void abrirUsuario() throws IOException{
        Stage stage2 = new Stage();
        stage2.initStyle(javafx.stage.StageStyle.UNDECORATED);
        Scene scene = new Scene(App.loadFXML("registropaciente"));
        String css = getClass().getResource("/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage2.setScene(scene);
        StageSingleton.getInstance().registrarVentana(stage2);
        stage2.show();
    }
    
    @FXML
    private void abrirDoctor() throws IOException{
        if(operaciones.leerSesion() == 1 || SesionSingleton.getInstance().getSesion() == 1) {
            Stage stage3 = new Stage();
            stage3.initStyle(javafx.stage.StageStyle.UNDECORATED);
            Scene scene = new Scene(App.loadFXML("ventanadoctor"));
            String css = getClass().getResource("/css/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage3.setScene(scene);
            StageSingleton.getInstance().registrarVentana(stage3);
            stage3.show();    
        } else {
            Stage stage3 = new Stage();
            stage3.initStyle(javafx.stage.StageStyle.UNDECORATED);
            Scene scene = new Scene(App.loadFXML("loginDoctor"));
            String css = getClass().getResource("/css/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage3.setScene(scene);
            StageSingleton.getInstance().registrarVentana(stage3);
            stage3.show();    
        }
    }
}
