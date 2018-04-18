<%-- 
    Document   : Atendimento
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
<%@page import="modelo.Professor"%>
<%@page import="modelo.Aluno"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../shared/ini.jsp" %>
<h2 class='panel-title text-center text-primary alert-primary'>
    <c:choose>
        <c:when test="${atendimento != null && atendimento.id != null}">
            Editando Atendimento
        </c:when>
        <c:otherwise>
            Criando Novo Atendimento
        </c:otherwise>
    </c:choose>
</h2>
<form method="POST" action="./AtendimentoServlet?acao=salvar">
    <div class="form-group">
        <label for="dataInicio">Data de Início</label>
        <input type="datetime" name="dataInicio" class="form-control" value="${atendimento.dataInicio}" autofocus required />
    </div>
    <div class="form-group">
        <label for="dataFim">Data de Término</label>
        <input type="datetime" name="dataFim" class="form-control" value="${atendimento.dataFim}" required />
    </div>
    <div class="form-group">
        <select class="form-control" name="aluno" required>
            <c:choose>
                <c:when test="${alunos.isEmpty()}">
                    <option value="">Nenhum aluno cadastrado.</option>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${alunos}" var="aluno">
                        <option value="${aluno.id}" <c:if test="${aluno.id == atendimento.aluno.id}">selected</c:if>>${aluno.nome} - ${aluno.matricula}</option>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </select>
    </div>
    <input type="hidden" value="${atendimento.id}" name="id" />
    <input type="hidden" value="${atendimento.professor.id != null ? atendimento.professor.id : professor}" name="professor" />
    <input type="submit" value="Salvar" class="btn btn-danger" />
</form>
<%@include file="../shared/fim.jsp" %>