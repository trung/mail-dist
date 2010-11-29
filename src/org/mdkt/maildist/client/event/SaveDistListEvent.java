/**
 * 
 */
package org.mdkt.maildist.client.event;

import org.mdkt.maildist.client.dto.DistList;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents a distLists deleted change event.
 * 
 * @author trung
 */
public class SaveDistListEvent extends GwtEvent<SaveDistListEvent.Handler> {

	private String distListName;
	
	/**
	 * Handler interface for {@link SaveDistListEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link SaveDistListEvent} is fired.
		 * 
		 * @param event
		 *            the {@link SaveDistListEvent} that was fired
		 */
		void onSaveDistList(SaveDistListEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<SaveDistListEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<SaveDistListEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<SaveDistListEvent.Handler>();
		}
		return TYPE;
	}

	/**
	 * Creates a selection change event.
	 */
	public SaveDistListEvent(String distList) {
		this.distListName = distList;
	}

	@Override
	public final Type<SaveDistListEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveDistListEvent.Handler handler) {
		handler.onSaveDistList(this);
	}
	
	public String getDistList() {
		return distListName;
	}
}
