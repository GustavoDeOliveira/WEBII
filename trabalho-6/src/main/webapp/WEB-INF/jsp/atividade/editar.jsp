<%@include file="/WEB-INF/jspf/ini.jspf" %>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form method="POST" 
      action="${atividade.id < 1 ? linkTo[AtividadeController].adicionar() 
                                 : linkTo[AtividadeController].editarPost()}">
    <c:if test="${atividade.id > 0}">
        <input type="hidden" name="atividade.id" value="${atividade.id}" />
    </c:if>
    <label for="atividade.nome">Nome</label>
    <input type="text" name="atividade.nome" value="${atividade.nome}" class="form-control" autofocus required placeholder="Nome" />
    <label for="atividade.preco">Preço</label>
    <input type="number" name="atividade.preco" value="${atividade.preco}" class="form-control" required min="0.0" step="0.05" /><br/>
    <input type="submit" value="Salvar" class="btn btn-success pull-right" />
</form>
    
<c:if test="${atividade.id > 0}">
    <h3>Alunos</h3>
    <form method="GET" class="form form-inline" action="${linkTo[AtividadeController].adicionarAluno()}${atividade.id}">
        <select class="form-control" name="aluno" required>
            <option value="">Selecione um aluno</option>
            <c:forEach var="aluno" items="${alunos}">
                <c:if test="${!atividade.alunos.contains(aluno)}">
                    <option value="${aluno.id}">${aluno.nome} &mdash; ${aluno.cpf}</option>
                </c:if>
            </c:forEach>
        </select>
        <input type="submit" value="Adicionar" class="btn btn-success" />
    </form>
    <ul class="list-group">
        <c:forEach var="aluno" items="${atividade.alunos}">
            <li class="list-group-item">
                ${aluno.nome} &mdash; ${aluno.cpf}
                <form method="GET" action="${linkTo[AtividadeController].removerAluno()}${atividade.id}">
                    <input type="hidden" name="aluno" value="${aluno.id}" />
                    <input type="submit" value="Remover" class="btn btn-danger btn-sm" />
                </form>
                <a href="${linkTo[AlunoController].editar()}${aluno.id}" class="btn btn-warning btn-sm">
                    Editar
                </a>
            </li>
        </c:forEach>
    </ul>
</c:if>

<%@include file="/WEB-INF/jspf/fim.jspf" %>