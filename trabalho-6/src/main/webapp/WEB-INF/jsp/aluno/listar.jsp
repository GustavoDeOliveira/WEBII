<%@include file="/WEB-INF/jspf/ini.jspf" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="table table-hover">
    <thead>
        <tr>
            <th>Nome</th>
            <th>CPF</th>
            <th>E-Mail</th>
            <th width="160">Ações</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="aluno" items="${alunos}">
            <tr>
                <td>${aluno.nome}</td>
                <td>${aluno.cpf}</td>
                <td>${aluno.email}</td>
                <td>
                    <a href="#detalhes-${aluno.id}" data-toggle="collapse" class="btn btn-xs btn-primary">
                        Editar
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
                <td><strong>Último pagamento:</strong> ${aluno.ultimoPagamento}</td>
                <td><strong>Prazo para o próximo pagamento:</strong> ${aluno.prazoPagamento}</td>
            </tr>
        </c:forEach>
    </tbody>
    <tfoot>
        <tr>
            <th colspan="4">
                <a class="btn btn-success pull-right" href="${linkTo[AlunoController].editar()}-1">
                    &plus;
                </a>
            </th>
        </tr>
    </tfoot>
</table>

<%@include file="/WEB-INF/jspf/fim.jspf" %>