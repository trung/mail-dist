package org.mdkt.library.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mdkt.library.security.users.UserRegistry;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author Luke Taylor
 */
public class GaeAuthenticationFilter extends GenericFilterBean {
    private static final String HOME = "/";

    private final Logger logger = Logger.getLogger(getClass());

    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> ads = new WebAuthenticationDetailsSource();
    private AuthenticationManager authenticationManager;
    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
    
    private UserRegistry userRegistry;
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        
		logger.debug("[" + httpServletRequest.getRequestURL() + "] Authentication filter [" + authentication + "]");
        if (authentication == null) {
            User googleUser = UserServiceFactory.getUserService().getCurrentUser();
            if (googleUser != null) {
            	// user could sign in to Google but has been logged out to our application
                Object isLoggedOut = getServletContext().getAttribute(googleUser.getUserId());
                if (isLoggedOut == null) { // user hasn't logged in yet, so use his/her google account to log in
                	logger.debug("Currently logged on to GAE as google user " + googleUser);
                	logger.debug("Authenticating to Spring Security");
                	// User has returned after authenticating via GAE. Need to authenticate through Spring Security.
                	PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(googleUser, null);
                	token.setDetails(ads.buildDetails((HttpServletRequest) request));
                	logger.debug("Update last signed in");
                	userRegistry.updateLastSignedIn(googleUser.getUserId());
                	try {
                		authentication = authenticationManager.authenticate(token);
                		SecurityContextHolder.getContext().setAuthentication(authentication);
                		getServletContext().removeAttribute(googleUser.getUserId()); // to mark that user has logged in to our app
                		if (httpServletRequest.getRequestURI().matches("^/[^/]+/rpc/.+")) {
                			// it's an rpc, don't do anything let the service handle
                			logger.debug("[" + httpServletRequest.getRequestURL() + "] From an RPC. Ignore");
                		} else {
                			logger.debug("Returning to home");
                			// return to home
                			((HttpServletResponse) response).sendRedirect(HOME);
                			return;
                		}
                		
                	} catch (AuthenticationException e) {
                		if (e instanceof InsufficientAuthenticationException) {
                			httpServletRequest.setAttribute("source", httpServletRequest.getRequestURI());
                		}
                		
                		failureHandler.onAuthenticationFailure((HttpServletRequest)request, (HttpServletResponse)response, e);
                		
                		return;
                	}                	
                } else {
                	logger.debug("[" + googleUser.getUserId() + "] has logged out explicitly before. Need to login again.");
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(authenticationManager, "AuthenticationManager must be set");
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }
    
    public void setUserRegistry(UserRegistry userRegistry) {
		this.userRegistry = userRegistry;
	}
}
