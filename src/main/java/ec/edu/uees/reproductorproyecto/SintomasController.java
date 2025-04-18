package ec.edu.uees.reproductorproyecto;

import ec.edu.uees.Modelo.Cola;
import ec.edu.uees.Modelo.Paciente;
import ec.edu.uees.Modelo.PacienteSingleton;
import ec.edu.uees.Modelo.Sintoma;
import ec.edu.uees.Modelo.StageSingleton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SintomasController implements Initializable{
    
    @FXML private AnchorPane root;
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    @FXML private Button minimizar, cerrar, btnAgendar;
    private OperacionesArchivo operaciones = new OperacionesArchivo();
    @FXML private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, 
    cb11, cb12, cb13, cb14, cb15, cb16, cb17, cb18, cb19, cb20, cb21, cb22, cb23, cb24;
    private List<CheckBox> checkBoxes;
    private List<Sintoma> sintomas;
    private Cola cola = Cola.getInstance();
   
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
        
        checkBoxes = checkBoxes = Arrays.asList(cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, 
        cb11, cb12, cb13, cb14, cb15, cb16, cb17, cb18, cb19, cb20, cb21, cb22, cb23, cb24);
        sintomas = operaciones.leerSintomas();
    } 
    
    @FXML
    private void agendarTurno() throws IOException {
        ArrayList<Sintoma> sintomasPaciente = new ArrayList<>();
        List<String> seleccionados = checkBoxes.stream()
                    .filter(CheckBox::isSelected)
                    .map(CheckBox::getText)
                    .collect(Collectors.toList());
        for (Sintoma sintoma : sintomas) {
            if (seleccionados.contains(sintoma.getDescripcion())) {
                sintomasPaciente.add(new Sintoma(sintoma.getDescripcion(), sintoma.getPrioridad()));
            }
        }
        if(sintomasPaciente.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registro de Pacientes");
            alert.setHeaderText("Error al guardar");
            alert.setContentText("Debes escoger al menos un sintoma.");
            alert.showAndWait();
            return;
        }
        Paciente paciente = new Paciente(
        PacienteSingleton.getInstance().getPaciente().getNombres(),
        PacienteSingleton.getInstance().getPaciente().getApellidos(),
        PacienteSingleton.getInstance().getPaciente().getFechaNacimiento(),
        PacienteSingleton.getInstance().getPaciente().getCedula(),
        PacienteSingleton.getInstance().getPaciente().getTelefono(),
        PacienteSingleton.getInstance().getPaciente().getCorreo(),
        PacienteSingleton.getInstance().getPaciente().getGenero(),
        sintomasPaciente
        );
        
        if(operaciones.grabarPaciente(paciente)) {
            cola.agregarPaciente(paciente);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro de Pacientes");
            alert.setHeaderText("Guardando Paciente y Sintomas");
            alert.setContentText("Paciente registrado exitosamente.");
            alert.showAndWait();
            PacienteSingleton.getInstance().setPaciente(null);
            volverHome();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registro de Pacientes");
            alert.setHeaderText("Error al guardar");
            alert.setContentText("No se pudo completar el registro del paciente.");
            alert.showAndWait();        
        }
    }
    
    @FXML
    private void cerrarStage() {
        stage = (Stage) cerrar.getScene().getWindow();
        PacienteSingleton.getInstance().setPaciente(null);
        stage.close();
    }
    @FXML
    private void minimizarStage() {
        stage = (Stage) minimizar.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    private void abrirVentanas() throws IOException{
        App app = new App();
        PacienteSingleton.getInstance().setPaciente(null);
        app.abrirVentanas();
    }
    
    private void volverHome() throws IOException {
        App app = new App();
        Stage stage2 = new Stage();
        stage2.initStyle(javafx.stage.StageStyle.UNDECORATED);
        Scene scene = new Scene(App.loadFXML("ventanahome"));
        String css = getClass().getResource("/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage2.setScene(scene);
        StageSingleton.getInstance().registrarVentana(stage2);
        stage2.show();
    }
}
