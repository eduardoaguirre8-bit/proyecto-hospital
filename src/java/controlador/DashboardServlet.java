package controlador;

import dao.PacienteDAO;
import modelo.Resumen;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

    private final PacienteDAO dao = new PacienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("resumen", dao.resumen());
            request.setAttribute("ultimos", dao.ultimos(5));
        } catch (SQLException ex) {
            log("No se pudo cargar el panel", ex);
            request.setAttribute("errorBD", true);
            request.setAttribute("resumen", new Resumen());
            request.setAttribute("ultimos", Collections.emptyList());
        }
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
