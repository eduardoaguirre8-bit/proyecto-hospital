package util;

public final class Html {
    private Html() { }

    /** Escapa texto para mostrarlo de forma segura dentro de HTML. */
    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
