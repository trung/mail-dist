package org.mdkt.library.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc/loginService")
public interface LoginService extends RemoteService {
	
	/**
	 * Ask server to return login URL with continue fromUrl 
	 * @return
	 */
	String getLoginUrl(String fromUrl);
	
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static LoginServiceAsync instance;
		public static LoginServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(LoginService.class);
			}
			return instance;
		}
	}
}
