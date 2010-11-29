/**
 * 
 */
package org.mdkt.backoffice.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author trung
 *
 */
public class EditUserCancelledEvent extends GwtEvent<EditUserCancelledEventHandler> {
	public static Type<EditUserCancelledEventHandler> TYPE = new Type<EditUserCancelledEventHandler>();

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<EditUserCancelledEventHandler> getAssociatedType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(EditUserCancelledEventHandler handler) {
		handler.onEditUserCancelled(this);
	}

}
