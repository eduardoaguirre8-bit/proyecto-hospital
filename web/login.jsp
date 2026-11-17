<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.Html"%>
<%
    String ctx = request.getContextPath();
    if (session.getAttribute("usuario") != null) { response.sendRedirect(ctx + "/DashboardServlet"); return; }
    String error = (String) request.getAttribute("error");
    String usuarioIngresado = (String) request.getAttribute("usuarioIngresado");
    String msg = request.getParameter("msg");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Iniciar sesión | Hospital 2027</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="<%= ctx %>/css/estilo.css" rel="stylesheet">
</head>
<body>
<div class="login-wrap">
    <section class="login-art">
        <div class="brand"><i class="bi bi-heart-pulse-fill"></i><div>Hospital<span>2027</span></div></div>
        <div class="login-copy">
            <h1>La atención comienza con una buena <span>gestión.</span></h1>
            <p>Administra pacientes, consultorios y doctores desde un solo lugar, con información ordenada y segura.</p>
            <div class="login-points">
                <div><i class="bi bi-check-circle-fill"></i>Registro y control de pacientes</div>
                <div><i class="bi bi-check-circle-fill"></i>Estadísticas y gráficos en tiempo real</div>
                <div><i class="bi bi-check-circle-fill"></i>Reportes en PDF con un clic</div>
            </div>
        </div>
        <small>© Hospital 2027 · Sistema de gestión de pacientes</small>
    </section>
    <section class="login-form-side">
        <div class="panel login-card">
            <span class="eyebrow"><i class="bi bi-shield-lock"></i> ACCESO SEGURO</span>
            <h2>Iniciar sesión</h2>
            <p class="sub">Ingresa tus credenciales para continuar.</p>
            <% if (error != null) { %><div class="alert alert-danger py-2"><i class="bi bi-exclamation-circle me-2"></i><%= Html.esc(error) %></div><% } %>
            <% if ("sesion".equals(msg)) { %><div class="alert alert-warning py-2"><i class="bi bi-lock me-2"></i>Inicia sesión para continuar.</div><% } %>
            <% if ("salio".equals(msg)) { %><div class="alert alert-success py-2"><i class="bi bi-check-circle me-2"></i>Cerraste sesión correctamente.</div><% } %>
            <form action="<%= ctx %>/LoginServlet" method="post" onsubmit="return validarLogin()" novalidate>
                <div class="mb-3">
                    <label class="form-label" for="usuario">Usuario</label>
                    <div class="input-icon"><i class="bi bi-person ico"></i>
                        <input type="text" class="form-control" id="usuario" name="usuario" autocomplete="username" autofocus
                               value="<%= Html.esc(usuarioIngresado) %>" placeholder="Tu usuario"></div>
                </div>
                <div class="mb-4">
                    <label class="form-label" for="clave">Clave</label>
                    <div class="input-icon"><i class="bi bi-key ico"></i>
                        <input type="password" class="form-control" id="clave" name="clave" autocomplete="current-password" placeholder="Tu clave">
                        <button type="button" class="toggle-pass" id="verClave" aria-label="Mostrar u ocultar clave"><i class="bi bi-eye"></i></button></div>
                </div>
                <button type="submit" class="btn btn-primary w-100 py-3"><i class="bi bi-box-arrow-in-right me-2"></i>Ingresar</button>
            </form>
            <div class="login-hint">Acceso solo para personal autorizado.</div>
        </div>
    </section>
</div>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="<%= ctx %>/js/validaciones.js"></script>
</body>
</html>
