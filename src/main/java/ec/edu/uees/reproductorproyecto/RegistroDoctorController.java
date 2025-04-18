package ec.edu.uees.reproductorproyecto;

import ec.edu.uees.Modelo.Doctor;
import ec.edu.uees.Modelo.StageSingleton;
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

public class RegistroDoctorController implements Initializable {

    @FXML private AnchorPane root;
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private OperacionesArchivo operaciones = new OperacionesArchivo();
    @FXML private Button minimizar, cerrar;
    @FXML private TextField txtNombres, txtApellidos, txtCorreo, txtCedula, txtTelefono;
    @FXML private PasswordField txtContrasena, txtContrasena2;
    
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
    private void registar() throws IOException {
        if(txtNombres.getText().isBlank() || 
        txtApellidos.getText().isBlank() || txtCedula.getText().isBlank() ||txtTelefono.getText().isBlank() || 
        txtContrasena.getText().isBlank() || txtContrasena2.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR); // Datos incompletos
            alert.setHeaderText("Error al registrar");
            alert.setContentText("Por favor llene todas las casillas.");
            alert.showAndWait();
        } else if(!txtCorreo.getText().matches("\\S+@\\S+\\.\\S+")) { // Mal formato correo
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Correo inválido");
            alert.setContentText("Por favor ingresa un correo electrónico válido.");
            alert.showAndWait();
        } else if (!txtContrasena.getText().equals(txtContrasena2.getText())) { // Las contraseñas son distintas
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Contraseñas no coinciden");
            alert.setContentText("Las contraseñas tienen que ser iguales.");
            alert.showAndWait();
        } else {
            Doctor doctor = new Doctor(txtNombres.getText(),txtApellidos.getText(),txtCorreo.getText(),
            txtCedula.getText(),txtTelefono.getText(),txtContrasena.getText());
            if(operaciones.grabarDoctor(doctor)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Registrar Doctor");
                alert.setContentText("Doctor registrado exitosamente.");
                alert.showAndWait();
                irALogin();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Registrar Doctor");
                alert.setContentText("Hubo un error registrando al Doctor.");
                alert.showAndWait();
            }
        }
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
    
    private void irALogin() throws IOException{
        Stage stage2 = new Stage();
        stage2.initStyle(javafx.stage.StageStyle.UNDECORATED);
        Scene scene = new Scene(App.loadFXML("loginDoctor"));
        String css = getClass().getResource("/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage2.setScene(scene);
        StageSingleton.getInstance().registrarVentana(stage2);
        stage2.show();    
    }
}
