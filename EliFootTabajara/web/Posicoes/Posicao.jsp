<%-- 
    Document   : Posicao
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
<%@page import="modelo.Posicao"%>
<%@page import="persistencia.PosicaoDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../shared/ini.jsp" %>
<h2 class='panel-title text-center text-primary alert-primary'>
    <c:choose>
        <c:when test="${posicao != null && posicao.id != null}">
            Editando Posicao
        </c:when>
        <c:otherwise>
            Criando Nova Posicao
        </c:otherwise>
    </c:choose>
</h2>
<form method="POST" action="./PosicaoServlet?acao=salvar&id=${posicao.id}">
    <div class="form-group">
        <label for="descricao">Descricao</label>
        <input class="form-control" type="text" name="descricao" id="descricao" value="${posicao.descricao}" onblur="validar();" autofocus required/>
    </div>
    <div class="form-group">
        <input type="hidden" name="id" id="id" ${posicao.id}/>
        <input id="btn_salvar" disabled class="btn btn-success btn-lg col-md-auto offset-md-10 offset-sm-8 offset-xs-6" type="submit" value="Salvar"/>
    </div>
</form>
<script type="text/javascript">
    function validar() {
        var salvar = document.getElementById("btn_salvar");
        var descricao = document.getElementById("descricao");
        if (descricao.value === undefined || descricao.value === "" || descricao.value === null) {
            salvar.disabled = "disabled";
            return;
        }
        salvar.disabled = undefined;
    }
</script>
<%@include file="../shared/fim.jsp" %>