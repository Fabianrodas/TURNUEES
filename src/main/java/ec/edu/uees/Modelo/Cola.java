package ec.edu.uees.Modelo;

import ec.edu.uees.reproductorproyecto.OperacionesArchivo;
import java.util.List;
import java.util.PriorityQueue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cola {
    private static Cola instancia;
    private OperacionesArchivo operaciones = new OperacionesArchivo();
    private PriorityQueue<Paciente> colaPrioridad;
    private ObservableList<Paciente> observableCola;

    private Cola() {
        List<Paciente> pacientes = operaciones.leerPacientes();
        observableCola = FXCollections.observableArrayList();
        colaPrioridad = organizarPorPrioridad(pacientes);
        actualizarObservableList(); 
    }

    public static Cola getInstance() {
        if (instancia == null) {
            instancia = new Cola();
        }
        return instancia;
    }

    private PriorityQueue<Paciente> organizarPorPrioridad(List<Paciente> pacientes) {
        PriorityQueue<Paciente> colaTemporal = new PriorityQueue<>(
            (p1, p2) -> Double.compare(p2.getPrioridadTotal(), p1.getPrioridadTotal())
        );
        for (Paciente p : pacientes) {
            colaTemporal.offer(p);
        }
        PriorityQueue<Paciente> colaFinal = new PriorityQueue<>(
            (p1, p2) -> Double.compare(p2.getPrioridadTotal(), p1.getPrioridadTotal())
        );
        int numeroTurno = 1;
        while (!colaTemporal.isEmpty()) {
            Paciente p = colaTemporal.poll();
            p.setNumeroTurno(numeroTurno++);
            colaFinal.offer(p);
        }
        return colaFinal;
    }

    public void removePaciente(Paciente p) {
        colaPrioridad.remove(p);
        actualizarObservableList();
    }
    
    public void agregarPaciente(Paciente p) {
        colaPrioridad.offer(p);
        List<Paciente> pacientes = operaciones.leerPacientes();
        colaPrioridad = organizarPorPrioridad(pacientes);
        actualizarObservableList();
    }

    public void actualizarObservableList() {
        if (observableCola != null) {
            observableCola.setAll(colaPrioridad);
        }
    }

    public ObservableList<Paciente> getObservableCola() {
        return observableCola;
    }

    public PriorityQueue<Paciente> getColaPrioridad() {
        return colaPrioridad;
    }

}

