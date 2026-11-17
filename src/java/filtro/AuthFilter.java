package filtro;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/** Protege todo el sistema: sin sesión iniciada solo se permite el login y los recursos estáticos. */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String ruta = request.getRequestURI().substring(request.getContextPath().length());
        boolean publica = ruta.equals("/login.jsp") || ruta.equals("/LoginServlet") || ruta.equals("/index.jsp")
                || ruta.equals("/") || ruta.startsWith("/css/") || ruta.startsWith("/js/")
                || ruta.startsWith("/img/") || ruta.equals("/favicon.ico");

        HttpSession session = request.getSession(false);
        boolean autenticado = session != null && session.getAttribute("usuario") != null;

        if (publica || autenticado) {
            if (!publica) {
                // Evita que el botón "Atrás" muestre páginas privadas después de cerrar sesión.
                response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
            }
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=sesion");
        }
    }
}
