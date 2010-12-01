/**
 * 
 */
package org.mdkt.maildist.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents a distLists deleted change event.
 * 
 * @author trung
 */
public class SaveAliasEvent extends GwtEvent<SaveAliasEvent.Handler> {

	private String email;
	
	/**
	 * Handler interface for {@link SaveAliasEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link SaveAliasEvent} is fired.
		 * 
		 * @param event
		 *            the {@link SaveAliasEvent} that was fired
		 */
		void onSaveAlias(SaveAliasEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<SaveAliasEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<SaveAliasEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<SaveAliasEvent.Handler>();
		}
		return TYPE;
	}

	/**
	 * Creates a selection change event.
	 */
	public SaveAliasEvent(String email) {
		this.email = email;
	}

	@Override
	public final Type<SaveAliasEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveAliasEvent.Handler handler) {
		handler.onSaveAlias(this);
	}
	
	public String getAlias() {
		return email;
	}
}
