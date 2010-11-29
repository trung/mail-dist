/**
 * 
 */
package org.mdkt.backoffice.client;

import org.mdkt.backoffice.client.dto.User;
import org.mdkt.backoffice.client.event.AddUserEvent;
import org.mdkt.backoffice.client.event.AddUserEventHandler;
import org.mdkt.backoffice.client.event.EditUserCancelledEvent;
import org.mdkt.backoffice.client.event.EditUserCancelledEventHandler;
import org.mdkt.backoffice.client.event.EditUserEvent;
import org.mdkt.backoffice.client.event.UserUpdatedEvent;
import org.mdkt.backoffice.client.event.UserUpdatedEventHandler;
import org.mdkt.backoffice.client.event.UsersDeletedEvent;
import org.mdkt.backoffice.client.presenter.EditUserPresenter;
import org.mdkt.backoffice.client.presenter.UsersPresenter;
import org.mdkt.backoffice.client.rpc.UsersService;
import org.mdkt.backoffice.client.rpc.UsersServiceAsync;
import org.mdkt.backoffice.client.view.BackOfficeHome;
import org.mdkt.backoffice.client.view.EditUserView;
import org.mdkt.backoffice.client.view.EditUserViewImpl;
import org.mdkt.backoffice.client.view.UsersView;
import org.mdkt.backoffice.client.view.UsersViewImpl;
import org.mdkt.library.client.AbstractAppController;
import org.mdkt.library.client.LoginServiceAsync;
import org.mdkt.library.client.presenter.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * @author trung
 *
 */
public class BackOfficeAppController extends AbstractAppController {
	
	/**
	 * Navigation Constants, mostly used for AppController to provide navigation logic
	 */
	public static final String BO_USERS = "Users";
	public static final String BO_ADD_USER = "Users/add";
	public static final String BO_EDIT_USER = "Users/edit";

	private final UsersServiceAsync userService = UsersService.Util.getInstance();
	
	private UsersView usersView = null;
	
	private final BackOfficeHome home;
	
	public BackOfficeAppController(HasWidgets container, EventBus eventBus,
			LoginServiceAsync rpcLoginService) {
		super(container, eventBus, rpcLoginService);
		this.home = new BackOfficeHome();
		container.add(home);		
	}

	@Override
	protected void onHistoryChange(String token) {
		if (BO_ADD_USER.equals(token)) {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					// select users page
					HasWidgets userTabContainer = home.selectTab(BO_USERS);			
					
					Presenter p = new EditUserPresenter(userService, eventBus, new EditUserViewImpl());
					p.go(userTabContainer);
					
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
				}
			});
		} else if (BO_USERS.equals(token)) {
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					HasWidgets usersTabContainer = home.selectTab(BO_USERS);
					if (usersView == null) {
						usersView = new UsersViewImpl();
					}
					Presenter p = new UsersPresenter(userService, eventBus, usersView);
					p.go(usersTabContainer);					
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
				}
			});
		}		
	}
	
	@Override
	protected void bind() {
		eventBus.addHandler(AddUserEvent.TYPE, new AddUserEventHandler() {
			
			@Override
			public void onAddUser(AddUserEvent event) {
				doAddNewUser();				
			}
		});
		eventBus.addHandler(EditUserCancelledEvent.TYPE, new EditUserCancelledEventHandler() {
			
			@Override
			public void onEditUserCancelled(EditUserCancelledEvent event) {
				doCancelEditUser();
			}
		});
		eventBus.addHandler(UserUpdatedEvent.TYPE, new UserUpdatedEventHandler() {
			
			@Override
			public void onUserUpdated(UserUpdatedEvent event) {
				doUserUpdated(event.getUpdatedUser());
			}
		});
		eventBus.addHandler(UsersDeletedEvent.getType(), new UsersDeletedEvent.Handler() {
			
			@Override
			public void onUsersDeleted(UsersDeletedEvent event) {
				doUsersDeleted();
			}
		});
		eventBus.addHandler(EditUserEvent.getType(), new EditUserEvent.Handler() {
			
			@Override
			public void onEditUser(EditUserEvent event) {
				doEditUser(event.getUser());
			}
		});
	}
	
	private void doEditUser(User user) {
		History.newItem(BO_EDIT_USER, false);
		// select users page
		HasWidgets userTabContainer = home.selectTab(BO_USERS);			
		EditUserView view = new EditUserViewImpl();
		Presenter p = new EditUserPresenter(userService, eventBus, view, user);
		p.go(userTabContainer);		
	}
	
	private void doUsersDeleted() {
		History.fireCurrentHistoryState();
	}
	
	private void doUserUpdated(User updatedUser) {
		History.newItem(BO_USERS);
	}
	
	private void doCancelEditUser() {
		History.newItem(BO_USERS);
	}
	
	private void doAddNewUser() {
		History.newItem(BO_ADD_USER);
	}
}
