<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Home Page</title>
  </head>
  <body>
  	<sec:authorize access="hasRole('REG_USER')">
    	<% response.sendRedirect("/Mail_dist.html"); %>
    </sec:authorize>
   	<sec:authorize access="hasRole('ADMIN')">
    	<% response.sendRedirect("/BackOffice.html"); %>
    </sec:authorize>
  	<sec:authorize access="hasRole('USER')">
    	<p>Welcome, <b><sec:authentication property="principal.email"/></b>! Please contact your local administrator for further authorized access or <a href="/logout.jsp">log out</a>.</p>
    </sec:authorize>
    <sec:authorize access="isAnonymous()">
    	<%
			String gloginUrl = "/glogin.jsp?continue=" + URLEncoder.encode(request.getRequestURI());
		%>
  		<p>Please sign in to continue. <a href="<%= gloginUrl%>">Sign in with Google Account</a></p>
    </sec:authorize>
  </body>
</html>
