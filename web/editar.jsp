<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Paciente"%>
<%
    Paciente p = (Paciente) request.getAttribute("paciente");
    if (p == null) { response.sendRedirect("PacienteServlet?accion=listar"); return; }
    String paginaActual = "pacientes";
    String tituloPagina = "Editar paciente";
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="page-heading"><div><span class="eyebrow">GESTIÓN DE PACIENTES</span>
    <h1 class="page-title">Editar paciente</h1><p class="page-sub">Actualiza los datos del paciente <b>#<%= p.getId() %></b>.</p></div></div>
<section class="panel form-card">
    <div class="form-card-head"><span class="form-icon"><i class="bi bi-pencil-square"></i></span>
        <div><h2><%= Html.esc(p.getNombres()) %> <%= Html.esc(p.getApellidos()) %></h2><p>Los campos con * son obligatorios.</p></div></div>
    <%
        String accionForm = "actualizar";
        String confirmaTitulo = "¿Guardar los cambios?";
        String confirmaBoton = "Guardar";
        String textoBoton = "Guardar cambios";
    %>
    <%@ include file="/WEB-INF/jspf/form-paciente.jspf" %>
</section>
<a class="back-link" href="<%= ctx %>/PacienteServlet?accion=listar"><i class="bi bi-arrow-left"></i> Volver a pacientes</a>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>
