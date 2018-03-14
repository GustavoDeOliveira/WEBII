<%-- 
    Document   : Index
    Created on : 02/03/2018, 19:34:57
    Author     : gustavo
--%>
<%@page import="modelo.Time"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.System"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Jogador"%>
<%@page import="persistencia.JogadorDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../shared/ini.jsp" %>
<form method="POST" action="./Excluir.jsp">
    <table class="table table-primary table-hover">
        <thead>
            <tr>
                <th colspan="3">
                    <h4 class="text-center text-default">Lista de Jogadores</h4>
                </th>
                <th><a href="./Jogador.jsp" class="btn btn-success btn-flat">Novo Jogador</a></th>
            </tr>
            <tr>
                <th width="40">#</th>
                <th>Nome</th>
                <th>Time</th>
                <th width="60">Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                JogadorDAO jdao = new JogadorDAO();
                List<Jogador> jogadores = jdao.listar();
                StringBuilder sb = new StringBuilder();
                if (jogadores.isEmpty()) {
                    sb.append("<tr class='warning'><td colspan='4'>Não há nenhum jogador cadastrado.</td></tr>");
                } else {
                    for (int i = 0; i < jogadores.size(); i++) {
                        Jogador j = jogadores.get(i);
                        sb.append("<tr>");
                        sb.append("<td>");
                        sb.append("<input type='checkbox' value='").append(j.getId()).append("'name='ids' onclick='checarExcluir()'/>");
                        sb.append("</td>");
                        sb.append("<td>");
                        sb.append(j.getNome());
                        sb.append("</td>");
                        sb.append("<td>");
                        sb.append("<a class='btn btn-xs btn-link btn-flat' href='../Times/Time.jsp?id=").append(j.getTime().getId()).append("'>");
                        sb.append(j.getTime().getNome());
                        sb.append("</a>");
                        sb.append("</td>");
                        sb.append("<td>");
                        sb.append("<a class='btn btn-xs btn-warning btn-flat' href='./Jogador.jsp?id=").append(j.getId()).append("'>Editar</a>");
                        sb.append("</td>");
                        sb.append("</tr>");
                    }
                    sb.append("<tr><td><input class='btn btn-xs btn-danger btn-flat' type='submit' value='X' id='btn_exc' disabled/></td><td colspan='3'></td></tr>");
                }
                out.println(sb);
            %>
        </tbody>
    </table>
</form>
<script type="text/javascript">
    function checarExcluir() {
        var checkboxes = document.getElementsByTagName("input");
        var button = document.getElementById("btn_exc");
        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].name === "ids" && checkboxes[i].checked) {
                button.disabled = undefined;
                return;
            }
        }
        button.disabled = "true";
    }
</script>
<%@include file="../shared/fim.jsp" %>