<%@include file="/WEB-INF/jspf/ini.jspf" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form method="POST" action="${linkTo[IndexController].logar()}">
    <label for="usuario.login">Login</label>
    <input type="text" name="usuario.login" autofocus required placeholder="Login" class="form-control" />
    <label for="usuario.senha">Senha</label>
    <input type="password" name="usuario.senha" required placeholder="Senha" class="form-control" /><br/>
    <input type="submit" value="Logar" class="btn btn-success pull-right" />
</form>

<%@include file="/WEB-INF/jspf/fim.jspf" %>