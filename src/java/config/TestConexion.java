package config;

import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection con = Conexion.getConexion()) {
            System.out.println("Conexión exitosa a hospital_2027");
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}
