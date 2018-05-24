<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>VRaptor Blank Project</title>
    </head>
    <body>


        <%--
     <c:forEach var="error" items="${errors}">
         ${error.category} - ${error.message}<br />
     </c:forEach>
        --%>

        <form action="${pageContext.request.contextPath}/cliente/adicionar" method="post">
            Nome: <input type="text" name="cliente.nome">    
            <span class="error">${errors.from('cliente.nome')}</span>        
            <input type="submit">        
        </form>
        <hr>
        <table border="1">
            <c:forEach items="${vetCliente}" var="cliente">
                <tr> 
                    <td> <a href="${pageContext.request.contextPath}/cliente/excluir/${cliente.id}">Excluir </a> </td>
                    <td> <a href="${pageContext.request.contextPath}/cliente/tela_alterar/${cliente.id}">Alterar </a> </td>
                    <td> ${cliente.nome} </td>                
                </tr>
            </c:forEach>            
        </table>
    </body>
</html>