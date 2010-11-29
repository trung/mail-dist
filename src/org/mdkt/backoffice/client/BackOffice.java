package org.mdkt.backoffice.client;

import org.mdkt.library.client.AbstractEntryPoint;

import com.google.gwt.user.client.History;

/**
 * Backoffice displays all functionalities to manage the system <br/>
 * Dashboard, Items & Catalogs, Locations & Stores, Users
 * Entry point classes define <code>onModuleLoad()</code>.
 * @author trung
 */
public class BackOffice extends AbstractEntryPoint {
	
	@Override
	protected void initializeModule() {
		// init MVP
		new BackOfficeAppController(getCenterLayout(), getEventBus(), getLoginService());
	}
	
	@Override
	protected void onStartup() {
		if ("".equals(History.getToken())) {
			History.newItem(BackOfficeAppController.BO_USERS);
	    } else {
	    	History.fireCurrentHistoryState();
	    }
	}
}
