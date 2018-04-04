<%-- 
    Document   : Index
    Created on : 02/03/2018, 19:34:57
    Author     : gustavo
--%>
<%@page import="modelo.Equipe"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.System"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Tecnico"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../shared/ini.jsp" %>
<form method="POST" action="./TecnicoServlet?acao=excluir">
    <table class="table table-primary table-hover">
        <thead>
            <tr>
                <th colspan="2">
                    <h4 class="text-center text-default">Lista de Técnicos</h4>
                </th>
                <th><a href="./TecnicoServlet?acao=editar" class="btn btn-success btn-flat">Novo Técnico</a></th>
            </tr>
            <tr>
                <th width="40">#</th>
                <th>Nome</th>
                <th width="60">Ações</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${tecnicos.isEmpty()}">
                    <tr class='warning'>
                        <td colspan='3'>Não há nenhum tecnico cadastrado.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${tecnicos}" var="tecnico">
                        <tr>
                            <td>
                                <input type="checkbox" value="${tecnico.id}" name="ids" onclick="checarExcluir()"/>
                            </td>
                            <td>${tecnico.nome}</td>
                            <td>
                                <a class="btn btn-xs btn-warning btn-flat" href="./TecnicoServlet?acao=editar&id=${tecnico.id}">
                                    Editar
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td>
                            <input class="btn btn-xs btn-danger btn-flat" type="submit" value="X" id="btn_exc" disabled/>
                        </td>
                        <td colspan="3"></td>
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
        button.disabled = "true";
    }
</script>
<%@include file="../shared/fim.jsp" %>