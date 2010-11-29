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
public class ShowDistListMemberEvent extends GwtEvent<ShowDistListMemberEvent.Handler> {

	private String distListId;
	
	/**
	 * Handler interface for {@link ShowDistListMemberEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link ShowDistListMemberEvent} is fired.
		 * 
		 * @param event
		 *            the {@link ShowDistListMemberEvent} that was fired
		 */
		void onEditDistList(ShowDistListMemberEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<ShowDistListMemberEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<ShowDistListMemberEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<ShowDistListMemberEvent.Handler>();
		}
		return TYPE;
	}

	/**
	 * Creates a selection change event.
	 */
	public ShowDistListMemberEvent(String distListId) {
		this.distListId = distListId;
	}

	@Override
	public final Type<ShowDistListMemberEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShowDistListMemberEvent.Handler handler) {
		handler.onEditDistList(this);
	}
	
	public String getDistListId() {
		return distListId;
	}
}
