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
public class DistList implements IsSerializable {
	private String userId;
	private String distListId;
	private int noOfMembers;
	
	public DistList() {
	}
	
	public void setDistListId(String distListId) {
		this.distListId = distListId;
	}
	
	public String getDistListId() {
		return distListId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public int getNoOfMembers() {
		return noOfMembers;
	}
	
	public void setNoOfMembers(int noOfMembers) {
		this.noOfMembers = noOfMembers;
	}
}
