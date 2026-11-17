<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Paciente"%>
<%@page import="modelo.Resumen"%>
<%
    Resumen r = (Resumen) request.getAttribute("resumen");
    if (r == null) r = new Resumen();
    List<Paciente> ultimos = (List<Paciente>) request.getAttribute("ultimos");
    if (ultimos == null) ultimos = java.util.Collections.emptyList();
    String paginaActual = "dashboard";
    String tituloPagina = "Panel general";
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="page-heading">
    <div><span class="eyebrow"><i class="bi bi-shield-check"></i> PANEL DE ADMINISTRACIÓN</span>
        <h1 class="page-title">Hola, <%= Html.esc(sesionUsuario) %></h1>
        <p class="page-sub">Resumen general de la actividad del hospital.</p></div>
    <div class="d-flex gap-2 flex-wrap">
        <a class="btn btn-navy" href="<%= ctx %>/ReporteEstadisticoServlet" target="_blank" rel="noopener"><i class="bi bi-graph-up-arrow me-2"></i>Generar reporte</a>
        <a class="btn btn-primary" href="<%= ctx %>/PacienteServlet?accion=nuevo"><i class="bi bi-plus-lg me-2"></i>Nuevo paciente</a>
    </div>
</div>

<% if (Boolean.TRUE.equals(request.getAttribute("errorBD"))) { %>
<div class="alert alert-danger mb-4"><i class="bi bi-exclamation-triangle me-2"></i>No se pudo consultar la base de datos <b>hospital_2027</b>. Revisa que MySQL esté activo y las credenciales en <code>Conexion.java</code>.</div>
<% } %>

<div class="row g-3 mb-3">
    <div class="col-6 col-xl-3"><div class="panel kpi"><span class="kpi-icon teal"><i class="bi bi-people"></i></span><div><b><%= r.getPacientes() %></b><small>Pacientes</small></div></div></div>
    <div class="col-6 col-xl-3"><div class="panel kpi"><span class="kpi-icon navy"><i class="bi bi-building"></i></span><div><b><%= r.getPabellones() %></b><small>Pabellones</small></div></div></div>
    <div class="col-6 col-xl-3"><div class="panel kpi"><span class="kpi-icon blue"><i class="bi bi-door-open"></i></span><div><b><%= r.getConsultorios() %></b><small>Consultorios</small></div></div></div>
    <div class="col-6 col-xl-3"><div class="panel kpi"><span class="kpi-icon amber"><i class="bi bi-person-badge"></i></span><div><b><%= r.getDoctores() %></b><small>Doctores</small></div></div></div>
</div>

<div class="row g-3 mb-3">
    <div class="col-lg-7"><div class="panel chart-card">
        <div class="chart-head"><h2>Pacientes por pabellón</h2><small>Gráfico de barras</small></div>
        <img class="chart-img" src="<%= ctx %>/GraficoBarrasServlet?por=pabellon" alt="Gráfico de barras de pacientes por pabellón">
    </div></div>
    <div class="col-lg-5"><div class="panel chart-card">
        <div class="chart-head"><h2>Distribución por doctor</h2><small>Gráfico circular</small></div>
        <img class="chart-img" src="<%= ctx %>/GraficoPastelServlet?por=doctor" alt="Gráfico circular de pacientes por doctor">
    </div></div>
</div>

<div class="row g-3">
    <div class="col-lg-5"><div class="panel chart-card">
        <div class="chart-head"><h2>Pacientes por consultorio</h2><small>Gráfico de barras</small></div>
        <img class="chart-img" src="<%= ctx %>/GraficoBarrasServlet?por=consultorio" alt="Gráfico de barras de pacientes por consultorio">
    </div></div>
    <div class="col-lg-7"><div class="panel table-panel h-100">
        <div class="toolbar"><div class="record-count"><span class="count-icon"><i class="bi bi-clock-history"></i></span>
            <div><b>Últimos registros</b><small>Los 5 pacientes más recientes</small></div></div>
            <a class="btn btn-light btn-sm" href="<%= ctx %>/PacienteServlet?accion=listar">Ver todos</a></div>
        <div class="table-responsive">
            <table class="table align-middle">
                <thead><tr><th>Paciente</th><th>Pabellón</th><th>Doctor</th></tr></thead>
                <tbody>
                <% for (Paciente e : ultimos) { %>
                    <tr>
                        <td><div class="patient-cell"><span class="avatar"><%= Html.esc(e.getNombres() == null || e.getNombres().isEmpty() ? "P" : e.getNombres().substring(0, 1).toUpperCase()) %></span>
                            <div><b><%= Html.esc(e.getNombres()) %> <%= Html.esc(e.getApellidos()) %></b><small>#<%= e.getId() %></small></div></div></td>
                        <td><span class="pab"><%= Html.esc(e.getPabellon()) %></span></td>
                        <td><%= Html.esc(e.getDoctor()) %></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
            <% if (ultimos.isEmpty()) { %><div class="empty"><i class="bi bi-inbox"></i><h3>Aún no hay pacientes</h3><p>Registra el primero para ver estadísticas.</p></div><% } %>
        </div>
    </div></div>
</div>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>
