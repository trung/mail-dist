/**
 * 
 */
package org.mdkt.library.client;

import org.mdkt.library.client.event.LoginEvent;
import org.mdkt.library.client.event.LoginEventHandler;
import org.mdkt.library.client.presenter.LoginPresenter;
import org.mdkt.library.client.presenter.Presenter;
import org.mdkt.library.client.view.LoginView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * History management and view transition logic for all modules
 * 
 * @author trung
 * 
 */
public abstract class AbstractAppController implements ValueChangeHandler<String> {

	/**
	 * Navigation constants
	 */
	public static final String HOME = "home";
	public static final String LOGIN = "login";

	
	protected final EventBus eventBus;
	protected HasWidgets container;
	private final LoginServiceAsync rpcLoginService;

	public AbstractAppController(HasWidgets container, EventBus eventBus, LoginServiceAsync rpcLoginService) {
		super();
		this.container = container;
		this.eventBus = eventBus;
		this.rpcLoginService = rpcLoginService;
		_bind();
	}

	private void _bind() {
		History.addValueChangeHandler(this);
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
			@Override
			public void onLogin(LoginEvent event) {
				doLogin();
			}
		});
		bind();
	}
	
	/**
	 * Binding event handler to eventbus
	 */
	protected abstract void bind();

	private void doLogin() {
		// when user click on Sign In button in the LoginView, bind in LoginPresenter
		rpcLoginService.getLoginUrl(Window.Location.getHref(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				Window.Location.assign(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to connect to server. Please try again later.");
			}
		});
		
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// whenever history value changed
		String token = event.getValue();
		if (token != null) {
			if (token.equals(LOGIN)) {
				new LoginPresenter(eventBus, new LoginView()).go(null);
			} else {
				onHistoryChange(token);
			}
		}
	}

	/**
	 * When the history # changed, call {@link Presenter}.go() method
	 * @param token
	 */
	protected abstract void onHistoryChange(String token);
}
