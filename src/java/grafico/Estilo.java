package grafico;

import java.awt.Color;
import java.awt.Font;

/** Colores y tipografías compartidas por los gráficos (misma paleta que la web). */
public final class Estilo {
    private Estilo() { }

    public static final Color NAVY = new Color(0x10, 0x2d, 0x46);
    public static final Color TEAL = new Color(0x0e, 0x8a, 0x8a);
    public static final Color GRID = new Color(0xe4, 0xeb, 0xf0);
    public static final Color TEXTO = new Color(0x42, 0x56, 0x67);

    public static final Color[] PALETA = {
        new Color(0x0e, 0x8a, 0x8a), new Color(0x10, 0x2d, 0x46), new Color(0x3b, 0x82, 0xc4),
        new Color(0x6c, 0xc5, 0xb8), new Color(0xe0, 0xa4, 0x3a), new Color(0xc9, 0x4e, 0x4e),
        new Color(0x8e, 0x7c, 0xc3), new Color(0x7a, 0x8d, 0x9c), new Color(0x4f, 0xb0, 0x6d),
        new Color(0xd9, 0x7b, 0xa6)
    };

    public static final Font TITULO = new Font("SansSerif", Font.BOLD, 17);
    public static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font ETIQUETA = new Font("SansSerif", Font.BOLD, 13);

    /** Texto legible para cada agrupación. */
    public static String nombre(String por) {
        switch (por) {
            case "consultorio": return "consultorio";
            case "doctor": return "doctor";
            default: return "pabell\u00f3n";
        }
    }
}
