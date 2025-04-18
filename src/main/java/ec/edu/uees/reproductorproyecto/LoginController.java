package ec.edu.uees.reproductorproyecto;

import ec.edu.uees.Modelo.Doctor;
import ec.edu.uees.Modelo.DoctorSingleton;
import ec.edu.uees.Modelo.SesionSingleton;
import ec.edu.uees.Modelo.StageSingleton;
import javafx.scene.control.CheckBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML private AnchorPane root;
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private OperacionesArchivo operaciones = new OperacionesArchivo();
    @FXML private Button minimizar, cerrar;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private CheckBox recuerdame;
    
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
    private void ingresar() throws IOException {
        if(txtCorreo.getText().isBlank() || txtContrasena.getText().isBlank()){ // Si las casillas estan vacías
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error al ingresar");
            alert.setContentText("Por favor llene todas las casillas.");
            alert.showAndWait();
            return;
        }
        Doctor doctor = operaciones.getDoctor(txtCorreo.getText());
        if(doctor == null) { // Si el correo no esta registrado
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error al ingresar");
            alert.setContentText("Datos incorrectos.");
            alert.showAndWait();
        } else {
            if(!doctor.getContrasena().equals(txtContrasena.getText())) { // Si la contraseña es incorrecta
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error al ingresar");
                alert.setContentText("Datos incorrectos.");
                alert.showAndWait();
            } else {
                if(recuerdame.isSelected()) {
                    operaciones.grabarSesion(1);
                    operaciones.grabarDoctorSesion(doctor);
                }
                DoctorSingleton.getInstance().setDoctor(doctor);
                SesionSingleton.getInstance().setSesion(1);
                Stage stage2 = new Stage();
                stage2.initStyle(javafx.stage.StageStyle.UNDECORATED);
                Scene scene = new Scene(App.loadFXML("ventanadoctor"));
                String css = getClass().getResource("/css/styles.css").toExternalForm();
                scene.getStylesheets().add(css);
                stage2.setScene(scene);
                StageSingleton.getInstance().registrarVentana(stage2);
                stage2.show();   
            }
        }
    }
    
    @FXML
    private void registrar() throws IOException {
        Stage stage2 = new Stage();
        stage2.initStyle(javafx.stage.StageStyle.UNDECORATED);
        Scene scene = new Scene(App.loadFXML("registroDoctor"));
        String css = getClass().getResource("/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage2.setScene(scene);
        StageSingleton.getInstance().registrarVentana(stage2);
        stage2.show(); 
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
    private void abrirVentanas() throws IOException {
        App app = new App();
        app.abrirVentanas();
    }
}
