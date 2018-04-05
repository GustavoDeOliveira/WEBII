<%-- 
    Document   : Index
    Created on : 02/03/2018, 19:34:57
    Author     : gustavo
--%>
<%@page import="modelo.Equipe"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.System"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Jogador"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../shared/ini.jsp" %>
<form method="POST" action="./JogadorServlet?acao=excluir">
    <table class="table table-primary table-hover">
        <thead>
            <tr>
                <th colspan="4">
                    <h4 class="text-center text-default">Lista de Jogadores</h4>
                </th>
                <th><a href="./JogadorServlet?acao=editar" class="btn btn-success btn-flat">Novo Jogador</a></th>
            </tr>
            <tr>
                <th width="40">#</th>
                <th>Nome</th>
                <th>Time</th>
                <th>Posição</th>
                <th width="60">Ações</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${jogadores.isEmpty()}">
                    <tr class='warning'>
                        <td colspan='5'>Não há nenhum jogador cadastrado.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${jogadores}" var="jogador">
                        <tr>
                            <td>
                                <input type="checkbox" value="${jogador.id}" name="ids" onclick="checarExcluir()"/>
                            </td>
                            <td>${jogador.nome}</td>
                            <td>
                                <a class="btn btn-xs btn-link btn-flat" href="./TimeServlet?acao=editar&id=${jogador.equipe.id}">
                                    ${jogador.equipe.nome}
                                </a>
                            </td>
                            <td>
                                <a class="btn btn-xs btn-link btn-flat" href="./PosicaoServlet?acao=editar&id=${jogador.posicao.id}">
                                    ${jogador.posicao.descricao}
                                </a>
                            </td>
                            <td>
                                <a class="btn btn-xs btn-warning btn-flat" href="./JogadorServlet?acao=editar&id=${jogador.id}">
                                    Editar
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td>
                            <input class="btn btn-xs btn-danger btn-flat" type="submit" value="X" id="btn_exc" disabled/>
                        </td>
                        <td colspan="4"></td>
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