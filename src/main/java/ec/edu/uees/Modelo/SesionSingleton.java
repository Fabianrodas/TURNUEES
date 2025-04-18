package ec.edu.uees.Modelo;

public class SesionSingleton {
    private static SesionSingleton instancia;
    private int sesion;

    private SesionSingleton() {
    }

    public static SesionSingleton getInstance() {
        if (instancia == null) {
            instancia = new SesionSingleton();
        }
        return instancia;
    }

    public void setSesion(int sesion) {
        this.sesion = sesion;
    }

    public int getSesion() {
        return sesion;
    }

}
