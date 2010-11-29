/**
 * 
 */
package org.mdkt.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author trung
 *
 */
public class LoginEvent extends GwtEvent<LoginEventHandler> {
	
	public static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();
	
	@Override
	public Type<LoginEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginEventHandler handler) {
		handler.onLogin(this);
	}

}
