package ec.edu.uees.Modelo;

public class DoctorSingleton {
    private static DoctorSingleton instancia;
    private Doctor doctor;

    private DoctorSingleton() {
    }

    public static DoctorSingleton getInstance() {
        if (instancia == null) {
            instancia = new DoctorSingleton();
        }
        return instancia;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Doctor getDoctor() {
        return doctor;
    }
    
    public boolean isFull() {
        return this.doctor != null;
    }
    
    public void vaciarDoctor() {
        this.doctor = null;
    }

}
