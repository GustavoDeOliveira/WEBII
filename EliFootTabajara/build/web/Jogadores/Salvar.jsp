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
    String nome = request.getParameter("nome");
    int id = request.getParameter("id") != null ? Integer.parseInt(request.getParameter("id")) : 0;
    if (nome == null || nome.isEmpty()) {
        response.sendRedirect("./Jogador.jsp" + (id != 0 ? "?id=" + id : ""));
    } else {
        String timeIdGet = request.getParameter("time_id");
        if (timeIdGet == null) {
            response.sendRedirect("./Jogador.jsp" + (id != 0 ? "?id=" + id : ""));
        } else {
            int timeId = Integer.parseInt(timeIdGet);
            if (timeId == 0) {
                response.sendRedirect("./Jogador.jsp" + (id != 0 ? "?id=" + id : ""));
            } else {
                Time time = new TimeDAO().carregar(timeId);

                Jogador jogador = new Jogador(nome, time);
                jogador.setId(id);

                new JogadorDAO().salvar(jogador);

                response.sendRedirect("./Index.jsp");
            }
        }
    }
%>