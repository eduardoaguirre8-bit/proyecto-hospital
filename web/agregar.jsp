<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Paciente"%>
<%
    String paginaActual = "nuevo";
    String tituloPagina = "Nuevo paciente";
%>
<%@ include file="/WEB-INF/jspf/cabecera.jspf" %>
<div class="page-heading"><div><span class="eyebrow">GESTIÓN DE PACIENTES</span>
    <h1 class="page-title">Nuevo paciente</h1><p class="page-sub">Completa la información para registrar un paciente.</p></div></div>
<section class="panel form-card">
    <div class="form-card-head"><span class="form-icon"><i class="bi bi-person-vcard"></i></span>
        <div><h2>Datos del paciente</h2><p>Los campos con * son obligatorios.</p></div></div>
    <%
        Paciente p = null;
        String accionForm = "agregar";
        String confirmaTitulo = "¿Registrar este paciente?";
        String confirmaBoton = "Registrar";
        String textoBoton = "Guardar paciente";
    %>
    <%@ include file="/WEB-INF/jspf/form-paciente.jspf" %>
</section>
<a class="back-link" href="<%= ctx %>/PacienteServlet?accion=listar"><i class="bi bi-arrow-left"></i> Volver a pacientes</a>
<%@ include file="/WEB-INF/jspf/pie.jspf" %>
