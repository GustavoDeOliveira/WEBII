<%@include file="/WEB-INF/jspf/ini.jspf" %>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="table table-hover">
    <thead>
        <tr>
            <th width="200">Nome</th>
            <th width="160">CPF</th>
            <th width="400">E-Mail</th>
            <th width="220">Ações</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="aluno" items="${alunos}">
            <tr>
                <td>${aluno.nome}</td>
                <td>${aluno.cpf}</td>
                <td>${aluno.email}</td>
                <td>
                    <a data-target="#detalhes-${aluno.id}" data-toggle="collapse" class="btn btn-xs btn-primary">
                        Detalhes
                    </a>
                    <a href="${linkTo[AlunoController].editar()}${aluno.id.toString()}" class="btn btn-xs btn-warning">
                        Editar
                    </a>
                    <form method="DELETE" action="${linkTo[AlunoController].excluir()}${aluno.id.toString()}">
                        <input type="submit" value="Excluir" class="btn btn-xs btn-danger" />
                    </form>
                </td>
            </tr>
            <tr class="collapse" id="detalhes-${aluno.id}">
                <jsp:useBean id="date" class="java.util.Date"/>
                <td colspan="2">
                    <strong>Último pagamento:</strong>
                    <fmt:formatDate value="${aluno.ultimoPagamento}" type="date" pattern="dd-MM-yyyy"/>
                </td>
                <td colspan="2">
                    <strong>Prazo para o próximo pagamento:</strong> 
                    <fmt:formatDate value="${aluno.prazoPagamento}" type="date" pattern="dd-MM-yyyy"/>
                </td>
            </tr>
        </c:forEach>
    </tbody>
    <tfoot>
        <tr>
            <th colspan="4">
                <a class="btn btn-success btn-block" href="${linkTo[AlunoController].editar()}-1">
                    &plus;
                </a>
            </th>
        </tr>
    </tfoot>
</table>

<%@include file="/WEB-INF/jspf/fim.jspf" %>