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
public class AddDistListEvent extends GwtEvent<AddDistListEvent.Handler> {

	
	/**
	 * Handler interface for {@link AddDistListEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link AddDistListEvent} is fired.
		 * 
		 * @param event
		 *            the {@link AddDistListEvent} that was fired
		 */
		void onAddDistList(AddDistListEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<AddDistListEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<AddDistListEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<AddDistListEvent.Handler>();
		}
		return TYPE;
	}

	@Override
	public final Type<AddDistListEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddDistListEvent.Handler handler) {
		handler.onAddDistList(this);
	}
}
