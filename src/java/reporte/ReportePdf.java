package reporte;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import modelo.Paciente;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Genera el reporte PDF de pacientes (A4 horizontal) con OpenPDF. */
public class ReportePdf {

    static final Color NAVY = new Color(0x10, 0x2d, 0x46);
    static final Color TEAL = new Color(0x0e, 0x8a, 0x8a);
    static final Color MENTA = new Color(0xe7, 0xf6, 0xf4);
    static final Color ZEBRA = new Color(0xf6, 0xf9, 0xfb);
    static final Color LINEA = new Color(0xe4, 0xeb, 0xf0);
    static final Color GRIS = new Color(0x73, 0x83, 0x92);

    public static void generar(List<Paciente> lista, String usuario, OutputStream salida)
            throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4.rotate(), 36, 36, 36, 46);
        PdfWriter writer = PdfWriter.getInstance(doc, salida);
        BaseFont base = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        BaseFont negrita = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        writer.setPageEvent(new Pie(base));
        doc.addTitle("Reporte de pacientes - Hospital 2027");
        doc.addAuthor("Hospital 2027");
        doc.open();

        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        // Encabezado
        PdfPTable cab = new PdfPTable(new float[]{3f, 1.6f});
        cab.setWidthPercentage(100);
        PdfPCell izq = celda(null, NAVY, 0);
        izq.setPadding(16);
        izq.addElement(new Phrase("HOSPITAL 2027", new Font(negrita, 20, Font.NORMAL, Color.WHITE)));
        izq.addElement(new Phrase("Reporte de pacientes registrados", new Font(base, 11, Font.NORMAL, new Color(0xb8, 0xd8, 0xd8))));
        cab.addCell(izq);
        PdfPCell f1 = celda(null, NAVY, 0);
        f1.setPadding(16);
        Paragraph pEmitido = new Paragraph("Emitido: " + fecha, new Font(base, 9.5f, Font.NORMAL, Color.WHITE));
        pEmitido.setAlignment(Element.ALIGN_RIGHT);
        Paragraph pUsuario = new Paragraph("Por: " + (usuario == null ? "-" : usuario),
                new Font(base, 9.5f, Font.NORMAL, new Color(0xb8, 0xd8, 0xd8)));
        pUsuario.setAlignment(Element.ALIGN_RIGHT);
        f1.addElement(pEmitido);
        f1.addElement(pUsuario);
        cab.addCell(f1);
        doc.add(cab);

        // Resumen
        Set<String> pab = new HashSet<>(), con = new HashSet<>(), docs = new HashSet<>();
        for (Paciente p : lista) {
            if (p.getPabellon() != null && !p.getPabellon().isBlank()) pab.add(p.getPabellon().trim());
            if (p.getConsultorio() != null && !p.getConsultorio().isBlank()) con.add(p.getConsultorio().trim());
            if (p.getDoctor() != null && !p.getDoctor().isBlank()) docs.add(p.getDoctor().trim());
        }
        PdfPTable res = new PdfPTable(4);
        res.setWidthPercentage(100);
        res.setSpacingBefore(14);
        res.setSpacingAfter(14);
        res.addCell(kpi("Pacientes", lista.size(), base, negrita));
        res.addCell(kpi("Pabellones", pab.size(), base, negrita));
        res.addCell(kpi("Consultorios", con.size(), base, negrita));
        res.addCell(kpi("Doctores", docs.size(), base, negrita));
        doc.add(res);

        // Tabla
        PdfPTable t = new PdfPTable(new float[]{0.6f, 2.3f, 1.3f, 1.7f, 1.9f, 2.7f});
        t.setWidthPercentage(100);
        t.setHeaderRows(1);
        String[] cols = {"ID", "PACIENTE", "PABELLÓN", "CONSULTORIO", "DOCTOR", "OBSERVACIONES"};
        for (String c : cols) {
            PdfPCell h = celda(new Phrase(c, new Font(negrita, 8.5f, Font.NORMAL, Color.WHITE)), TEAL, 0);
            h.setPadding(8);
            t.addCell(h);
        }
        Font normal = new Font(base, 9, Font.NORMAL, new Color(0x20, 0x33, 0x44));
        Font fuerte = new Font(negrita, 9, Font.NORMAL, NAVY);
        int i = 0;
        for (Paciente p : lista) {
            Color fondo = (i++ % 2 == 0) ? Color.WHITE : ZEBRA;
            t.addCell(dato("#" + p.getId(), new Font(base, 8.5f, Font.NORMAL, GRIS), fondo));
            t.addCell(dato(nz(p.getNombres()) + " " + nz(p.getApellidos()), fuerte, fondo));
            t.addCell(dato(nz(p.getPabellon()), normal, fondo));
            t.addCell(dato(nz(p.getConsultorio()), normal, fondo));
            t.addCell(dato(nz(p.getDoctor()), normal, fondo));
            t.addCell(dato(nz(p.getObs()), new Font(base, 8.5f, Font.NORMAL, GRIS), fondo));
        }
        if (lista.isEmpty()) {
            PdfPCell vacio = celda(new Phrase("No hay pacientes registrados.", new Font(base, 10, Font.NORMAL, GRIS)), Color.WHITE, 1);
            vacio.setColspan(6);
            vacio.setPadding(20);
            vacio.setHorizontalAlignment(Element.ALIGN_CENTER);
            t.addCell(vacio);
        }
        doc.add(t);
        doc.close();
    }

    static String nz(String s) {
        return s == null ? "" : s;
    }

    static PdfPCell celda(Phrase frase, Color fondo, float borde) {
        PdfPCell c = frase == null ? new PdfPCell() : new PdfPCell(frase);
        c.setBackgroundColor(fondo);
        c.setBorderWidth(borde);
        c.setBorderColor(LINEA);
        return c;
    }

    private static PdfPCell dato(String texto, Font f, Color fondo) {
        PdfPCell c = celda(new Phrase(texto, f), fondo, 0);
        c.setBorder(Rectangle.BOTTOM);
        c.setBorderWidthBottom(0.6f);
        c.setBorderColor(LINEA);
        c.setPadding(7);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return c;
    }

    static PdfPCell kpi(String etiqueta, int valor, BaseFont base, BaseFont negrita) {
        PdfPCell c = celda(null, MENTA, 0);
        c.setBorder(Rectangle.LEFT);
        c.setBorderWidthLeft(3f);
        c.setBorderColorLeft(TEAL);
        c.setPadding(10);
        c.addElement(new Phrase(String.valueOf(valor), new Font(negrita, 18, Font.NORMAL, NAVY)));
        c.addElement(new Phrase(etiqueta, new Font(base, 9, Font.NORMAL, GRIS)));
        return c;
    }

    /** Pie de página: "Hospital 2027 · Página x de y". */
    static class Pie extends PdfPageEventHelper {
        private final BaseFont fuente;
        private PdfTemplate total;

        Pie(BaseFont fuente) {
            this.fuente = fuente;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(30, 12);
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            float y = document.bottom() - 22;
            cb.setColorStroke(LINEA);
            cb.moveTo(document.left(), y + 12);
            cb.lineTo(document.right(), y + 12);
            cb.stroke();
            cb.beginText();
            cb.setFontAndSize(fuente, 8);
            cb.setColorFill(GRIS);
            cb.setTextMatrix(document.left(), y);
            cb.showText("Hospital 2027 · Sistema de gestión de pacientes");
            String texto = "Página " + writer.getPageNumber() + " de ";
            float ancho = fuente.getWidthPoint(texto, 8);
            cb.setTextMatrix(document.right() - ancho - 14, y);
            cb.showText(texto);
            cb.endText();
            cb.addTemplate(total, document.right() - 14, y);
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            total.beginText();
            total.setFontAndSize(fuente, 8);
            total.setColorFill(GRIS);
            total.setTextMatrix(0, 0);
            total.showText(String.valueOf(writer.getPageNumber() - 1));
            total.endText();
        }
    }
}
