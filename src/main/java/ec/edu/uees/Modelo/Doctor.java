package ec.edu.uees.Modelo;

public class Doctor {
    private String nombres;
    private String apellidos;
    private String correo;
    private String cedula;
    private String telefono;
    private String contrasena;

    public Doctor(String nombres, String apellidos, String correo, String cedula, String telefono, String contrasena) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.cedula = cedula;
        this.telefono = telefono;
        this.contrasena = contrasena;
    }
    
    public Doctor() {}

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "Doctor{" + "nombres=" + nombres + ", apellidos=" + apellidos + ", correo=" + correo + ", cedula=" + cedula + ", telefono=" + telefono + ", contrasena=" + contrasena + '}';
    }
    
    public static Doctor fromString(String linea) {
        try {
            String[] datos = linea.split(",");
            String nombres = datos[0].replace("Doctor{nombres=", "").trim();
            String apellidos = datos[1].replace("apellidos=", "").trim();
            String correo = datos[2].replace("correo=", "").trim();
            String cedula = datos[3].replace("cedula=", "").trim();
            String telefono = datos[4].replace("telefono=", "").trim();
            String contrasena = datos[5].replace("contrasena=", "").replace("}", "").trim();
            return new Doctor(nombres,apellidos,correo,cedula,telefono,contrasena);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
