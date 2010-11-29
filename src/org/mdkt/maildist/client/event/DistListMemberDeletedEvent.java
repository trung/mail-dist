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
public class DistListMemberDeletedEvent extends GwtEvent<DistListMemberDeletedEvent.Handler> {

	private String distListId;
	
	/**
	 * Handler interface for {@link DistListMemberDeletedEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link DistListMemberDeletedEvent} is fired.
		 * 
		 * @param event
		 *            the {@link DistListMemberDeletedEvent} that was fired
		 */
		void onDistListMemberDeleted(DistListMemberDeletedEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<DistListMemberDeletedEvent.Handler> TYPE;

	public DistListMemberDeletedEvent(String distListId) {
		this.distListId = distListId;
	}
	
	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<DistListMemberDeletedEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<DistListMemberDeletedEvent.Handler>();
		}
		return TYPE;
	}

	@Override
	public final Type<DistListMemberDeletedEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DistListMemberDeletedEvent.Handler handler) {
		handler.onDistListMemberDeleted(this);
	}
	
	public String getDistListId() {
		return distListId;
	}
}
