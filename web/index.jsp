<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("usuario") != null) {
        response.sendRedirect(request.getContextPath() + "/DashboardServlet");
    } else {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
%>
