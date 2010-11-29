/**
 * 
 */
package org.mdkt.maildist.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Email distribution list
 * 
 * @author trung
 *
 */
public class DistListMember implements IsSerializable {
	private String distListId;
	private String distListMemberId;
	private String name;
	private String email;
	
	public DistListMember() {
	}
	
	public void setDistListId(String distListId) {
		this.distListId = distListId;
	}
	
	public String getDistListId() {
		return distListId;
	}

	public String getDistListMemberId() {
		return distListMemberId;
	}

	public void setDistListMemberId(String distListMemberId) {
		this.distListMemberId = distListMemberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
