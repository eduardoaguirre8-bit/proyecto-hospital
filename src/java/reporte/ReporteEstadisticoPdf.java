package reporte;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import grafico.GraficoBarrasServlet;
import grafico.GraficoPastelServlet;
import modelo.Resumen;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static reporte.ReportePdf.*;

/** Reporte estadístico: indicadores, resumen de "cómo va" el hospital y los gráficos incrustados. */
public class ReporteEstadisticoPdf {

    public static void generar(Resumen r, Map<String, Integer> porPabellon, Map<String, Integer> porDoctor,
                               Map<String, Integer> porConsultorio, String usuario, OutputStream salida)
            throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4, 40, 40, 36, 50);
        PdfWriter writer = PdfWriter.getInstance(doc, salida);
        BaseFont base = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        BaseFont negrita = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        writer.setPageEvent(new Pie(base));
        doc.addTitle("Reporte estadístico - Hospital 2027");
        doc.addAuthor("Hospital 2027");
        doc.open();

        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        // ---- Encabezado ----
        PdfPTable cab = new PdfPTable(new float[]{3f, 1.6f});
        cab.setWidthPercentage(100);
        PdfPCell izq = celda(null, NAVY, 0);
        izq.setPadding(16);
        izq.addElement(new Phrase("HOSPITAL 2027", new Font(negrita, 20, Font.NORMAL, Color.WHITE)));
        izq.addElement(new Phrase("Reporte estadístico: ¿cómo vamos?", new Font(base, 11, Font.NORMAL, new Color(0xb8, 0xd8, 0xd8))));
        cab.addCell(izq);
        PdfPCell der = celda(null, NAVY, 0);
        der.setPadding(16);
        Paragraph pf = new Paragraph("Emitido: " + fecha, new Font(base, 9.5f, Font.NORMAL, Color.WHITE));
        pf.setAlignment(Element.ALIGN_RIGHT);
        Paragraph pu = new Paragraph("Por: " + (usuario == null ? "-" : usuario),
                new Font(base, 9.5f, Font.NORMAL, new Color(0xb8, 0xd8, 0xd8)));
        pu.setAlignment(Element.ALIGN_RIGHT);
        der.addElement(pf);
        der.addElement(pu);
        cab.addCell(der);
        doc.add(cab);

        // ---- Indicadores ----
        PdfPTable k = new PdfPTable(4);
        k.setWidthPercentage(100);
        k.setSpacingBefore(14);
        k.setSpacingAfter(12);
        k.addCell(kpi("Pacientes", r.getPacientes(), base, negrita));
        k.addCell(kpi("Pabellones", r.getPabellones(), base, negrita));
        k.addCell(kpi("Consultorios", r.getConsultorios(), base, negrita));
        k.addCell(kpi("Doctores", r.getDoctores(), base, negrita));
        doc.add(k);

        // ---- Resumen: cómo van ----
        doc.add(titulo("Resumen de situación", negrita));
        Font txt = new Font(base, 10, Font.NORMAL, new Color(0x20, 0x33, 0x44));
        Font fuerte = new Font(negrita, 10, Font.NORMAL, NAVY);
        if (r.getPacientes() == 0) {
            doc.add(new Paragraph("Todavía no hay pacientes registrados, por lo que no hay estadísticas que mostrar.", txt));
        } else {
            doc.add(linea(new Phrase("Total: ", fuerte), new Phrase(r.getPacientes() + " pacientes atendidos en "
                    + r.getPabellones() + " pabellones, " + r.getConsultorios() + " consultorios y "
                    + r.getDoctores() + " doctores.", txt)));
            if (r.getDoctores() > 0) {
                double prom = (double) r.getPacientes() / r.getDoctores();
                doc.add(linea(new Phrase("Carga promedio: ", fuerte),
                        new Phrase(String.format(java.util.Locale.US, "%.1f", prom) + " pacientes por doctor.", txt)));
            }
            destacado(doc, "Pabellón con más pacientes: ", porPabellon, r.getPacientes(), fuerte, txt);
            destacado(doc, "Doctor con mayor carga: ", porDoctor, r.getPacientes(), fuerte, txt);
            destacado(doc, "Consultorio más utilizado: ", porConsultorio, r.getPacientes(), fuerte, txt);
        }

        // ---- Gráficos ----
        doc.add(bloque("Pacientes por pabellón", imagen(GraficoBarrasServlet.crear(porPabellon, "Pacientes por pabellón")), negrita));
        doc.add(bloque("Distribución por doctor", imagen(GraficoPastelServlet.crear(porDoctor, "Distribución por doctor")), negrita));
        doc.add(bloque("Pacientes por consultorio", imagen(GraficoBarrasServlet.crear(porConsultorio, "Pacientes por consultorio")), negrita));

        // ---- Tablas de detalle ----
        doc.add(titulo("Detalle numérico", negrita));
        doc.add(tabla("Por pabellón", porPabellon, r.getPacientes(), base, negrita));
        doc.add(tabla("Por doctor", porDoctor, r.getPacientes(), base, negrita));
        doc.add(tabla("Por consultorio", porConsultorio, r.getPacientes(), base, negrita));
        doc.close();
    }

    private static Paragraph titulo(String texto, BaseFont negrita) {
        Paragraph p = new Paragraph(texto.toUpperCase(), new Font(negrita, 9, Font.NORMAL, TEAL));
        p.setSpacingBefore(8);
        p.setSpacingAfter(6);
        return p;
    }

    private static Paragraph linea(Phrase a, Phrase b) {
        Paragraph p = new Paragraph();
        p.add(a);
        p.add(b);
        p.setSpacingAfter(4);
        return p;
    }

    private static void destacado(Document doc, String etiqueta, Map<String, Integer> datos, int total,
                                  Font fuerte, Font txt) throws DocumentException {
        if (datos.isEmpty()) return;
        Map.Entry<String, Integer> top = datos.entrySet().iterator().next();
        int pct = total == 0 ? 0 : Math.round(top.getValue() * 100f / total);
        doc.add(linea(new Phrase(etiqueta, fuerte),
                new Phrase(top.getKey() + " (" + top.getValue() + " pacientes, " + pct + "%).", txt)));
    }

    private static Image imagen(JFreeChart chart) throws IOException, DocumentException {
        chart.setTitle((org.jfree.chart.title.TextTitle) null); // el título ya va en la tarjeta
        byte[] png = ChartUtils.encodeAsPNG(chart.createBufferedImage(900, 440));
        Image img = Image.getInstance(png);
        img.scaleToFit(470, 250);
        img.setAlignment(Element.ALIGN_CENTER);
        return img;
    }

    /** Tarjeta con título e imagen, que no se parte entre páginas. */
    private static PdfPTable bloque(String titulo, Image img, BaseFont negrita) {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.setSpacingBefore(12);
        t.setKeepTogether(true);
        PdfPCell c = celda(null, Color.WHITE, 0.8f);
        c.setPadding(12);
        Paragraph p = new Paragraph(titulo, new Font(negrita, 11, Font.NORMAL, NAVY));
        p.setSpacingAfter(6);
        c.addElement(p);
        c.addElement(img);
        t.addCell(c);
        return t;
    }

    private static PdfPTable tabla(String nombre, Map<String, Integer> datos, int total, BaseFont base, BaseFont negrita) {
        PdfPTable t = new PdfPTable(new float[]{3.2f, 1f, 1f});
        t.setWidthPercentage(100);
        t.setSpacingBefore(8);
        t.setKeepTogether(true);
        String[] cols = {nombre.toUpperCase(), "PACIENTES", "%"};
        for (int i = 0; i < cols.length; i++) {
            PdfPCell h = celda(new Phrase(cols[i], new Font(negrita, 8.5f, Font.NORMAL, Color.WHITE)), TEAL, 0);
            h.setPadding(7);
            if (i > 0) h.setHorizontalAlignment(Element.ALIGN_RIGHT);
            t.addCell(h);
        }
        Font normal = new Font(base, 9, Font.NORMAL, new Color(0x20, 0x33, 0x44));
        int i = 0;
        for (Map.Entry<String, Integer> e : datos.entrySet()) {
            Color fondo = (i++ % 2 == 0) ? Color.WHITE : ZEBRA;
            int pct = total == 0 ? 0 : Math.round(e.getValue() * 100f / total);
            t.addCell(fila(e.getKey(), normal, fondo, Element.ALIGN_LEFT));
            t.addCell(fila(String.valueOf(e.getValue()), normal, fondo, Element.ALIGN_RIGHT));
            t.addCell(fila(pct + "%", normal, fondo, Element.ALIGN_RIGHT));
        }
        return t;
    }

    private static PdfPCell fila(String texto, Font f, Color fondo, int alineacion) {
        PdfPCell c = celda(new Phrase(texto, f), fondo, 0);
        c.setBorder(Rectangle.BOTTOM);
        c.setBorderWidthBottom(0.6f);
        c.setBorderColor(LINEA);
        c.setPadding(6);
        c.setHorizontalAlignment(alineacion);
        return c;
    }
}
