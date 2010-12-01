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
public class AliasDeletedEvent extends GwtEvent<AliasDeletedEvent.Handler> {

	
	/**
	 * Handler interface for {@link AliasDeletedEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link AliasDeletedEvent} is fired.
		 * 
		 * @param event
		 *            the {@link AliasDeletedEvent} that was fired
		 */
		void onAliasDeleted(AliasDeletedEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<AliasDeletedEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<AliasDeletedEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<AliasDeletedEvent.Handler>();
		}
		return TYPE;
	}

	@Override
	public final Type<AliasDeletedEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AliasDeletedEvent.Handler handler) {
		handler.onAliasDeleted(this);
	}
}
