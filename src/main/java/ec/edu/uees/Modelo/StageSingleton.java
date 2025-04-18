package ec.edu.uees.Modelo;

import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;

public class StageSingleton {
    private static StageSingleton instancia;
    private Stage ventanaActiva;
    private final Set<Stage> ventanasAbiertas = new HashSet<>();

    private StageSingleton() {}

    public static StageSingleton getInstance() {
        if (instancia == null) {
            instancia = new StageSingleton();
        }
        return instancia;
    }

    public void registrarVentana(Stage stage) {
        if (ventanaActiva != null) {
            cerrarOtrasVentanas(stage);
        }
        ventanasAbiertas.add(stage);
        ventanaActiva = stage;
    }

    private void cerrarOtrasVentanas(Stage nuevaVentana) {
        for (Stage stage : new HashSet<>(ventanasAbiertas)) {
            if (stage != nuevaVentana) {
                stage.close();
                ventanasAbiertas.remove(stage);
            }
        }
    }

    public boolean isEmpty() {
        return ventanaActiva == null || !ventanaActiva.isShowing();
    }
}
