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
public class SaveDistListCancelledEvent extends GwtEvent<SaveDistListCancelledEvent.Handler> {

	
	/**
	 * Handler interface for {@link SaveDistListCancelledEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link SaveDistListCancelledEvent} is fired.
		 * 
		 * @param event
		 *            the {@link SaveDistListCancelledEvent} that was fired
		 */
		void onCancelled(SaveDistListCancelledEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<SaveDistListCancelledEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<SaveDistListCancelledEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<SaveDistListCancelledEvent.Handler>();
		}
		return TYPE;
	}

	@Override
	public final Type<SaveDistListCancelledEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveDistListCancelledEvent.Handler handler) {
		handler.onCancelled(this);
	}
}
