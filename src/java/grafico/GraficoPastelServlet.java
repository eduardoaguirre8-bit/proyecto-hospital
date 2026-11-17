package grafico;

import dao.PacienteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

/** Gráfico circular: distribución de pacientes por doctor (por defecto), pabellón o consultorio. */
@WebServlet("/GraficoPastelServlet")
public class GraficoPastelServlet extends HttpServlet {

    static {
        System.setProperty("java.awt.headless", "true");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String por = request.getParameter("por");
        if (!"consultorio".equals(por) && !"pabellon".equals(por)) por = "doctor";

        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-store");
        try {
            Map<String, Integer> datos = new PacienteDAO().contarPor(por);
            JFreeChart chart = crear(datos, "Distribución por " + Estilo.nombre(por));
            ChartUtils.writeChartAsPNG(response.getOutputStream(), chart, 900, 480);
        } catch (Exception e) {
            log("Error en gráfico circular", e);
            response.reset();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static JFreeChart crear(Map<String, Integer> datos, String titulo) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> e : datos.entrySet()) {
            dataset.setValue(e.getKey(), e.getValue());
        }
        JFreeChart chart = ChartFactory.createPieChart(titulo, dataset, true, true, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.setAntiAlias(true);
        chart.getTitle().setFont(Estilo.TITULO);
        chart.getTitle().setPaint(Estilo.NAVY);
        chart.getLegend().setItemFont(Estilo.NORMAL);
        chart.getLegend().setFrame(new org.jfree.chart.block.BlockBorder(Color.WHITE));

        @SuppressWarnings("unchecked")
        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setNoDataMessage("Sin datos para mostrar");
        plot.setLabelFont(Estilo.NORMAL);
        plot.setLabelPaint(Estilo.NAVY);
        plot.setLabelBackgroundPaint(Color.WHITE);
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);
        plot.setSectionOutlinesVisible(true);
        plot.setDefaultSectionOutlinePaint(Color.WHITE);
        plot.setSimpleLabels(false);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1} ({2})"));
        int i = 0;
        for (String clave : dataset.getKeys()) {
            plot.setSectionPaint(clave, Estilo.PALETA[i++ % Estilo.PALETA.length]);
        }
        return chart;
    }
}
