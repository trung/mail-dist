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
public class SaveAliasCancelledEvent extends GwtEvent<SaveAliasCancelledEvent.Handler> {

	
	/**
	 * Handler interface for {@link SaveAliasCancelledEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link SaveAliasCancelledEvent} is fired.
		 * 
		 * @param event
		 *            the {@link SaveAliasCancelledEvent} that was fired
		 */
		void onCancelled(SaveAliasCancelledEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<SaveAliasCancelledEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<SaveAliasCancelledEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<SaveAliasCancelledEvent.Handler>();
		}
		return TYPE;
	}

	@Override
	public final Type<SaveAliasCancelledEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveAliasCancelledEvent.Handler handler) {
		handler.onCancelled(this);
	}
}
