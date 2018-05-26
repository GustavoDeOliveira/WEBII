<%@include file="/WEB-INF/jspf/ini.jspf" %>
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
    <input type="number" name="atividade.preco" value="${atividade.preco}" class="form-control" required min="0" step="0.05" /><br/>
    <input type="submit" value="Salvar" class="btn btn-success pull-right" />
</form>

<%@include file="/WEB-INF/jspf/fim.jspf" %>