package ec.edu.uees.Modelo;

public class PacienteSingleton {
    private static PacienteSingleton instancia;
    private Paciente paciente;

    private PacienteSingleton() {
    }

    public static PacienteSingleton getInstance() {
        if (instancia == null) {
            instancia = new PacienteSingleton();
        }
        return instancia;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Paciente getPaciente() {
        return paciente;
    }
    
    public boolean isFull() {
        return this.paciente != null;
    }
    
    public void vaciarPaciente() {
        this.paciente = null;
    }
}