<%-- 
    Document   : Aluno
    Created on : 03/03/2018, 12:14:18
    Author     : gustavo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 
    Document   : Index
    Created on : 02/03/2018, 19:34:57
    Author     : gustavo
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.System"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Atendimento"%>
<%@page import="modelo.Aluno"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../shared/ini.jsp" %>
<h2 class='panel-title text-center text-primary alert-primary'>
    <c:choose>
        <c:when test="${aluno != null && aluno.id != null}">
            Editando Aluno
        </c:when>
        <c:otherwise>
            Criando Novo Aluno
        </c:otherwise>
    </c:choose>
</h2>
<form method="POST" action="./AlunoServlet?acao=salvar">
    <div class="form-group">
        <label for="nome">Nome</label>
        <input class="form-control" type="text" name="nome" value="${aluno.nome}" autofocus required/>
        <label for="matricula">Matrícula</label>
        <input class="form-control" type="text" name="matricula" value="${aluno.matricula}" required/>
    </div>
    <div class="form-group">
        <input type="hidden" name="id" value="${aluno.id}"/>
        <input id="btn_salvar" class="btn btn-success btn-lg col-md-auto offset-md-10 offset-sm-8 offset-xs-6" type="submit" value="Salvar"/>
    </div>
</form>
<div class="row">
    <div class="col-xs-8 col-xs-offset-2">
        <table class="table table-primary table-hover">
            <thead>
                <tr>
                    <th colspan="3">
                        <h4 class="text-center text-default">Lista de Atendimentos</h4>
                    </th>
                </tr>
                <tr>
                    <th>Início</th>
                    <th>Término</th>
                    <th>Professor</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${atendimentos.isEmpty()}">
                        <tr class="warning">
                            <td colspan="3">Não há nenhum atendimento cadastrado.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${atendimentos}" var="atendimento">
                            <tr>
                                <td>${atendimento.dataInicio}</td>
                                <td>${atendimento.dataFim}</td>
                                <td>${atendimento.professor.nome} - ${atendimento.professor.siape}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>
<%@include file="../shared/fim.jsp" %>