/**
 * 
 */
package org.mdkt.maildist.client.event;

import java.util.ArrayList;

import org.mdkt.maildist.client.dto.DistList;
import org.mdkt.maildist.client.dto.DistListMember;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents a distLists deleted change event.
 * 
 * @author trung
 */
public class SaveDistListMemberEvent extends
		GwtEvent<SaveDistListMemberEvent.Handler> {

	private String distListId;

	private ArrayList<DistListMember> distListMembers;

	/**
	 * Handler interface for {@link SaveDistListMemberEvent} events.
	 */
	public static interface Handler extends EventHandler {

		/**
		 * Called when a {@link SaveDistListMemberEvent} is fired.
		 * 
		 * @param event
		 *            the {@link SaveDistListMemberEvent} that was fired
		 */
		void onSaveDistListMember(SaveDistListMemberEvent event);
	}

	/**
	 * Handler type.
	 */
	private static Type<SaveDistListMemberEvent.Handler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<SaveDistListMemberEvent.Handler> getType() {
		if (TYPE == null) {
			TYPE = new Type<SaveDistListMemberEvent.Handler>();
		}
		return TYPE;
	}

	public SaveDistListMemberEvent(String distListId,
			ArrayList<DistListMember> distListMembers) {
		super();
		this.distListId = distListId;
		this.distListMembers = distListMembers;
	}

	@Override
	public final Type<SaveDistListMemberEvent.Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveDistListMemberEvent.Handler handler) {
		handler.onSaveDistListMember(this);
	}

	public ArrayList<DistListMember> getDistListMembers() {
		return distListMembers;
	}

	public String getDistListId() {
		return distListId;
	}
}
