package ec.edu.uees.Modelo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Paciente implements Comparable<Paciente>{
    private String nombres;
    private String apellidos;
    private String fechaNacimiento;
    private String cedula;
    private String telefono;
    private String correo;
    private String genero;
    private ArrayList<Sintoma> sintomas;
    private double prioridadTotal;
    private int numeroTurno;

    public Paciente(String nombres, String apellidos, String fechaNacimiento, String cedula, String telefono, String correo, String genero) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.genero = genero;
    }

    public Paciente(String nombres, String apellidos, String fechaNacimiento, String cedula, String telefono, String correo, String genero, ArrayList<Sintoma> sintomas) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.genero = genero;
        this.sintomas = sintomas;
        this.prioridadTotal = getPrioridadTotal();
    }
    
    public Paciente(String nombres, String apellidos, String fechaNacimiento, String cedula, String telefono, String correo, String genero, ArrayList<Sintoma> sintomas, double prioridadTotal) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.genero = genero;
        this.sintomas = sintomas;
        this.prioridadTotal = prioridadTotal;
    }

    public Paciente(String nombres, String apellidos, String fechaNacimiento, String cedula, String telefono, String correo, String genero, ArrayList<Sintoma> sintomas, double prioridadTotal, int numeroTurno) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.genero = genero;
        this.sintomas = sintomas;
        this.prioridadTotal = prioridadTotal;
        this.numeroTurno = numeroTurno;
    }
    
    public Paciente() {
    }
    
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        String sintomasStr = (sintomas == null || sintomas.isEmpty()) ? "sintomas=[]" : sintomas.toString();

        return "Paciente{" + 
               "nombres=" + nombres + 
               ", apellidos=" + apellidos + 
               ", fechaNacimiento=" + fechaNacimiento + 
               ", cedula=" + cedula + 
               ", telefono=" + telefono + 
               ", correo=" + correo + 
               ", genero=" + genero + 
               ", sintomas=" + sintomasStr + 
               ", prioridadTotal=" + prioridadTotal + 
               '}';
    }

    public static Paciente fromString(String linea) {
        try {
            int indexSintomas = linea.indexOf("sintomas=[");
            int indexPrioridad = linea.lastIndexOf(", prioridadTotal=");
            if (indexSintomas == -1 || indexPrioridad == -1) {
                return null;
            }
            String datosPrincipales = linea.substring(0, indexSintomas).trim();
            String sintomasStr = linea.substring(indexSintomas, indexPrioridad).trim();
            String prioridadStr = linea.substring(indexPrioridad).replace(", prioridadTotal=", "").replace("}", "").trim();
            String[] datos = datosPrincipales.split(", ");
            if (datos.length < 7) {
                return null;
            }
            String nombres = datos[0].replace("Paciente{nombres=", "").trim();
            String apellidos = datos[1].replace("apellidos=", "").trim();
            String fechaNacimiento = datos[2].replace("fechaNacimiento=", "").trim();
            String cedula = datos[3].replace("cedula=", "").trim();
            String telefono = datos[4].replace("telefono=", "").trim();
            String correo = datos[5].replace("correo=", "").trim();
            String genero = datos[6].replace("genero=", "").trim();
            genero = genero.replace(",", "");
            ArrayList<Sintoma> listaSintomas = toSintomas(sintomasStr);
            double prioridadTotal = Double.parseDouble(prioridadStr);
            return new Paciente(nombres, apellidos, fechaNacimiento, cedula, telefono, correo, genero, listaSintomas, prioridadTotal);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
  
    public double getPrioridadTotal() {
        double sumatoria = 0;
        if(sintomas.isEmpty()) return 0;
        for (Sintoma s : sintomas) {
            sumatoria += (4.0 - s.getPrioridad()) / s.getPrioridad();
        }
        return sumatoria;
    }

    @Override
    public int compareTo(Paciente o) {
        return Double.compare(o.prioridadTotal, this.prioridadTotal);
    }
    
    private static ArrayList<Sintoma> toSintomas(String datos) {
        ArrayList<Sintoma> listaSintomas = new ArrayList<>();
        datos = datos.replaceAll("[\\[\\]]", "").trim();
        Pattern pattern = Pattern.compile("Sintoma\\{descripcion=([^}]+), prioridad=(\\d+)\\}");
        Matcher matcher = pattern.matcher(datos);
        while (matcher.find()) {
            String descripcion = matcher.group(1).trim();
            int prioridad = Integer.parseInt(matcher.group(2).trim());
            listaSintomas.add(new Sintoma(descripcion, prioridad));
        }
        return listaSintomas;
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public void setNumeroTurno(int numeroTurno) {
        if (this.numeroTurno == 0) {
            this.numeroTurno = numeroTurno;
        }
    }

    public ArrayList<Sintoma> getSintomas() {
        return sintomas;
    }

    public void setSintomas(ArrayList<Sintoma> sintomas) {
        this.sintomas = sintomas;
    }
    
}
