package ec.edu.uees.reproductorproyecto;
import ec.edu.uees.Modelo.Doctor;
import ec.edu.uees.Modelo.Paciente;
import ec.edu.uees.Modelo.Sintoma;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OperacionesArchivo {
    
    public boolean grabarPaciente(Paciente paciente) {
        String archivo = "archivos/pacientes.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) { // `true` para no sobrescribir
            bw.write(paciente.toString());
            bw.newLine();
            return true;
        } catch (IOException ex) {
            System.out.println("Error al escribir el archivo: " + ex.getMessage());
            return false;
        }
    }
    
    public List<Paciente> leerPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        String archivo = "archivos/pacientes.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Paciente paciente = Paciente.fromString(linea);
                if (paciente != null) {
                    pacientes.add(paciente);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error de lectura: " + ex.getMessage());
        }

        return pacientes;
    }
    
    public boolean grabarSesion(int sesion) {
        String archivo = "archivos/sesion.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            bw.write(""+sesion);
            bw.newLine();
            return true;
        } catch (IOException ex) {
            System.out.println("Error al escribir el archivo: " + ex.getMessage());
            return false;
        }
    }
    

    public int leerSesion() {
        String archivo = "archivos/sesion.txt";
        int sesion = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea = br.readLine();
            if(linea == null || linea.isBlank() || !linea.matches("\\d+")) {
                return sesion;
            }
            sesion = Integer.parseInt(linea);
        } catch (IOException ex) {
            System.out.println("Error de lectura: " + ex.getMessage());
        }
        return sesion;
    }
    
    public List<Sintoma> leerSintomas() {
        List<Sintoma> sintomas = new ArrayList<>();
        String archivo = "archivos/sintomas.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Sintoma sintoma = Sintoma.fromString(linea);
                if (sintoma != null) {
                    sintomas.add(sintoma);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error de lectura: " + ex.getMessage());
        }

        return sintomas;
    }

    public boolean eliminarSintomasPaciente(String cedulaPaciente) {
        String archivo = "archivos/pacientes.txt";
        List<Paciente> pacientes = leerPacientes();
        boolean encontrado = false;
        for (Paciente paciente : pacientes) {
            if (paciente.getCedula().equals(cedulaPaciente)) {
                paciente.setSintomas(new ArrayList<>());
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            File archivoOriginal = new File(archivo);
            File archivoTemporal = new File("archivos/pacientes_temp.txt");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoTemporal))) {
                for (Paciente paciente : pacientes) {
                    bw.write(paciente.toString());
                    bw.newLine();
                }
            } catch (IOException ex) {
                System.out.println("Error al escribir el archivo: " + ex.getMessage());
                return false;
            }
            if (archivoOriginal.delete() && archivoTemporal.renameTo(archivoOriginal)) {
                return true;
            } else {
                System.out.println("Error al reemplazar el archivo.");
                return false;
            }
        } else {
            System.out.println("Paciente no encontrado.");
            return false;
        }
    }
    
    public boolean eliminarPaciente(String cedulaPaciente) {
        String archivo = "archivos/pacientes.txt";
        List<Paciente> pacientes = leerPacientes();
        if (pacientes == null || pacientes.isEmpty()) {
            System.out.println("No hay pacientes en el archivo.");
            return false;
        }
        boolean eliminado = pacientes.removeIf(paciente -> paciente.getCedula().equals(cedulaPaciente));
        if (eliminado) {
            File archivoOriginal = new File(archivo);
            File archivoTemporal = new File("archivos/pacientes_temp.txt");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoTemporal))) {
                for (Paciente paciente : pacientes) {
                    bw.write(paciente.toString());
                    bw.newLine();
                }
            } catch (IOException ex) {
                System.out.println("Error al escribir en el archivo temporal: " + ex.getMessage());
                return false;
            }
            if (archivoOriginal.delete() && archivoTemporal.renameTo(archivoOriginal)) {
                System.out.println("Paciente eliminado correctamente.");
                return true;
            } else {
                System.out.println("Error al reemplazar el archivo original.");
                return false;
            }
        } else {
            System.out.println("Paciente no encontrado.");
            return false;
        }
    }
    
    public void guardarPacienteActual(String cedula) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/archivos/paciente_actual.txt", false))) {
            bw.write(cedula);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar paciente actual: " + e.getMessage());
        }
    }

    public String leerPacienteActual() {
        File archivo = new File("archivos/paciente_actual.txt");
        if (!archivo.exists()) {
            System.out.println("Archivo paciente_actual.txt no encontrado.");
            return null;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String cedula = br.readLine();
            return cedula;
        } catch (IOException e) {
            System.out.println("Error al leer paciente actual: " + e.getMessage());
            return null;
        }
    }
    
    public boolean grabarDoctor(Doctor doctor) {
        String archivo = "archivos/doctores.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) { // `true` para no sobrescribir
            bw.write(doctor.toString());
            bw.newLine();
            return true;
        } catch (IOException ex) {
            System.out.println("Error al escribir el archivo: " + ex.getMessage());
            return false;
        }
    }
    
    public Doctor getDoctor(String correo) {
        String archivo = "archivos/doctores.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Doctor doctor = Doctor.fromString(linea);
                if (doctor.getCorreo().equalsIgnoreCase(correo)){
                    return doctor;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error de lectura: " + ex.getMessage());
        }
        return null;
    }
    
    public boolean grabarDoctorSesion(Doctor doctor) {
        String archivo = "archivos/doctor_sesion.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            bw.write(doctor.toString());
            bw.newLine();
            return true;
        } catch (IOException ex) {
            System.out.println("Error al escribir el archivo: " + ex.getMessage());
            return false;
        }
    }
    
    public boolean limpiarDoctorSesion() {
        String archivo = "archivos/doctor_sesion.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            bw.write("");
            bw.newLine();
            return true;
        } catch (IOException ex) {
            System.out.println("Error al escribir el archivo: " + ex.getMessage());
            return false;
        }
    }
    
    public Doctor leerDoctorSesion() {
        String archivo = "archivos/doctor_sesion.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea = br.readLine();
            Doctor doctor = Doctor.fromString(linea);
            return doctor;
        } catch (IOException ex) {
            System.out.println("Error de lectura: " + ex.getMessage());
        }
        return null;
    }
}

