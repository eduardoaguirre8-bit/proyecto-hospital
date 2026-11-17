package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Conexión a MySQL (base hospital_2027). Ajusta USER y PASSWORD si es necesario. */
public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3307/hospital_2027"
            + "?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    // XAMPP/WAMP suelen traer root SIN clave. Si tu MySQL tiene clave, escríbela aquí.
    private static final String PASSWORD = "";

    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró MySQL Connector/J en web/WEB-INF/lib.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
