<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Paciente"%>
<%
    List<Paciente> lista = (List<Paciente>) request.getAttribute("lista");
    if (lista == null) lista = java.util.Collections.emptyList();
    String paginaActual = "pacientes";
    String tituloPagina = "Pacientes";
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="page-heading">
    <div><span class="eyebrow">ADMINISTRACIÓN</span><h1 class="page-title">Pacientes</h1>
        <p class="page-sub">Consulta, registra y administra los pacientes del hospital.</p></div>
    <div class="d-flex gap-2 flex-wrap">
        <button class="btn btn-navy" type="button" data-bs-toggle="modal" data-bs-target="#modalGraficos"><i class="bi bi-bar-chart-line me-2"></i>Ver gráficos</button>
        <a class="btn btn-light" href="<%= ctx %>/ReporteEstadisticoServlet" target="_blank" rel="noopener"><i class="bi bi-graph-up-arrow me-2"></i>Reporte estadístico</a>
        <a class="btn btn-light" href="<%= ctx %>/ReporteServlet" target="_blank" rel="noopener"><i class="bi bi-file-earmark-pdf me-2"></i>Listado PDF</a>
        <a class="btn btn-primary" href="<%= ctx %>/PacienteServlet?accion=nuevo"><i class="bi bi-plus-lg me-2"></i>Nuevo paciente</a>
    </div>
</div>
<% if (Boolean.TRUE.equals(request.getAttribute("errorBD"))) { %>
<div class="alert alert-danger mb-3"><i class="bi bi-exclamation-triangle me-2"></i>No se pudo consultar la base de datos. Verifica MySQL, la base <b>hospital_2027</b>, la tabla <b>pacientes</b> y las credenciales en <code>Conexion.java</code>.</div>
<% } %>
<section class="panel table-panel">
    <div class="toolbar">
        <div class="record-count"><span class="count-icon"><i class="bi bi-people"></i></span>
            <div><b id="totalRegistros"><%= lista.size() %></b><small>Registros encontrados</small></div></div>
        <div class="search"><i class="bi bi-search"></i>
            <input id="buscar" class="form-control" type="search" placeholder="Buscar paciente, doctor, pabellón..." aria-label="Buscar"></div>
    </div>
    <div class="table-responsive">
        <table class="table align-middle" id="tabla">
            <thead><tr><th>ID</th><th>Paciente</th><th>Pabellón</th><th>Consultorio</th><th>Doctor</th><th>Observaciones</th><th class="text-end">Acciones</th></tr></thead>
            <tbody>
            <% for (Paciente e : lista) {
                   String nombreCompleto = Html.esc(e.getNombres()) + " " + Html.esc(e.getApellidos());
                   String ini = (e.getNombres() == null || e.getNombres().isEmpty()) ? "P" : e.getNombres().substring(0, 1).toUpperCase();
            %>
                <tr data-fila>
                    <td><span class="id-chip">#<%= e.getId() %></span></td>
                    <td><div class="patient-cell"><span class="avatar"><%= Html.esc(ini) %></span>
                        <div><b><%= nombreCompleto %></b><small>Paciente registrado</small></div></div></td>
                    <td><span class="pab"><%= Html.esc(e.getPabellon()) %></span></td>
                    <td><%= Html.esc(e.getConsultorio()) %></td>
                    <td><%= Html.esc(e.getDoctor()) %></td>
                    <td><span class="obs" title="<%= Html.esc(e.getObs()) %>"><%= Html.esc(e.getObs()) %></span></td>
                    <td class="text-end text-nowrap">
                        <a class="icon-btn" title="Editar" aria-label="Editar" href="<%= ctx %>/PacienteServlet?accion=editar&id=<%= e.getId() %>"><i class="bi bi-pencil"></i></a>
                        <form class="row-form" method="post" action="<%= ctx %>/PacienteServlet" data-eliminar data-nombre="<%= nombreCompleto %>">
                            <input type="hidden" name="accion" value="eliminar"><input type="hidden" name="id" value="<%= e.getId() %>">
                            <button class="icon-btn del" type="submit" title="Eliminar" aria-label="Eliminar"><i class="bi bi-trash3"></i></button>
                        </form>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
        <div class="empty" id="sinResultados" <%= lista.isEmpty() ? "" : "hidden" %>><i class="bi bi-inbox"></i><h3>No hay pacientes para mostrar</h3><p>Registra un paciente o cambia tu búsqueda.</p></div>
    </div>
    <div class="pager-bar"><small id="infoPagina"></small><div class="pager" id="paginador"></div></div>
</section>
<%@ include file="/WEB-INF/jspf/modal-graficos.jspf" %>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>
