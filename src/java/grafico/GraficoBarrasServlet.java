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
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

/** Gráfico de barras: pacientes por pabellón (por defecto), consultorio o doctor. Uso: GraficoBarrasServlet?por=doctor */
@WebServlet("/GraficoBarrasServlet")
public class GraficoBarrasServlet extends HttpServlet {

    static {
        System.setProperty("java.awt.headless", "true");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String por = request.getParameter("por");
        if (!"consultorio".equals(por) && !"doctor".equals(por)) por = "pabellon";

        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-store");
        try {
            Map<String, Integer> datos = new PacienteDAO().contarPor(por);
            JFreeChart chart = crear(datos, "Pacientes por " + Estilo.nombre(por));
            ChartUtils.writeChartAsPNG(response.getOutputStream(), chart, 900, 480);
        } catch (Exception e) {
            log("Error en gráfico de barras", e);
            response.reset();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /** Construye el gráfico (separado del servlet para poder probarlo sin servidor). */
    public static JFreeChart crear(Map<String, Integer> datos, String titulo) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> e : datos.entrySet()) {
            dataset.addValue(e.getValue(), "Pacientes", e.getKey());
        }
        JFreeChart chart = ChartFactory.createBarChart(titulo, null, "Pacientes", dataset,
                PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.setAntiAlias(true);
        chart.getTitle().setFont(Estilo.TITULO);
        chart.getTitle().setPaint(Estilo.NAVY);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(Estilo.GRID);
        plot.setNoDataMessage("Sin datos para mostrar");
        plot.getDomainAxis().setTickLabelFont(Estilo.NORMAL);
        plot.getDomainAxis().setTickLabelPaint(Estilo.TEXTO);
        plot.getDomainAxis().setAxisLineVisible(false);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        NumberAxis eje = (NumberAxis) plot.getRangeAxis();
        eje.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        eje.setTickLabelFont(Estilo.NORMAL);
        eje.setTickLabelPaint(Estilo.TEXTO);
        eje.setLabelFont(Estilo.NORMAL);
        eje.setLabelPaint(Estilo.TEXTO);
        eje.setAxisLineVisible(false);

        BarRenderer r = (BarRenderer) plot.getRenderer();
        r.setBarPainter(new StandardBarPainter());
        r.setShadowVisible(false);
        r.setDrawBarOutline(false);
        r.setMaximumBarWidth(0.10);
        r.setSeriesPaint(0, Estilo.TEAL);
        r.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        r.setDefaultItemLabelsVisible(true);
        r.setDefaultItemLabelFont(Estilo.ETIQUETA);
        r.setDefaultItemLabelPaint(Estilo.NAVY);
        return chart;
    }
}
