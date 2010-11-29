<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
	response.setContentType("text/javascript");
%>
<sec:authorize access="isAuthenticated()">
	var info = {"email":"<sec:authentication property="principal.email"/>"};
</sec:authorize>