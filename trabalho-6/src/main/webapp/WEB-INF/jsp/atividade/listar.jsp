<%@include file="/WEB-INF/jspf/ini.jspf" %>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="table table-hover">
    <thead>
        <tr>
            <th>Nome</th>
            <th width="100">Preço</th>
            <th width="100">Alunos</th>
            <th width="160">Ações</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="atividade" items="${atividades}">
            <tr>
                <td>${atividade.nome}</td>
                <td>R$${atividade.preco}</td>
                <td>${atividade.alunos.size()}</td>
                <td>
                    <a href="${linkTo[AtividadeController].editar(atividade.id.toString())}" class="btn btn-xs btn-warning">
                        Editar
                    </a>
                    <a href="${linkTo[AtividadeController].excluir()}${atividade.id.toString()}" class="btn btn-xs btn-danger">
                        Excluir
                    </a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
    <tfoot>
        <tr>
            <th colspan="4">
                <a class="btn btn-success btn-block" href="${linkTo[AtividadeController].editar()}-1">
                    <strong>&plus;</strong>
                </a>
            </th>
        </tr>
    </tfoot>
</table>

<%@include file="/WEB-INF/jspf/fim.jspf" %>