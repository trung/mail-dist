/**
 * 
 */
package org.mdkt.backoffice.client.rpc;

import java.util.ArrayList;

import org.mdkt.backoffice.client.dto.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author trung
 *
 */
public interface UsersServiceAsync {

	void deleteUser(String userId, AsyncCallback<Boolean> callback);

	void getUser(String userId, AsyncCallback<User> callback);

	void deleteUsers(ArrayList<String> userIds,
			AsyncCallback<ArrayList<User>> callback);

	void getUserDetails(AsyncCallback<ArrayList<User>> callback);

	void updateUser(User user, AsyncCallback<User> callback);

	void getAcceptedUser(String email, AsyncCallback<String> callback);

}
