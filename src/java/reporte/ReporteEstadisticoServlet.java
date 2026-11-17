package reporte;

import dao.PacienteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/** Reporte PDF con indicadores, resumen y gráficos ("cómo va" el hospital). */
@WebServlet("/ReporteEstadisticoServlet")
public class ReporteEstadisticoServlet extends HttpServlet {

    static {
        System.setProperty("java.awt.headless", "true");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PacienteDAO dao = new PacienteDAO();
            Object usuario = request.getSession().getAttribute("usuario");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ReporteEstadisticoPdf.generar(dao.resumen(), dao.contarPor("pabellon"), dao.contarPor("doctor"),
                    dao.contarPor("consultorio"), usuario == null ? null : usuario.toString(), buffer);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"reporte_estadistico.pdf\"");
            response.setContentLength(buffer.size());
            buffer.writeTo(response.getOutputStream());
        } catch (Exception e) {
            log("Error al generar el reporte estadístico", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No se pudo generar el reporte. Verifica la conexión con la base de datos.");
        }
    }
}
