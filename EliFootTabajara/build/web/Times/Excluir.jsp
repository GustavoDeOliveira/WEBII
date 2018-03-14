<%-- 
    Document   : Excluir
    Created on : 03/03/2018, 13:21:57
    Author     : gustavo
--%>

<%@page import="persistencia.TimeDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    String idsGet[] = request.getParameterValues("ids");
    int ids[] = new int[idsGet.length];
    for (int i = 0; i < ids.length; i++) {
        ids[i] = Integer.parseInt(idsGet[i]);
    }
    new TimeDAO().excluir(ids);
    response.sendRedirect("./Index.jsp");
%>