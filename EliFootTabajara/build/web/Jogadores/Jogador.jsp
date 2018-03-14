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
    Jogador j;
    List<Time> times = new TimeDAO().listar();

    out.println("<h2 class='panel-title text-center text-primary alert-primary'>");
    if (idGet != null) {
        j = new JogadorDAO().carregar(Integer.parseInt(idGet));
        out.println("Editando Jogador " + j.getNome() + "</h2>");
    } else {
        j = new Jogador("", new Time(""));
        out.println("Criando Novo Jogador");
    }
    out.println("</h2>");

%>
<form method="POST" action="./Salvar.jsp">
    <div class="form-group">
        <label for="nome">Nome</label>
        <input class="form-control" type="text" name="nome" id="nome" onblur="validar();" autofocus required 
               <% out.println(" value='" + j.getNome() + "'"); %>/>
    </div>
    <div class="form-group">
        <label for="time_id">Time</label>
        <select class="form-control" name="time_id" id="time_id" required onchange="validar();">
            <%
                StringBuilder sb = new StringBuilder();
                if (times.isEmpty()) {
                    sb.append("<option id='invalid_option' class='alert-warning' value='0' selected>Não há nenhum time disponível.</option>");
                } else {
                    sb.append("<option id='invalid_option' value='0' selected>Selecione um time</option>");
                    for (int i = 0; i < times.size(); i++) {
                        Time t = times.get(i);
                        sb.append("<option value='").append(t.getId());
                        if (j.getTime().getId() == t.getId()) {
                            sb.append("' selected='selected");
                        }
                        sb.append("'>");
                        sb.append(t.getNome()).append("</option>");
                    }
                }
                out.println(sb);
            %>
        </select>
    </div>
    <div class="form-group">
        <input type="hidden" name="id" id="id" <% out.println("value='" + j.getId() + "'");%>/>
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
        if (opt.selected) {
            salvar.disabled = "disabled";
            return;
        }
        salvar.disabled = undefined;
    }
</script>
<%@include file="../shared/fim.jsp" %>