/**
 * 
 */
package org.mdkt.backoffice.client.presenter;

import java.util.ArrayList;
import java.util.Set;

import org.mdkt.backoffice.client.dto.User;
import org.mdkt.backoffice.client.event.AddUserEvent;
import org.mdkt.backoffice.client.event.EditUserEvent;
import org.mdkt.backoffice.client.event.UsersDeletedEvent;
import org.mdkt.backoffice.client.rpc.UsersServiceAsync;
import org.mdkt.backoffice.client.view.UsersView;
import org.mdkt.library.client.presenter.Presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.MultiSelectionModel;

/**
 * @author trung
 *
 */
public class UsersPresenter implements Presenter,
		org.mdkt.backoffice.client.view.UsersView.Presenter {

	private final UsersServiceAsync usersService;
	private final EventBus eventBus;
	private final UsersView view;
	
	public UsersPresenter(UsersServiceAsync userService, EventBus eventBus,
			UsersView usersView) {
		this.usersService = userService;
		this.eventBus = eventBus;
		this.view = usersView;
		this.view.setPresenter(this);
	}

	/* (non-Javadoc)
	 * @see org.mdkt.library.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasWidgets)
	 */
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		fetchUserDetails();
	}

	private void fetchUserDetails() {
		usersService.getUserDetails(new AsyncCallback<ArrayList<User>>() {
			@Override
			public void onSuccess(ArrayList<User> result) {
				view.setRowData(result);				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching users");
			}
		});
		
	}

	@Override
	public void onAddButtonClicked() {
		eventBus.fireEvent(new AddUserEvent());
	}
	
	@Override
	public void onExportCsvButtonClicked() {
		
	}

	@Override
	public void onDeleteButtonClicked() {
		MultiSelectionModel<User> selectionModel = view.getSelectionModel();
		Set<User> selectedUsers = selectionModel.getSelectedSet();
		ArrayList<String> ids = new ArrayList<String>();
		for (User u : selectedUsers) {
			ids.add((String) selectionModel.getKey(u));
		}
		
		// rpc here
		usersService.deleteUsers(ids, new AsyncCallback<ArrayList<User>>() {
			@Override
			public void onSuccess(ArrayList<User> result) {
				eventBus.fireEvent(new UsersDeletedEvent());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error delete users");
			}
		});
	}

	@Override
	public void onUserClicked(User clickedUser) {
		eventBus.fireEvent(new EditUserEvent(clickedUser));
	}

}
