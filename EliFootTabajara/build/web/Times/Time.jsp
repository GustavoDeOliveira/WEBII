<%-- 
    Document   : Jogador
    Created on : 03/03/2018, 12:14:18
    Author     : gustavo
--%>

<%@page import="persistencia.TimeDAO"%>
<%-- 
    Document   : Index
    Created on : 02/03/2018, 19:34:57
    Author     : gustavo
--%>
<%@page import="modelo.Time"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.System"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Jogador"%>
<%@page import="persistencia.JogadorDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../shared/ini.jsp" %>
<%
    String idGet = request.getParameter("id");
    Time t;
    List<Jogador> jogadores = new ArrayList<>();
    out.println("<h2 class='panel-title text-center text-primary alert-primary'>");
    if (idGet != null) {
        t = new TimeDAO().carregar(Integer.parseInt(idGet));
        out.println("Editando Time " + t.getNome() + "</h2>");
    } else {
        t = new Time("");
        out.println("Criando Novo Time</h2>");
    }
    for (Jogador jogador : new JogadorDAO().listar()) {
        if (jogador.getTime().getId() == t.getId()) {
            jogadores.add(jogador);
        }
    }
%>
<form method="POST" action="./Salvar.jsp">
    <div class="form-group">
        <label for="nome">Nome</label>
        <input class="form-control" type="text" name="nome" id="nome" onblur="validar();" autofocus required
               <% out.println(" value='" + t.getNome() + "'"); %>/>
    </div>
    <%
        if (!jogadores.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div class='form-group'><label>Jogadores</label><ul>");
            for (int i = 0; i < jogadores.size(); i++) {
                Jogador j = jogadores.get(i);
                sb.append("<li>");
                sb.append("<a class='btn btn-xs btn-link' href='../Jogadores/Jogador.jsp?id=");
                sb.append(j.getId()).append("'>");
                sb.append(j.getNome()).append("</a>");
                sb.append("</li>");
            }
            sb.append("</ul></div>");
            out.println(sb);
        }
    %>
    <div class="form-group">
        <input type="hidden" name="id" id="id" <%out.println(" value='" + t.getId() + "'");%>/>
        <input id="btn_salvar" disabled type="submit" value="Salvar" class="btn btn-success btn-lg col-md-auto offset-md-10 offset-sm-8 offset-xs-6"/>
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
        salvar.disabled = undefined;
    }
</script>
<%@include file="../shared/fim.jsp" %>