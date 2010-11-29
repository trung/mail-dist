package org.mdkt.library.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc/heartBeatService")
public interface HeartBeatService extends RemoteService {
	
	Integer getSessionTimeout();
	
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static HeartBeatServiceAsync instance;
		public static HeartBeatServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(HeartBeatService.class);
			}
			return instance;
		}
	}
}
