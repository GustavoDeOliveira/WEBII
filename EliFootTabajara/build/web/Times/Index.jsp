<%-- 
    Document   : Index
    Created on : 02/03/2018, 19:34:57
    Author     : gustavo
--%>
<%@page import="modelo.Time"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.System"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Time"%>
<%@page import="persistencia.TimeDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../shared/ini.jsp" %>
<form method="POST" action="./Excluir.jsp">
    <table class="table table-hover table-primary">
        <thead>
            <tr>
                <th colspan="2">
                    <h4 class="text-center text-default">Lista de Times</h4>
                </th>
                <th><a href="./Time.jsp" class="btn btn-success btn-block">Novo Time</a></th>
            </tr>
            <tr>
                <th width="20">#</th>
                <th>Nome</th>
                <th width="40">Ações</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Time> times = new TimeDAO().listar();
//                            Time timeA = new Time("Tabajara FC");
//                            timeA.setId(1);
//                            Time timeB = new Time("Mergulhões FC");
//                            timeB.setId(2);
//                            Time timeC = new Time("Papareira FC");
//                            timeC.setId(3);
//                            List<Time> times = new ArrayList();
//                            times.add(timeA);
//                            times.add(timeB);
//                            times.add(timeC);
                StringBuilder sb = new StringBuilder();
                if (times.isEmpty()) {
                    sb.append("<tr class='warning text-muted'><td colspan='3'>Não há nenhum time cadastrado.</td></tr>");
                } else {
                    for (int i = 0; i < times.size(); i++) {
                        Time t = times.get(i);
                        sb.append("<tr>");
                        sb.append("<td>");
                        sb.append("<input type='checkbox' value='");
                        sb.append(t.getId());
                        sb.append("'name='ids' onclick='checarExcluir()' ");
                        if (!t.getJogadores().isEmpty()) {
                            sb.append("disabled");
                        }
                        sb.append("/>");
                        sb.append("</td>");
                        sb.append("<td>");
                        sb.append(t.getNome());
                        sb.append("</td>");
                        sb.append("<td class='btn-group btn-group-xs'>");
                        sb.append("<a class='btn btn-warning btn-flat' href='./Time.jsp?id=").append(t.getId()).append("'>Editar</a>");
                        sb.append("</td>");
                        sb.append("</tr>");
                    }
                    sb.append("<tr><td><input class='btn btn-danger btn-xs' type='submit' value='X' disabled='disabled' id='btn_exc'/></td><td colspan='3'></td></tr>");
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
        button.disabled = "disabled";
    }
</script>
<%@include file="../shared/fim.jsp" %>