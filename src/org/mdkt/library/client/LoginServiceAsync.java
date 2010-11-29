/**
 * 
 */
package org.mdkt.library.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author trung
 *
 */
public interface LoginServiceAsync {

	void getLoginUrl(String fromUrl, AsyncCallback<String> callback);

}
