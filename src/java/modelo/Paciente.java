package modelo;

public class Paciente {

    private int id;
    private String nombres;
    private String apellidos;
    private String pabellon;
    private String consultorio;
    private String doctor;
    private String obs;

    public Paciente() {
    }

    public Paciente(int id, String nom, String apell, String pabell, String consul, String doc, String obs) {
        this.id = id;
        this.nombres = nom;
        this.apellidos = apell;
        this.pabellon = pabell;
        this.consultorio = consul;
        this.doctor = doc;
        this.obs = obs;
        
        

    }

    public Paciente(String nom, String apell, String pabell, String consul, String doc, String obs) {      
        this.nombres = nom;
        this.apellidos = apell;
        this.pabellon = pabell;
        this.consultorio = consul;
        this.doctor = doc;
        this.obs = obs;
        
        

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNombres() {
        return nombres;
    }
    
        public void setNombres(String nombre) {
        this.nombres = nombre;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getPabellon() {
        return pabellon;
    }
    
    
    public void setPabellon(String pabellon) {
        this.pabellon = pabellon;
    }
    
    
    public String getConsultorio() {
        return consultorio;
    }
    
    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }
    
    public String getDoctor() {
        return doctor;
    }
    
    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
    
    public String getObs() {
        return obs;
    }
    
    public void setObs(String obs) {
        this.obs = obs;
    }

}
    
