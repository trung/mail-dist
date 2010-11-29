/**
 * 
 */
package org.mdkt.backoffice.client.event;

import org.mdkt.backoffice.client.dto.User;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * Fired when user gets updated in the user edit screen
 * @author trung
 *
 */
public class UserUpdatedEvent extends GwtEvent<UserUpdatedEventHandler> {
	public static Type<UserUpdatedEventHandler> TYPE = new Type<UserUpdatedEventHandler>();

	private final User updatedUser;
	
	public UserUpdatedEvent(User updatedUser) {
		this.updatedUser = updatedUser;
	}

	public User getUpdatedUser() {
		return updatedUser;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<UserUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(UserUpdatedEventHandler handler) {
		handler.onUserUpdated(this);
	}

}
