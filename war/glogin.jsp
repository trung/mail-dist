<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	// get current user id and push to servletContext 
	User googleUser = UserServiceFactory.getUserService().getCurrentUser();
	if (googleUser != null) {
		application.removeAttribute(googleUser.getUserId());
	}
	String fromUrl = request.getParameter("continue");
	String loginUrl = UserServiceFactory.getUserService().createLoginURL(fromUrl);
	response.sendRedirect(loginUrl);
%>