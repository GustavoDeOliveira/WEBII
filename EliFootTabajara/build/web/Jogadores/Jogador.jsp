<%-- 
    Document   : Jogador
    Created on : 03/03/2018, 12:14:18
    Author     : gustavo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="persistencia.TimeDAO"%>
<%-- 
    Document   : Index
    Created on : 02/03/2018, 19:34:57
    Author     : gustavo
--%>
<%@page import="modelo.Equipe"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.System"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Jogador"%>
<%@page import="persistencia.JogadorDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../shared/ini.jsp" %>
<h2 class='panel-title text-center text-primary alert-primary'>
    <c:choose>
        <c:when test="${jogador != null && jogador.id != null}">
            Editando Jogador
        </c:when>
        <c:otherwise>
            Criando Novo Jogador
        </c:otherwise>
    </c:choose>
</h2>
<form method="POST" action="./JogadorServlet?acao=salvar&id=${jogador.id}">
    <div class="form-group">
        <label for="nome">Nome</label>
        <input class="form-control" type="text" name="nome" id="nome" value="${jogador.nome}" onblur="validar();" autofocus required/>
    </div>
    <div class="form-group">
        <label for="time_id">Time</label>
        <select class="form-control" name="time_id" id="time_id" required onchange="validar();">
            <c:choose>
                <c:when test="${times.isEmpty()}">
                    <option id='invalid_option' class='alert-warning' value='0' selected>Não há nenhum time disponível.</option>
                </c:when>
                <c:otherwise>
                    <option id='invalid_option' value='0' selected>Selecione um time</option>
                    <c:forEach items="${times}" var="time">
                        <option value="${time.id}" <c:if test="${time.id == jogador.equipe.id}">selected</c:if>>
                            ${time.nome}
                        </option>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </select>
    </div>
    <div class="form-group">
        <label for="posicao_id">Time</label>
        <select class="form-control" name="posicao_id" id="posicao_id" required onchange="validar();">
            <c:choose>
                <c:when test="${posicoes.isEmpty()}">
                    <option id='invalid_option2' class='alert-warning' value='0' selected>Não há nenhuma posição disponível.</option>
                </c:when>
                <c:otherwise>
                    <option id='invalid_option2' value='0' selected>Selecione um posição</option>
                    <c:forEach items="${posicoes}" var="posicao">
                        <option value="${posicao.id}" <c:if test="${posicao.id == jogador.equipe.id}">selected</c:if>>
                            ${posicao.descricao}
                        </option>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </select>
    </div>
    <div class="form-group">
        <input type="hidden" name="id" id="id" ${jogador.id}/>
        <input id="btn_salvar" disabled class="btn btn-success btn-lg col-md-auto offset-md-10 offset-sm-8 offset-xs-6" type="submit" value="Salvar"/>
    </div>
</form>
<script type="text/javascript">
    function validar() {
        var salvar = document.getElementById("btn_salvar");
        var nome = document.getElementById("nome");
        if (nome.value === undefined || nome.value === "" || nome.value === null) {
            salvar.disabled = "disabled";
            return;
        }
        var opt = document.getElementById("invalid_option");
        var opt2 = document.getElementById("invalid_option2");
        if (opt.selected || opt2.selected) {
            salvar.disabled = "disabled";
            return;
        }
        salvar.disabled = undefined;
    }
</script>
<%@include file="../shared/fim.jsp" %>