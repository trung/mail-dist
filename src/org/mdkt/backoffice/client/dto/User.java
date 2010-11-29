/**
 * 
 */
package org.mdkt.backoffice.client.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author trung
 *
 */
public class User implements Serializable, IsSerializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String email;
	private String nickname;
	private String firstname;
	private String lastname;
	private Set<String> authorities;
	private boolean enabled;
	private Date lastSignedIn;
	private boolean currentUser;
	
	public User() {
		userId = null;
	}
	
	public User(String userId, String email, String nickname,
			String firstname, String lastname, Set<String> authorities,
			boolean enabled) {
		super();
		this.userId = userId;
		this.email = email;
		this.nickname = nickname;
		this.firstname = firstname;
		this.lastname = lastname;
		this.authorities = authorities;
		this.enabled = enabled;
	}

	public String getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public String getNickname() {
		return nickname;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	public boolean isEnabled() {
		return enabled;
	}
    
	public Date getLastSignedIn() {
		return lastSignedIn;
	}
	
	public void setLastSignedIn(Date lastSignedIn) {
		this.lastSignedIn = lastSignedIn;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getDisplayName() {
		return this.firstname + " " + this.getLastname();
	}

	public boolean isCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(boolean currentUser) {
		this.currentUser = currentUser;
	}
}
