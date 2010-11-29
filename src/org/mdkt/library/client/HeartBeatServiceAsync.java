/**
 * 
 */
package org.mdkt.library.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author trung
 *
 */
public interface HeartBeatServiceAsync {

	void getSessionTimeout(AsyncCallback<Integer> callback);
	
}
