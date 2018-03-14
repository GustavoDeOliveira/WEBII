<%-- 
    Document   : Salvar
    Created on : 03/03/2018, 13:03:22
    Author     : gustavo
--%>

<%@page import="persistencia.JogadorDAO"%>
<%@page import="modelo.Jogador"%>
<%@page import="persistencia.TimeDAO"%>
<%@page import="modelo.Time"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    String id = request.getParameter("id");
    Time time;
    if (id != null) {
        time = new TimeDAO().carregar(Integer.parseInt(id));
    } else {
        time = new Time("");
    }
    String nome = request.getParameter("nome");
    if (nome == null || nome.isEmpty()) {
        response.sendRedirect("./Time.jsp" + (time.getId() != 0 ? "?id=" + time.getId() : ""));
    } else {
        time.setNome(nome);

        new TimeDAO().salvar(time);

        response.sendRedirect("./Index.jsp");
    }
%>