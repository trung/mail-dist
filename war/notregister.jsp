<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="javax.servlet.http.HttpServletResponse"%>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
	String sourceUrl = (String) request.getAttribute("source");
	boolean isRpc = sourceUrl != null ? sourceUrl.matches("^/[^/]+/rpc/.+") : false;
	String loginUrl = UserServiceFactory.getUserService().createLoginURL("/index.jsp");
	User googleUser = UserServiceFactory.getUserService().getCurrentUser();
	String currentUserEmail = googleUser != null ? googleUser.getEmail() : "unknown";
%>

<%
	if (isRpc) {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not registered in our application yet. Please contact your local administor to register.");
	} else {
%>

You are not registered in our application yet. Please contact your local administor to register. <br/>
If <b><%= currentUserEmail %></b> is not your correct account, please <a href="<%= loginUrl%>">sign in to another Google Account</a> and try again.

<%
	}
%>