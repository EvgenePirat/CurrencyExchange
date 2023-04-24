<%--
  Created by IntelliJ IDEA.
  User: Євген
  Date: 4/22/2023
  Time: 7:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <h2 align="center"> Web App get exception!</h2>
    <c:if test="${not empty requestScope.error}">
        <div class="error">${requestScope.error}</div>
    </c:if>
</body>
</html>
