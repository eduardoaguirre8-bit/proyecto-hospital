package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String usuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");
        usuario = usuario == null ? "" : usuario.trim();
        clave = clave == null ? "" : clave.trim();

        try {
            Usuario u = dao.validar(usuario, clave);
            if (u != null) {
                HttpSession anterior = request.getSession(false);
                if (anterior != null) {
                    anterior.invalidate(); // evita fijación de sesión
                }
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", u.getUsuario());
                session.setAttribute("rol", u.getRol());
                response.sendRedirect(request.getContextPath() + "/DashboardServlet");
                return;
            }
            request.setAttribute("error", "Usuario o clave incorrectos.");
        } catch (SQLException ex) {
            log("Error al validar usuario", ex);
            request.setAttribute("error", "No se pudo conectar con la base de datos. Verifica MySQL y la tabla usuarios.");
        }
        request.setAttribute("usuarioIngresado", usuario);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
