/**
 * 
 */
package org.mdkt.backoffice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents a users deleted change event.
 * 
 * @author trung
 */
public class UsersDeletedEvent extends GwtEvent<UsersDeletedEvent.Handler> {

	/**
	 * Handler interface for {@link UsersDeletedEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link UsersDeletedEvent} is fired.
		 * 
		 * @param event
		 *            the {@link UsersDeletedEvent} that was fired
		 */
		void onUsersDeleted(UsersDeletedEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<UsersDeletedEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<UsersDeletedEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<UsersDeletedEvent.Handler>();
		}
		return TYPE;
	}

	/**
	 * Creates a selection change event.
	 */
	public UsersDeletedEvent() {
	}

	@Override
	public final Type<UsersDeletedEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UsersDeletedEvent.Handler handler) {
		handler.onUsersDeleted(this);
	}
}
