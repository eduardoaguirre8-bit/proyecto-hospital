package controlador;

import dao.PacienteDAO;
import modelo.Paciente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/PacienteServlet")
public class PacienteServlet extends HttpServlet {

    private final PacienteDAO dao = new PacienteDAO();

    // Longitudes máximas según la tabla pacientes
    private static final int MAX_NOMBRES = 30, MAX_APELLIDOS = 30, MAX_PABELLON = 30,
            MAX_CONSULTORIO = 100, MAX_DOCTOR = 50, MAX_OBS = 100;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null || accion.isBlank()) accion = "listar";

        switch (accion) {
            case "listar":
                try {
                    request.setAttribute("lista", dao.listar());
                } catch (java.sql.SQLException ex) {
                    log("No se pudo consultar la tabla pacientes", ex);
                    request.setAttribute("errorBD", true);
                    request.setAttribute("lista", java.util.Collections.emptyList());
                }
                request.getRequestDispatcher("/listar.jsp").forward(request, response);
                break;
            case "nuevo":
                request.getRequestDispatcher("/agregar.jsp").forward(request, response);
                break;
            case "editar":
                Integer id = leerId(request);
                Paciente paciente = id == null ? null : dao.buscarPorId(id);
                if (paciente == null) {
                    response.sendRedirect("PacienteServlet?accion=listar&msg=error");
                    return;
                }
                request.setAttribute("paciente", paciente);
                request.getRequestDispatcher("/editar.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("PacienteServlet?accion=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        // La eliminación se hace por POST (más seguro que un enlace GET).
        if ("eliminar".equals(accion)) {
            Integer idEliminar = leerId(request);
            boolean eliminado = idEliminar != null && dao.eliminar(idEliminar);
            response.sendRedirect("PacienteServlet?accion=listar&msg=" + (eliminado ? "eliminado" : "error"));
            return;
        }

        String nombres = limpiar(request.getParameter("nombres"));
        String apellidos = limpiar(request.getParameter("apellidos"));
        String pabellon = limpiar(request.getParameter("pabellon"));
        String consultorio = limpiar(request.getParameter("consultorio"));
        String doctor = limpiar(request.getParameter("doctor"));
        String obs = limpiar(request.getParameter("obs"));

        boolean invalido = nombres.isBlank() || apellidos.isBlank() || pabellon.isBlank()
                || consultorio.isBlank() || doctor.isBlank()
                || nombres.length() > MAX_NOMBRES || apellidos.length() > MAX_APELLIDOS
                || pabellon.length() > MAX_PABELLON || consultorio.length() > MAX_CONSULTORIO
                || doctor.length() > MAX_DOCTOR || obs.length() > MAX_OBS;
        if (invalido) {
            response.sendRedirect("PacienteServlet?accion="
                    + ("actualizar".equals(accion) ? "editar&id=" + request.getParameter("id") : "nuevo")
                    + "&msg=invalido");
            return;
        }

        if ("agregar".equals(accion)) {
            boolean ok = dao.agregar(new Paciente(nombres, apellidos, pabellon, consultorio, doctor, obs));
            response.sendRedirect("PacienteServlet?accion=listar&msg=" + (ok ? "creado" : "error"));
        } else if ("actualizar".equals(accion)) {
            Integer id = leerId(request);
            if (id == null) {
                response.sendRedirect("PacienteServlet?accion=listar&msg=error");
                return;
            }
            boolean ok = dao.actualizar(new Paciente(id, nombres, apellidos, pabellon, consultorio, doctor, obs));
            response.sendRedirect("PacienteServlet?accion=listar&msg=" + (ok ? "actualizado" : "error"));
        } else {
            response.sendRedirect("PacienteServlet?accion=listar");
        }
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private Integer leerId(HttpServletRequest request) {
        try {
            return Integer.valueOf(request.getParameter("id"));
        } catch (Exception e) {
            return null;
        }
    }
}
