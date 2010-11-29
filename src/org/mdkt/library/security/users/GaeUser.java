/**
 * 
 */
package org.mdkt.library.security.users;

import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author trung
 *
 */
public class GaeUser implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5475978392059844705L;
	private final String userId;
    private final String email;
    private final String firstname;
    private final String lastname;
    private final Set<AppRole> authorities;
    private final boolean enabled;
    private long lastSignedIn;

    /**
     * Pre-registration constructor.
     *
     * Assigns the user the "USER" role only.
     */
    public GaeUser(String userId, String email) {
        this.userId = userId;
        this.authorities = EnumSet.of(AppRole.USER);
        this.firstname = null;
        this.lastname = null;
        this.email = email;
        this.enabled = true;
        this.lastSignedIn = 0;
    }

    /**
     * Post-registration constructor
     */
    public GaeUser(String userId, String email, String firstname, String lastname, Set<AppRole> authorities, boolean enabled) {
        this.userId = userId;
        this.email = email;
        this.authorities = authorities;
        this.firstname = firstname;
        this.lastname = lastname;
        this.enabled= enabled;
        this.lastSignedIn = 0;
    }

    public String getUserId() {
        return userId;
    }

    public long getLastSignedIn() {
		return lastSignedIn;
	}
    
    public void setLastSignedIn(long lastSignedIn) {
		this.lastSignedIn = lastSignedIn;
	}

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    public void addAuthoriy(AppRole authority) {
    	authorities.add(authority);
    }

    @Override
    public String toString() {
        return "GaeUser{" +
                "userId='" + userId + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", authorities=" + authorities +
                ", lastSignedIn=" + lastSignedIn +
                '}';
    }
}

