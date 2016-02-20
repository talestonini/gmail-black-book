<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.talestonini.blackbook.controller.FrontController.Action" %>
<%@ page import="com.talestonini.blackbook.model.GmailQuery" %>
<%@ page import="com.talestonini.blackbook.repository.GmailQueryRepository" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
        pageContext.setAttribute("user", user);
%>
<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
    <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)
</p>
<p>NOTE: You must be logged-in here with the same account you're currently logged-in in Gmail!</p>
<%
        List<GmailQuery> gmailQueries = GmailQueryRepository.getInstance().findByEmailAddress(user.getEmail());
        pageContext.setAttribute("count", gmailQueries.size());
%>
<p>Filters for account '${fn:escapeXml(user.email)}' (${count}):</p>
<form action="/frontController" method="post">
    <input type="hidden" name="action" value="<%=Action.INSERT_QUERY%>"/>
    <input type="hidden" name="userId" value="${user.userId}"/>
    <input type="hidden" name="emailAddress" value="${user.email}"/>
    <div><input type="text" name="query" size="60"/> <input type="submit" value="Ins"/></div>
</form>
<%
        if (!gmailQueries.isEmpty()) {
            int i = 0;
            for (GmailQuery gmailQuery : gmailQueries) {
                pageContext.setAttribute("i", ++i);
                pageContext.setAttribute("gmailQuery", gmailQuery);
%>
<form action="/frontController" method="post">
    <input type="hidden" name="action" value="<%=Action.DELETE_QUERY%>"/>
    <input type="hidden" name="emailAddress" value="${user.email}"/>
    <input type="hidden" name="queryId" value="${gmailQuery.id}"/>
    <div>${i}) ${fn:escapeXml(gmailQuery.query)} <input type="submit" value="Del"/></div>
</form>
<%
            }
%>
<form action="/frontController" method="post">
    <input type="hidden" name="action" value="<%=Action.RUN_NOW%>"/>
    <input type="hidden" name="userId" value="${user.userId}"/>
    <input type="hidden" name="emailAddress" value="${user.email}"/>
    <div><input type="submit" value="Run Now"/></div>
</form>
<%
        }
    } else {
%>
<p>Hello!
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
    to black-list undesired emails.
</p>
<p>NOTE: You must be logged-in here with the same account you're currently logged-in in Gmail!</p>
<%
    }
%>
</body>
</html>