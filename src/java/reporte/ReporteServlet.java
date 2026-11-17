package reporte;

import dao.PacienteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Se arma primero en memoria: si algo falla no se envía un PDF corrupto.
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            Object usuario = request.getSession().getAttribute("usuario");
            ReportePdf.generar(new PacienteDAO().listar(), usuario == null ? null : usuario.toString(), buffer);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"reporte_pacientes.pdf\"");
            response.setContentLength(buffer.size());
            buffer.writeTo(response.getOutputStream());
        } catch (Exception e) {
            log("Error al generar el reporte PDF", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No se pudo generar el reporte. Verifica la conexión con la base de datos.");
        }
    }
}
