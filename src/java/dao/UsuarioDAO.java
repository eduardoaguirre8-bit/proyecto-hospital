package dao;

import config.Conexion;
import modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    /** Devuelve el usuario si las credenciales son correctas; null si no lo son. */
    public Usuario validar(String usuario, String clave) throws SQLException {
        if (usuario == null || clave == null || usuario.isBlank() || clave.isBlank()) {
            return null;
        }
        String sql = "SELECT id, usuario, clave, rol FROM usuarios WHERE usuario = ? AND activo = 1";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("clave");
                    boolean ok;
                    try {
                        ok = BCrypt.checkpw(clave, hash);
                    } catch (IllegalArgumentException e) {
                        ok = false; // hash con formato inválido
                    }
                    if (ok) {
                        Usuario u = new Usuario();
                        u.setId(rs.getInt("id"));
                        u.setUsuario(rs.getString("usuario"));
                        u.setClave(hash);
                        u.setRol(rs.getString("rol"));
                        return u;
                    }
                }
            }
        }
        return null;
    }
}
