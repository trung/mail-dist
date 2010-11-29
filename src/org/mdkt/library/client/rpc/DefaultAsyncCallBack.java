/**
 * 
 */
package org.mdkt.library.client.rpc;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Wrapper of {@link AsyncCallback}. Display loading message and hide it during the operation
 * @author trung
 *
 */
public abstract class DefaultAsyncCallBack<T> implements AsyncCallback<T> {

	private String action;
	
	public DefaultAsyncCallBack(String action) {
		this.action = action;
	}
	
	@Override
	public void onSuccess(T result) {
		_onSuccess(result);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
	 */
	@Override
	public void onFailure(Throwable caught) {
		String errorMessage = caught.toString();
		if (errorMessage.indexOf("403") != -1) {
			Window.Location.assign("/403.html");
		} else if (errorMessage.indexOf("401") != -1) { 
			// need to show sign dialog
			History.newItem("login");
		} else {
			Window.alert("Failed to perform action: " + action + " due to [" + errorMessage + "]");
		}
	}

	public abstract void _onSuccess(T result);
}
