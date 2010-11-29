/**
 * 
 */
package org.mdkt.library.client;

import org.mdkt.library.client.rpc.DefaultAsyncCallBack;
import org.mdkt.library.client.view.Footer;
import org.mdkt.library.client.view.Header;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Provide basic functionalites for all modules<br/>
 * 
 * <ul>
 * 	<li>Heart beat to server</li>
 *  <li>Session timed out and login redirect</li>
 * </ul>
 * 
 * @author trung
 *
 */
public abstract class AbstractEntryPoint implements EntryPoint {
	
	/**
	 * Added to the first session timeout check to allow for startup time
	 */
	private final int INITIAL_TIMEOUT_PAD = 60000;

	/**
	 * Added to the session timeout check timer.
	 */
	private final int TIMEOUT_PAD = 15000;

	private Timer sessionTimeoutResponseTimer;

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
//	private static final String SERVER_ERROR = "An error occurred while "
//			+ "attempting to contact the server. Please check your network "
//			+ "connection and try again.";

	private final LoginServiceAsync loginService = LoginService.Util.getInstance();
	private final HeartBeatServiceAsync heartBeatService = HeartBeatService.Util
			.getInstance();

	private final EventBus eventBus = new SimpleEventBus();
	
	public static final LoginDialogBox LOGIN_DIALOG = new LoginDialogBox();

	public static final String DEFAULT_LOGIN_DIALOG_LABEL = "Please sign in to continue.";
	
	private DockLayoutPanel centerLayout;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		DockLayoutPanel rootLayout = new DockLayoutPanel(Unit.EM);
		rootLayout.setWidth("100%");
		rootLayout.setHeight("100%");
		centerLayout = new DockLayoutPanel(Unit.EM);
		centerLayout.setWidth("100%");
		centerLayout.setHeight("100%");
		centerLayout.setStyleName("margin10", true);
		
		rootLayout.addNorth(new Header(), 6);
		rootLayout.addSouth(new Footer(), 3);
		rootLayout.add(centerLayout);
		
		RootLayoutPanel.get().add(rootLayout);
		initializeModule();
		// send a ping rpc to check if user signed in
		// if not, then forward to google login page
		initSessionTimers();
	}
	
	/**
	 * Called first in onModuleLoad()
	 */
	protected abstract void initializeModule();
	
	/**
	 * What to do when a module starts up?
	 */
	protected abstract void onStartup();

	protected HasWidgets getCenterLayout() {
		return centerLayout;
	}
	
	protected EventBus getEventBus() {
		return eventBus;
	}
	
	protected LoginServiceAsync getLoginService() {
		return loginService;
	}

	private void initSessionTimers() {
		heartBeatService.getSessionTimeout(new DefaultAsyncCallBack<Integer>("heartBeat") {
			@Override
			public void _onSuccess(Integer timeout) {
				sessionTimeoutResponseTimer = new Timer() {
					@Override
					public void run() {
						checkUserSessionAlive();
					}
				};
				sessionTimeoutResponseTimer.schedule(timeout
						+ INITIAL_TIMEOUT_PAD);
				
				onStartup();
			}
			
			
		});
	}

	private void checkUserSessionAlive() {
		heartBeatService.getSessionTimeout(new AsyncCallback<Integer>() {
			public void onSuccess(Integer timeout) {
				sessionTimeoutResponseTimer.cancel();
				sessionTimeoutResponseTimer.schedule(timeout + TIMEOUT_PAD);
			}

			public void onFailure(Throwable caught) {
				//Session timed out, let's login again
				displayLoginDialog("You session has timed out.");
			}
		});

	}
	
	private void displayLoginDialog(String label) {
		History.newItem("login");
	}
	
	public static class LoginDialogBox extends DialogBox {
		private final Button loginBtn;
		private final Label label;
		
		public LoginDialogBox() {
			super();
			this.setText("Sign in");
			this.setAnimationEnabled(true);
			this.setModal(true);
			this.setGlassEnabled(true);
			this.center();
			loginBtn = new Button("Sign in with Google Account");
			label = new Label(DEFAULT_LOGIN_DIALOG_LABEL);
			
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setBorderWidth(0);
			
			
			hPanel.add(label);
			hPanel.add(loginBtn);
			
			this.add(hPanel);
			
			this.hide();
		}
		
		public void setLabel(String str) {
			this.label.setText(str);
		}
		
		public Button getLoginButton() {
			return loginBtn;
		}
	}

}
