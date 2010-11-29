package org.mdkt.library.server;

import javax.servlet.ServletContext;

import org.mdkt.library.client.LoginService;
import org.springframework.web.context.ServletContextAware;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LoginServiceImpl implements LoginService, ServletContextAware {

	private ServletContext servletContext;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@Override
	public String getLoginUrl(String fromUrl) {
		UserService userService = UserServiceFactory.getUserService();
		String loginURL = userService.createLoginURL(fromUrl);
		// now the user wants to login
		User googleUser = userService.getCurrentUser();
		if (googleUser != null) {
			servletContext.removeAttribute(googleUser.getUserId()); // to mark that user has logged in to our app
		}
		return loginURL;
	}
}
