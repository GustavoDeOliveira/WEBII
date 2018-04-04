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
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.System"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Equipe"%>
<%@page import="modelo.Jogador"%>
<%@page import="persistencia.JogadorDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../shared/ini.jsp" %>
<h2 class='panel-title text-center text-primary alert-primary'>
    <c:choose>
        <c:when test="${time != null && time.id != 0}">
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
        <input class="form-control" type="text" name="nome" id="cmp-nome" value="${time.nome}" autofocus required/>
    </div>
    <div class="form-group">
        <label for="tecnico_id">Tecnico</label>
        <select class="form-control" type="text" name="tecnico_id" id="drop-tecnico">
            <option value="" <c:if test="${time.tecnico == null}">selected</c:if>>Nenhum</option>
            <c:forEach items="${tecnicos}" var="tecnico">
                <option value="${tecnico.id}" <c:if test="${tecnico.id == time.tecnico.id}">selected</c:if>>${tecnico.nome}</option>
            </c:forEach>
        </select>
    </div>
        <c:if test="${time != null && !time.jogadores.isEmpty()}">
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
        <input id="btn-salvar" type="submit" value="Salvar" class="btn btn-success btn-lg col-md-auto offset-md-10 offset-sm-8 offset-xs-6"/>
    </div>
</form>
<%@include file="../shared/fim.jsp" %>