<%-- 
    Document   : Index
    Created on : 02/03/2018, 19:34:57
    Author     : gustavo
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.System"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Aluno"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="../shared/ini.jsp" %>
    <table class="table table-primary table-hover">
        <thead>
            <tr>
                <th colspan="2">
                    <h4 class="text-center text-default">Lista de Alunos</h4>
                </th>
                <th><a href="./AlunoServlet?acao=editar" class="btn btn-success">Novo Aluno</a></th>
            </tr>
            <tr>
                <th>Nome</th>
                <th>Matrícula</th>
                <th width="120">Ações</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${alunos.isEmpty()}">
                    <tr class="warning">
                        <td colspan="3">Não há nenhum aluno cadastrado.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${alunos}" var="aluno">
                        <tr>
                            <td>${aluno.nome}</td>
                            <td>${aluno.matricula}</td>
                            <td>
                                <a class="btn btn-xs btn-warning" href="./AlunoServlet?acao=editar&id=${aluno.id}">
                                    Editar
                                </a>
                                <a class="btn btn-xs btn-danger" href="./AlunoServlet?acao=excluir&id=${aluno.id}">
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