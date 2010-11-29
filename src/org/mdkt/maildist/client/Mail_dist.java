package org.mdkt.maildist.client;

import org.mdkt.library.client.AbstractEntryPoint;

import com.google.gwt.user.client.History;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mail_dist extends AbstractEntryPoint {
	@Override
	protected void initializeModule() {
		// init MVP
		new MailDistAppController(getCenterLayout(), getEventBus(), getLoginService());
	}
	
	@Override
	protected void onStartup() {
		if ("".equals(History.getToken())) {
			History.newItem(MailDistAppController.MD_HOME);
	    } else {
	    	History.fireCurrentHistoryState();
	    }
	}
}
