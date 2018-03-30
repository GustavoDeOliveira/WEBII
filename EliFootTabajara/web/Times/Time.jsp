<%-- 
    Document   : Jogador
    Created on : 03/03/2018, 12:14:18
    Author     : gustavo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="persistencia.TimeDAO"%>
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
<h2 class='panel-title text-center text-primary alert-primary'>
    <c:choose>
        <c:when test="${time.id != 0}">
            Editando Time
        </c:when>
        <c:otherwise>
            Criando Novo Time
        </c:otherwise>
    </c:choose>
</h2>
<form method="POST" action="./TimeServlet?acao=salvar">
    <div class="form-group">
        <label for="nome">Nome</label>
        <input class="form-control" type="text" name="nome" id="nome" value="${time.nome}" onblur="validar();" autofocus required/>
    </div>
        <c:if test="${!time.jogadores.isEmpty()}">
            <div class="form-group">
                <label>Jogadores</label>
                <ul>
                    <c:forEach items="${time.jogadores}" var="jogador">
                        <li>
                            <a class="btn btn-xs btn-link" href="./JogadorServlet?acao=editar&id=${jogador.id}">
                                ${jogador.nome}
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
    <div class="form-group">
        <input type="hidden" name="id" id="id" value="${time.id}"/>
        <input id="btn_salvar" disabled type="submit" value="Salvar" class="btn btn-success btn-lg col-md-auto offset-md-10 offset-sm-8 offset-xs-6"/>
    </div>
</form>
<script type="text/javascript">
    function validar() {
        var salvar = document.getElementById("btn_salvar");
        var nome = document.getElementById("nome");
        if (nome.value === undefined || nome.value === "" || nome.value === null) {
            salvar.disabled = "disabled";
            return;
        }
        salvar.disabled = undefined;
    }
</script>
<%@include file="../shared/fim.jsp" %>