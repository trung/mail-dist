/**
 * 
 */
package org.mdkt.backoffice.client.event;

import org.mdkt.backoffice.client.dto.User;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents a users deleted change event.
 * 
 * @author trung
 */
public class EditUserEvent extends GwtEvent<EditUserEvent.Handler> {

	private User user;
	
	/**
	 * Handler interface for {@link EditUserEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link EditUserEvent} is fired.
		 * 
		 * @param event
		 *            the {@link EditUserEvent} that was fired
		 */
		void onEditUser(EditUserEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<EditUserEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<EditUserEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<EditUserEvent.Handler>();
		}
		return TYPE;
	}

	/**
	 * Creates a selection change event.
	 */
	public EditUserEvent(User user) {
		this.user = user;
	}

	@Override
	public final Type<EditUserEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditUserEvent.Handler handler) {
		handler.onEditUser(this);
	}
	
	public User getUser() {
		return user;
	}
}
