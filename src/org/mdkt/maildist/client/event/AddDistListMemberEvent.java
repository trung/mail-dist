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
public class AddDistListMemberEvent extends GwtEvent<AddDistListMemberEvent.Handler> {

	private String distListId;
	
	/**
	 * Handler interface for {@link AddDistListMemberEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link AddDistListMemberEvent} is fired.
		 * 
		 * @param event
		 *            the {@link AddDistListMemberEvent} that was fired
		 */
		void onAddDistListMember(AddDistListMemberEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<AddDistListMemberEvent.Handler> TYPE;

	public AddDistListMemberEvent(String distListId) {
		this.distListId = distListId;
	}

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<AddDistListMemberEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<AddDistListMemberEvent.Handler>();
		}
		return TYPE;
	}

	@Override
	public final Type<AddDistListMemberEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddDistListMemberEvent.Handler handler) {
		handler.onAddDistListMember(this);
	}
	
	public String getDistListId() {
		return distListId;
	}
}
