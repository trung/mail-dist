package org.mdkt.backoffice.client.rpc;

import java.util.ArrayList;

import org.mdkt.backoffice.client.dto.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc/usersService")
public interface UsersService extends RemoteService {
	
	Boolean deleteUser(String userId);
	ArrayList<User> deleteUsers(ArrayList<String> userIds);
	ArrayList<User> getUserDetails();
	User getUser(String userId);
	String getAcceptedUser(String email);
	/**
	 * Create/update
	 * @param user
	 * @return
	 */
	User updateUser(User user);
	
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static UsersServiceAsync instance;
		public static UsersServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(UsersService.class);
			}
			return instance;
		}
	}
}
