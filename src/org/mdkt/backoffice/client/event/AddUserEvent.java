/**
 * 
 */
package org.mdkt.backoffice.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author trung
 *
 */
public class AddUserEvent extends GwtEvent<AddUserEventHandler> {
	public static Type<AddUserEventHandler> TYPE = new Type<AddUserEventHandler>();

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<AddUserEventHandler> getAssociatedType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(AddUserEventHandler handler) {
		handler.onAddUser(this);
	}

}
