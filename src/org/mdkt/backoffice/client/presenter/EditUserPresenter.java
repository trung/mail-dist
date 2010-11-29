/**
 * 
 */
package org.mdkt.backoffice.client.presenter;

import org.mdkt.backoffice.client.dto.User;
import org.mdkt.backoffice.client.event.EditUserCancelledEvent;
import org.mdkt.backoffice.client.event.UserUpdatedEvent;
import org.mdkt.backoffice.client.rpc.UsersServiceAsync;
import org.mdkt.backoffice.client.view.EditUserView;
import org.mdkt.library.client.presenter.Presenter;
import org.mdkt.library.client.rpc.DefaultAsyncCallBack;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * @author trung
 *
 */
public class EditUserPresenter implements Presenter, org.mdkt.backoffice.client.view.EditUserView.Presenter {
	
	private final User user;
	private final UsersServiceAsync usersService;
	private final EditUserView view;
	private final EventBus eventBus;
	
	/**
	 * Create a view when register NEW user
	 * @param eventBus
	 * @param usersService
	 * @param view
	 */
	public EditUserPresenter(UsersServiceAsync usersService, EventBus eventBus, EditUserView view) {
		this.eventBus = eventBus;
		this.usersService = usersService;
		this.view = view;
		this.user = new User();
		bind();
	}
	
	public EditUserPresenter(UsersServiceAsync usersService, EventBus eventBus, EditUserView view, User user) {
		this.eventBus = eventBus;
		this.usersService = usersService;
		this.view = view;
		this.user = user;
		bind();
		view.setUser(user);
	}
	
	private void bind() {
		view.reset();
		view.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				onSaveButtonClicked();
			}
		});
		view.getCancelButon().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onCancelButtonClicked();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.mdkt.library.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets)
	 */
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
	
	@Override
	public void onCancelButtonClicked() {
		eventBus.fireEvent(new EditUserCancelledEvent());	
	}
	
	@Override
	public void onSaveButtonClicked() {
		if (view.isValid()) {
			if (user.getUserId() == null) { // new user
				usersService.getAcceptedUser(view.getEmailAddress().getValue(), new DefaultAsyncCallBack<String>("getAcceptedUser") {
					@Override
					public void _onSuccess(String googleUserId) {
						if (googleUserId == null) {
							view.setErrorEmail("User hasn't been accepted to use our application yet.");
						} else {
							user.setUserId(googleUserId);
							saveUser();
						}
					}
				});
			} else {
				saveUser();
			}
		}
	}
	
	private void saveUser() {
		user.setFirstname(view.getFirstname().getValue());
		user.setLastname(view.getLastName().getValue());
		user.setEmail(view.getEmailAddress().getValue());
		user.setAuthorities(view.getRoles().getValue());
		
		usersService.updateUser(user, new AsyncCallback<User>() {
			
			@Override
			public void onSuccess(User updatedUser) {
				eventBus.fireEvent(new UserUpdatedEvent(updatedUser));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error updating user");
			}
		});
	}

}
