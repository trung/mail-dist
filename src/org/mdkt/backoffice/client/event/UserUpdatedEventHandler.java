/**
 * 
 */
package org.mdkt.backoffice.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author trung
 *
 */
public interface UserUpdatedEventHandler extends EventHandler {
	void onUserUpdated(UserUpdatedEvent event);
}
