<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.talestonini.blackbook.controller.FrontController.Action" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utl" uri="https://black-book-1221.appspot.com" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>
<body>
<c:set var="user" value="${utl:currentUser()}"/>
<c:choose>
    <c:when test="${user != null}">
        <h1>${fn:escapeXml(user.nickname)}'s Gmail Black Book</h1>
        <p>Hello, ${fn:escapeXml(user.nickname)}! (You can <a href="${utl:logoutURL(pageContext.request)}">sign out</a>.)
        </p>
        <p>Add filters to erase unwanted emails from your inbox automatically. They will process every minute
            inspecting all your folders, including spam and bin.
        </p>
        <form action="/frontController" method="post">
            <input type="hidden" name="action" value="<%=Action.INSERT_QUERY%>"/>
            <div><input type="text" name="query"/> <input type="submit" value="Add"/></div>
        </form>
        <c:set var="gmailQueries" value="${utl:gmailQueries(user.email)}"/>
        <p>Filters for account '${fn:escapeXml(user.email)}' (${fn:length(gmailQueries)}):</p>
        <c:forEach var="gmailQuery" items="${gmailQueries}" varStatus="i">
            <form action="/frontController" method="post">
                <input type="hidden" name="action" value="<%=Action.DELETE_QUERY%>"/>
                <input type="hidden" name="queryId" value="${gmailQuery.id}"/>
                <div>${i.count}) ${fn:escapeXml(gmailQuery.query)} <input type="submit" value="Remove"/></div>
            </form>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <h1>Gmail Black Book</h1>
        <p>Hello! <a href="${utl:loginURL(pageContext.request)}">Sign in</a> to black-list unwanted emails.</p>
    </c:otherwise>
</c:choose>
</body>
</html>