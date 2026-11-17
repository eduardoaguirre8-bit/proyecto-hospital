package modelo;

/** Indicadores para el panel principal. */
public class Resumen {
    private int pacientes;
    private int pabellones;
    private int consultorios;
    private int doctores;

    public int getPacientes() { return pacientes; }
    public void setPacientes(int pacientes) { this.pacientes = pacientes; }
    public int getPabellones() { return pabellones; }
    public void setPabellones(int pabellones) { this.pabellones = pabellones; }
    public int getConsultorios() { return consultorios; }
    public void setConsultorios(int consultorios) { this.consultorios = consultorios; }
    public int getDoctores() { return doctores; }
    public void setDoctores(int doctores) { this.doctores = doctores; }
}
