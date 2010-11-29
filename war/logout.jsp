<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
	// get current user id and push to servletContext 
	User googleUser = UserServiceFactory.getUserService().getCurrentUser();
	if (googleUser != null) {
		application.setAttribute(googleUser.getUserId(), new Object()); // just to mark this user has logged out from our application
	}
	session.invalidate();
	
	response.sendRedirect("/index.jsp");
%>