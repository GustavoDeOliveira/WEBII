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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../shared/ini.jsp" %>
<form method="POST" action="./TimeServlet?acao=excluir">
    <table class="table table-hover table-primary">
        <thead>
            <tr>
                <th colspan="2">
                    <h4 class="text-center text-default">Lista de Times</h4>
                </th>
                <th><a href="./TimeServlet?acao=editar" class="btn btn-success btn-block">Novo Time</a></th>
            </tr>
            <tr>
                <th width="20">#</th>
                <th>Nome</th>
                <th width="40">Ações</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${times.isEmpty()}">
                    <tr class="warning">
                        <td colspan="3">Não há nenhum time cadastrado.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${times}" var="time">
                        <tr>
                            <td>
                                <input type="checkbox" value="${time.id}" name="ids" onclick="checarExcluir()" <c:if test="${!time.jogadores.isEmpty()}">disabled</c:if>/>
                            </td>
                            <td>${time.nome}</td>
                            <td>
                                <a class="btn btn-xs btn-warning btn-flat" href="./TimeServlet?acao=editar&id=${time.id}">
                                    Editar
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td>
                            <input class="btn btn-xs btn-danger btn-flat" type="submit" value="X" id="btn_exc" disabled/>
                        </td>
                        <td colspan="2"></td>
                    </tr>
                </c:otherwise>
            </c:choose>
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