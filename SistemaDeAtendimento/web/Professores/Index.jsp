<%-- 
    Document   : Index
    Created on : 02/03/2018, 19:34:57
    Author     : gustavo
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.System"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Professor"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="../shared/ini.jsp" %>
    <table class="table table-primary table-hover">
        <thead>
            <tr>
                <th colspan="2">
                    <h4 class="text-center text-default">Lista de Professores</h4>
                </th>
                <th><a href="./ProfessorServlet?acao=editar" class="btn btn-success">Novo Professor</a></th>
            </tr>
            <tr>
                <th>Nome</th>
                <th>Siape</th>
                <th width="120">Ações</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${professores.isEmpty()}">
                    <tr class="warning">
                        <td colspan="3">Não há nenhum professor cadastrado.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${professores}" var="professor">
                        <tr>
                            <td>${professor.nome}</td>
                            <td>${professor.siape}</td>
                            <td>
                                <a class="btn btn-xs btn-warning" href="./ProfessorServlet?acao=editar&id=${professor.id}">
                                    Editar
                                </a>
                                <a class="btn btn-xs btn-danger" href="./ProfessorServlet?acao=excluir&id=${professor.id}">
                                    Excluir
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
<%@include file="../shared/fim.jsp" %>