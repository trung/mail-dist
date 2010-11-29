/**
 * 
 */
package org.mdkt.maildist.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author trung
 */
public class SaveDistListMemberCancelledEvent extends GwtEvent<SaveDistListMemberCancelledEvent.Handler> {

	private String distListId;
	
	/**
	 * Handler interface for {@link SaveDistListMemberCancelledEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link SaveDistListMemberCancelledEvent} is fired.
		 * 
		 * @param event
		 *            the {@link SaveDistListMemberCancelledEvent} that was fired
		 */
		void onCancelled(SaveDistListMemberCancelledEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<SaveDistListMemberCancelledEvent.Handler> TYPE;

	public SaveDistListMemberCancelledEvent(String distListId) {
		this.distListId = distListId;
	}
	
	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<SaveDistListMemberCancelledEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<SaveDistListMemberCancelledEvent.Handler>();
		}
		return TYPE;
	}

	@Override
	public final Type<SaveDistListMemberCancelledEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveDistListMemberCancelledEvent.Handler handler) {
		handler.onCancelled(this);
	}
	
	public String getDistListId() {
		return distListId;
	}
}
