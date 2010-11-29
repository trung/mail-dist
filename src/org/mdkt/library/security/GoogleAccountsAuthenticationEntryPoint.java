/**
 * 
 */
package org.mdkt.library.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 
 * Authentication using Google Accounts
 * 
 * @author trung
 *
 */
public class GoogleAccountsAuthenticationEntryPoint implements
		AuthenticationEntryPoint {

	private final Logger logger = Logger.getLogger(getClass());
	
	/* (non-Javadoc)
	 * @see org.springframework.security.web.AuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.debug("[" + request.getRequestURL() + "] Trying to access protected resource. Reply with 401");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please log in to continue.");
		
//		logger.debug("[" + request.getRequestURL() + "] Commencing Google Account authentication");
//		
//		UserService userService = UserServiceFactory.getUserService();
//		
//		String loginURL = userService.createLoginURL(request.getRequestURI());
//		
//		logger.debug("[" + request.getRequestURL() + "] Login URL created: [" + loginURL + "]");
//		
//		response.sendRedirect(loginURL);
	}
	
	

}
