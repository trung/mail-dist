/**
 * 
 */
package org.mdkt.maildist.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Email alias
 * 
 * @author trung
 *
 */
public class Alias implements IsSerializable {
	
	private String userEmail;
	private String aliasId;
	private String email;
	
	public Alias() {
	}
	
	public String getAliasId() {
		return aliasId;
	}
	
	public void setAliasId(String aliasId) {
		this.aliasId = aliasId;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
