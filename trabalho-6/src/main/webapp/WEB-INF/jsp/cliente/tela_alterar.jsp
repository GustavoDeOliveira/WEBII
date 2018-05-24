<%-- 
    Document   : tela_alterar
    Created on : 22/05/2018, 21:31:46
    Author     : iapereira
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>


    <c:forEach var="error" items="${errors}">
        ${error.category} - ${error.message}<br />
    </c:forEach>

    <form action="${pageContext.request.contextPath}/cliente/alterar" method="post">
        Nome: <input type="text" name="cliente.nome" value="${cliente.nome}">    
        <input type="hidden" value="${cliente.id}" name="cliente.id">
        <input type="submit">        
    </form>
</body>
</html>
