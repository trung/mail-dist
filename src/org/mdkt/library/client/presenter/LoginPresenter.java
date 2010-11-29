/**
 * 
 */
package org.mdkt.library.client.presenter;

import org.mdkt.library.client.event.LoginEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * @author trung
 * 
 */
public class LoginPresenter implements Presenter {

	public interface Display {
		HasClickHandlers getLoginButton();
		void show();
	}

	private final EventBus eventBus;
	private final Display display;

	public LoginPresenter(EventBus eventBus, Display display) {
		super();
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}

	public void bind() {
		display.getLoginButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LoginEvent());
				
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mdkt.pos.client.presenter.Presenter#go(com.google.gwt.user.client
	 * .ui.HasWidgets)
	 */
	@Override
	public void go(HasWidgets container) {
		display.show();
	}
	
}
