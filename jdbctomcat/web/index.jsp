<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 04.10.13
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
    <table border="1">
        <tr><td><b>Date</b></td><td><b>Message</b></td></tr>
    <c:forEach var="var1" items="${requestScope.posts}">
        <tr><td><c:out value="${var1.date}"></c:out></td><td><c:out value="${var1.message}"></c:out></td></tr>
    </c:forEach>
    </table>
    <form method="post" action="/test">
        <input type="text" name="message"/>
        <input type="submit"/>
    </form>

  </body>
</html>