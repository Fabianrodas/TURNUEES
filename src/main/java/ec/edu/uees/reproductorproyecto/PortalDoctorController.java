package ec.edu.uees.reproductorproyecto;

import ec.edu.uees.Modelo.Cola;
import ec.edu.uees.Modelo.Doctor;
import ec.edu.uees.Modelo.DoctorSingleton;
import ec.edu.uees.Modelo.Paciente;
import ec.edu.uees.Modelo.SesionSingleton;
import ec.edu.uees.Modelo.Sintoma;
import ec.edu.uees.Modelo.StageSingleton;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PortalDoctorController implements Initializable {
    
    @FXML private AnchorPane root;
    @FXML private VBox vboxSintomas, vboxTurnos;
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    @FXML private Button minimizar, cerrar;
    @FXML private Label txtNombres, txtApellidos, txtEdad, txtCedula, txtGenero, txtTelefono, txtCorreo, doctorEncargado;
    private OperacionesArchivo operaciones = new OperacionesArchivo();
    private List<Paciente> pacientes = operaciones.leerPacientes();
    private Cola cola = Cola.getInstance();
    private Paciente paciente;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        root.setOnMouseDragged(e -> {
            stage = (Stage) cerrar.getScene().getWindow();
            if (!stage.isFullScreen()) {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            }
        });
        
        if(operaciones.leerSesion() == 1) {
            doctorEncargado.setText("Dr. "+operaciones.leerDoctorSesion().getNombres()+" "+
            operaciones.leerDoctorSesion().getApellidos());    
        } else {
            doctorEncargado.setText("Dr. "+DoctorSingleton.getInstance().getDoctor().getNombres()+" "+
            DoctorSingleton.getInstance().getDoctor().getApellidos());
        }
        
        
        String cedulaGuardada = operaciones.leerPacienteActual();
        paciente = null;
        if (cedulaGuardada != null) {
            for (Paciente p : pacientes) {
                if (p.getCedula().equals(cedulaGuardada)) {
                    paciente = p;
                    break;
                }
            }
        }
        if (paciente == null && !cola.getColaPrioridad().isEmpty()) {
            paciente = cola.getColaPrioridad().peek();
        } else {
            if (paciente != null) {
                operaciones.guardarPacienteActual(paciente.getCedula());    
            }
        }
        actualizarInterfazPaciente();
        System.out.println("Paciente actual: " + (paciente != null ? paciente.getNombres() : "Ninguno"));
        
        sincronizarVbox();
        cola.getObservableCola().addListener((ListChangeListener<Paciente>) c -> sincronizarVbox());
    }

    private void actualizarInterfazPaciente() {
        if (paciente != null) {
            txtNombres.setText(paciente.getNombres());
            txtApellidos.setText(paciente.getApellidos());
            txtEdad.setText(Integer.toString(calcularEdad(paciente.getFechaNacimiento())));
            txtCedula.setText(paciente.getCedula()); 
            txtGenero.setText(paciente.getGenero()); 
            txtTelefono.setText(paciente.getTelefono()); 
            txtCorreo.setText(paciente.getCorreo());

            vboxSintomas.getChildren().clear();
            if (paciente.getSintomas().isEmpty()) {
                vboxSintomas.getChildren().add(new Label("No presenta síntomas."));
            } else {
                for (Sintoma s : paciente.getSintomas()) {
                    vboxSintomas.getChildren().add(new Label(s.getDescripcion()));
                }
            }
        } else {
            txtNombres.setText("No hay nombres");
            txtApellidos.setText("No hay apellidos");
            txtEdad.setText("No hay edad");
            txtCedula.setText("No hay cedula"); 
            txtGenero.setText("No hay genero"); 
            txtTelefono.setText("No hay telefono"); 
            txtCorreo.setText("No hay correo");
            
            vboxSintomas.getChildren().clear();
            vboxSintomas.getChildren().add(new Label("No hay síntomas."));
        }
        
    }
    
    private void sincronizarVbox() {
        Platform.runLater(() -> {
            vboxTurnos.getChildren().clear();
            for (Paciente p : cola.getObservableCola()) {
                Label turno = new Label("Turno " + p.getNumeroTurno());
                Label nombre = new Label(p.getNombres() + " " + p.getApellidos());
                vboxTurnos.getChildren().addAll(turno, nombre);
            }
        });
    }

    @FXML
    private void cerrarSesion() throws IOException {
        operaciones.grabarSesion(0);
        operaciones.limpiarDoctorSesion();
        SesionSingleton.getInstance().setSesion(0);
        DoctorSingleton.getInstance().setDoctor(null);
        Stage stage2 = new Stage();
        stage2.initStyle(javafx.stage.StageStyle.UNDECORATED);
        Scene scene = new Scene(App.loadFXML("loginDoctor"));
        String css = getClass().getResource("/css/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage2.setScene(scene);
        StageSingleton.getInstance().registrarVentana(stage2);
        stage2.show();  
    }
    
    @FXML
    private void curarSintomas() {
        if (paciente != null) {
            operaciones.eliminarSintomasPaciente(paciente.getCedula());
            paciente.setSintomas(new ArrayList<>()); 
            operaciones.guardarPacienteActual(paciente.getCedula());
            actualizarInterfazPaciente();
        }
    }
    
    @FXML
    private void completarTurno() {
        if (paciente != null) {
            cola.removePaciente(paciente);
            operaciones.eliminarPaciente(paciente.getCedula());
            operaciones.guardarPacienteActual("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cola de Pacientes");
            alert.setHeaderText("Paciente Curado");
            alert.setContentText("El paciente: " + paciente.getNombres() + " " + paciente.getApellidos() + "\n"
                + "fue curado de sus síntomas.");
            alert.showAndWait();
            paciente = cola.getColaPrioridad().isEmpty() ? null : cola.getColaPrioridad().peek();
            actualizarInterfazPaciente();
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

    private static int calcularEdad(String fechaNacimiento) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fechaNac = LocalDate.parse(fechaNacimiento, formatter);
            LocalDate fechaActual = LocalDate.now();
            return Period.between(fechaNac, fechaActual).getYears();
        } catch (Exception e) {
            System.out.println("Error al calcular la edad: " + e.getMessage());
            return -1;
        }
    }

    public void setPacienteActual(Paciente pacienteActual) {
        this.paciente = pacienteActual;
        actualizarInterfazPaciente();
    }
}