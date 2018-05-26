<%@include file="/WEB-INF/jspf/ini.jspf" %>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form method="POST" 
      action="${aluno.id < 1 ? linkTo[AlunoController].adicionar() 
                : linkTo[AlunoController].editarPost()}">
      <c:if test="${aluno.id > 0}">
          <input type="hidden" name="aluno.id" value="${aluno.id}" />
      </c:if>
      <label for="aluno.nome">Nome</label>
      <input type="text" name="aluno.nome" value="${aluno.nome}" class="form-control" autofocus required placeholder="Nome" />
      <label for="aluno.cpf">CPF</label>
      <input type="text" name="aluno.cpf" value="${aluno.cpf}" class="form-control" required placeholder="12345678901" /><br/>
      <label for="aluno.email">E-Mail</label>
      <input type="email" name="aluno.email" value="${aluno.email}" class="form-control" required placeholder="exemplo@exemplo.com" /><br/>
      <label for="aluno.senha">Senha</label>
      <input type="password" name="aluno.senha" value="${aluno.senha}" class="form-control" placeholder="Opcional" /><br/>
      <c:if test="${aluno.id > 0}">
          <label for="aluno.ultimoPagamento">Data do último pagamento realizado</label>
          <input type="date" name="aluno.ultimoPagamento" value="${aluno.ultimoPagamento}" class="form-control" placeholder="dd/mm/aaaa" /><br/>
          <label for="aluno.prazoPagamento">Prazo para o próximo pagamento</label>
          <input type="date" name="aluno.prazoPagamento" value="${aluno.prazoPagamento}" class="form-control" placeholder="dd/mm/aaaa" /><br/>

          <c:if test="${aluno.atividades.size() > 0}">
              <table class="table table-striped">
                  <thead>
                  <th colspan="2">Atividades</th>
                  </thead>
                  <tbody>
                      <c:forEach var="atividade" items="${aluno.atividades}" >
                          <tr>
                              <td>
                                  <a href="${linkTo[AtividadeController].editar()}${atividade.id}">${atividade.nome}</a>
                              </td>
                              <td>R$${atividade.preco}</td>
                          </tr>
                      </c:forEach>
                  </tbody>
                  <tfoot>
                      <tr>
                          <th>Total:</th>
                          <td>
                              R$${mensalidade}
                          </td>
                      </tr>
                  </tfoot>
              </table>
          </c:if>
      </c:if>
      <input type="submit" value="Salvar" class="btn btn-success pull-right" />
</form>

<%@include file="/WEB-INF/jspf/fim.jspf" %>