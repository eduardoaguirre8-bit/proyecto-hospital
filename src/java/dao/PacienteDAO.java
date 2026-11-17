package dao;

import config.Conexion;
import modelo.Paciente;
import modelo.Resumen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Acceso a la tabla pacientes (ver sql/01_hospital_2027_mejorada.sql):
 * ID_paciente, nombres, apellidos, pabellon, consultorio, doctor, obs, fecha_registro
 */
public class PacienteDAO {

    private static final String TABLA = "pacientes";
    private static final String PAB = "pabellon";
    private static final String COLUMNAS = "ID_paciente, nombres, apellidos, " + PAB + ", consultorio, doctor, obs";

    private Paciente mapear(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt(1));
        p.setNombres(rs.getString(2));
        p.setApellidos(rs.getString(3));
        p.setPabellon(rs.getString(4));
        p.setConsultorio(rs.getString(5));
        p.setDoctor(rs.getString(6));
        p.setObs(rs.getString(7));
        return p;
    }

    public List<Paciente> listar() throws SQLException {
        return consultar("SELECT " + COLUMNAS + " FROM " + TABLA + " ORDER BY ID_paciente DESC");
    }

    public List<Paciente> ultimos(int cantidad) throws SQLException {
        return consultar("SELECT " + COLUMNAS + " FROM " + TABLA
                + " ORDER BY ID_paciente DESC LIMIT " + Math.max(1, cantidad));
    }

    private List<Paciente> consultar(String sql) throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public boolean agregar(Paciente p) {
        String sql = "INSERT INTO " + TABLA + " (nombres, apellidos, " + PAB
                + ", consultorio, doctor, obs) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getPabellon());
            ps.setString(4, p.getConsultorio());
            ps.setString(5, p.getDoctor());
            ps.setString(6, p.getObs());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al agregar: " + ex.getMessage());
            return false;
        }
    }

    public Paciente buscarPorId(int id) {
        String sql = "SELECT " + COLUMNAS + " FROM " + TABLA + " WHERE ID_paciente = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar: " + ex.getMessage());
        }
        return null;
    }

    public boolean actualizar(Paciente p) {
        String sql = "UPDATE " + TABLA + " SET nombres = ?, apellidos = ?, " + PAB
                + " = ?, consultorio = ?, doctor = ?, obs = ? WHERE ID_paciente = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getPabellon());
            ps.setString(4, p.getConsultorio());
            ps.setString(5, p.getDoctor());
            ps.setString(6, p.getObs());
            ps.setInt(7, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al actualizar: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE ID_paciente = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al eliminar: " + ex.getMessage());
            return false;
        }
    }

    // ------------------- ESTADÍSTICAS (panel y gráficos) -------------------

    public Resumen resumen() throws SQLException {
        String sql = "SELECT COUNT(*), COUNT(DISTINCT NULLIF(TRIM(" + PAB + "),'')), "
                + "COUNT(DISTINCT NULLIF(TRIM(consultorio),'')), COUNT(DISTINCT NULLIF(TRIM(doctor),'')) FROM " + TABLA;
        Resumen r = new Resumen();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                r.setPacientes(rs.getInt(1));
                r.setPabellones(rs.getInt(2));
                r.setConsultorios(rs.getInt(3));
                r.setDoctores(rs.getInt(4));
            }
        }
        return r;
    }

    /** Cantidad de pacientes agrupados por: "pabellon", "consultorio" o "doctor". */
    public Map<String, Integer> contarPor(String campo) throws SQLException {
        String columna;
        switch (campo) {
            case "pabellon": columna = PAB; break;
            case "consultorio": columna = "consultorio"; break;
            case "doctor": columna = "doctor"; break;
            default: throw new IllegalArgumentException("Campo no permitido: " + campo);
        }
        String sql = "SELECT COALESCE(NULLIF(TRIM(" + columna + "),''),'Sin asignar') AS etiqueta, COUNT(*) AS cantidad "
                + "FROM " + TABLA + " GROUP BY etiqueta ORDER BY cantidad DESC, etiqueta LIMIT 10";
        Map<String, Integer> datos = new LinkedHashMap<>();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                datos.put(rs.getString(1), rs.getInt(2));
            }
        }
        return datos;
    }
}
