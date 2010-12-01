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
public class AddAliasEvent extends GwtEvent<AddAliasEvent.Handler> {

	
	/**
	 * Handler interface for {@link AddAliasEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link AddAliasEvent} is fired.
		 * 
		 * @param event
		 *            the {@link AddAliasEvent} that was fired
		 */
		void onAddAlias(AddAliasEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<AddAliasEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<AddAliasEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<AddAliasEvent.Handler>();
		}
		return TYPE;
	}

	@Override
	public final Type<AddAliasEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddAliasEvent.Handler handler) {
		handler.onAddAlias(this);
	}
}
