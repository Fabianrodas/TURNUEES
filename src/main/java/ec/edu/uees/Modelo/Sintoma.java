package ec.edu.uees.Modelo;

public class Sintoma {
    private String descripcion;
    private int prioridad;

    public Sintoma(String descripcion, int prioridad) {
        this.descripcion = descripcion;
        this.prioridad = prioridad;
    }

    public Sintoma() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public String toString() {
        return "Sintoma{" + "descripcion=" + descripcion + ", prioridad=" + prioridad + '}';
    }
    
    public static Sintoma fromString(String linea) {
        String[] datos = linea.split(",");
        if (datos.length == 2) {
            return new Sintoma(datos[0],Integer.parseInt(datos[1]));
        }
        return null;
    }
}
