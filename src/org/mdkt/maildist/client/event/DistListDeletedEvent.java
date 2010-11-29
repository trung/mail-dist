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
public class DistListDeletedEvent extends GwtEvent<DistListDeletedEvent.Handler> {

	
	/**
	 * Handler interface for {@link DistListDeletedEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link DistListDeletedEvent} is fired.
		 * 
		 * @param event
		 *            the {@link DistListDeletedEvent} that was fired
		 */
		void onDistListDeleted(DistListDeletedEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<DistListDeletedEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<DistListDeletedEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<DistListDeletedEvent.Handler>();
		}
		return TYPE;
	}

	@Override
	public final Type<DistListDeletedEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DistListDeletedEvent.Handler handler) {
		handler.onDistListDeleted(this);
	}
}
