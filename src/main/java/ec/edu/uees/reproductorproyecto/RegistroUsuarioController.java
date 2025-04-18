package ec.edu.uees.reproductorproyecto;

import ec.edu.uees.Modelo.Paciente;
import ec.edu.uees.Modelo.PacienteSingleton;
import ec.edu.uees.Modelo.StageSingleton;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class RegistroUsuarioController implements Initializable{
    @FXML private TextField txtnombres;
    @FXML private TextField txtapellidos;
    @FXML private DatePicker txtfechanacimiento;
    @FXML private TextField txtcedula;
    @FXML private TextField txtcorreo;
    @FXML private TextField txttelefono;
    @FXML private RadioButton generoFemenino;
    @FXML private Button cerrar;
    @FXML private Button minimizar;
   
    @FXML private AnchorPane root;
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;

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
        
        if(txtfechanacimiento != null){
            txtfechanacimiento.setDayCellFactory(getDayCellFactory());
        }
    }
    
    private void registrarPaciente(){
        try {
            String nombres = txtnombres.getText();
            String apellidos = txtapellidos.getText();
            String fechaNacimiento = txtfechanacimiento.getValue().toString();
            String cedula = txtcedula.getText();
            String telefono = txttelefono.getText();
            String correo = txtcorreo.getText();
            String genero;
            if (generoFemenino.isSelected()){
                genero = "Femenino";
            } else {
                genero = "Masculino";
            }
            
            if (nombres == null || apellidos == null || correo == null || fechaNacimiento == null || nombres.isBlank() 
            || apellidos.isBlank() || genero.isBlank() || correo.isBlank() || fechaNacimiento.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error al registrar");
                alert.setContentText("Por favor llene todas las casillas.");
                alert.showAndWait();
                return;
            } else if (!correo.matches("\\S+@\\S+\\.\\S+")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Correo inválido");
                alert.setContentText("Por favor ingresa un correo electrónico válido.");
                alert.showAndWait();
                return;
            }
            Paciente p = new Paciente(nombres, apellidos, fechaNacimiento, cedula, telefono, correo, genero);
            PacienteSingleton.getInstance().setPaciente(p);
        } catch (Exception e) {
            System.out.println("No lleno nada");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error al registrar");
            alert.setContentText("Por favor llene todas las casillas.");
            alert.showAndWait();
        }
        
    }

    private Callback<DatePicker, DateCell> getDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #b5b3b3");
                }
            }
        };
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
    private void seleccionarSintomas() throws IOException{
        registrarPaciente();
        if(PacienteSingleton.getInstance().isFull()){
            Stage stage2 = new Stage();
            stage2.initStyle(javafx.stage.StageStyle.UNDECORATED);
            Scene scene = new Scene(App.loadFXML("sintomas"));
            String css = getClass().getResource("/css/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage2.setScene(scene);
            StageSingleton.getInstance().registrarVentana(stage2);
            stage2.show();    
        }
    }
}
