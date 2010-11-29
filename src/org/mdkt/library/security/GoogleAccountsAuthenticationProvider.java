package org.mdkt.library.security;

import org.apache.log4j.Logger;
import org.mdkt.library.security.users.AppRole;
import org.mdkt.library.security.users.GaeUser;
import org.mdkt.library.security.users.UserRegistry;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * A simple authentication provider which interacts with {@code User} returned by the GAE {@code UserService},
 * and also the local persistent {@code UserRegistry} to build an application user principal.
 * <p>
 * If the user has been authenticated through google accounts, it will check if they are already registered
 * and either load the existing user information or assign them a temporary identity with limited access until they
 * have registered.
 * <p>
 * If the account has been disabled, a {@code DisabledException} will be raised.
 *
 * @author Luke Taylor
 * @author trung
 */
public class GoogleAccountsAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {
	private final Logger logger = Logger.getLogger(getClass());
	
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private UserRegistry userRegistry;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User googleUser = (User) authentication.getPrincipal();

        GaeUser user = userRegistry.findUser(googleUser.getUserId());

        if (user == null) {
            // User not in registry
            user = new GaeUser(googleUser.getUserId(), googleUser.getEmail());
            if (UserServiceFactory.getUserService().isUserAdmin()) {
            	logger.info("[" + googleUser.getUserId() + "] Welcome Administrator. You will be added to our registry. Access the backoffice to add more users");
            	user.addAuthoriy(AppRole.ADMIN);
            	userRegistry.registerUser(user);
            } else {
            	logger.debug("[" + googleUser.getUserId() + "] not found in our registry. Insufficent authentication. Accepted temporarily");
            	userRegistry.acceptUser(googleUser.getUserId(), googleUser.getEmail());
            	// can't throw 403 here as it's in the authentication process
            	throw new InsufficientAuthenticationException("You are not registered in our application yet. Please contact your local administrator.");
            }
        }

        if (!user.isEnabled()) {
            throw new DisabledException("Account is disabled");
        }

        return new GaeUserAuthentication(user, authentication.getDetails());
    }

    /**
     * Indicate that this provider only supports PreAuthenticatedAuthenticationToken (sub)classes.
     */
    public final boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setUserRegistry(UserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
