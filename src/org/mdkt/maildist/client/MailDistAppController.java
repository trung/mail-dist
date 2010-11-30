/**
 * 
 */
package org.mdkt.maildist.client;

import java.util.ArrayList;

import org.mdkt.library.client.AbstractAppController;
import org.mdkt.library.client.LoginServiceAsync;
import org.mdkt.library.client.rpc.DefaultAsyncCallBack;
import org.mdkt.maildist.client.dto.DistList;
import org.mdkt.maildist.client.dto.DistListMember;
import org.mdkt.maildist.client.event.AddDistListEvent;
import org.mdkt.maildist.client.event.AddDistListMemberEvent;
import org.mdkt.maildist.client.event.DistListDeletedEvent;
import org.mdkt.maildist.client.event.DistListMemberDeletedEvent;
import org.mdkt.maildist.client.event.SaveDistListCancelledEvent;
import org.mdkt.maildist.client.event.SaveDistListEvent;
import org.mdkt.maildist.client.event.SaveDistListMemberCancelledEvent;
import org.mdkt.maildist.client.event.SaveDistListMemberEvent;
import org.mdkt.maildist.client.event.ShowDistListMemberEvent;
import org.mdkt.maildist.client.presenter.AddDistListMemberPresenter;
import org.mdkt.maildist.client.presenter.AddDistListPresenter;
import org.mdkt.maildist.client.presenter.DistListMembersPresenter;
import org.mdkt.maildist.client.presenter.DistListPresenter;
import org.mdkt.maildist.client.rpc.DistListService;
import org.mdkt.maildist.client.rpc.DistListServiceAsync;
import org.mdkt.maildist.client.view.AddDistListMemberView;
import org.mdkt.maildist.client.view.AddDistListMemberViewImpl;
import org.mdkt.maildist.client.view.AddDistListViewImpl;
import org.mdkt.maildist.client.view.DistListMembersView;
import org.mdkt.maildist.client.view.DistListMembersViewImpl;
import org.mdkt.maildist.client.view.DistListViewImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * @author trung
 *
 */
public class MailDistAppController extends AbstractAppController {
		
	public static final String MD_HOME = "list";
	public static final String MD_LIST_ADD = "list/add";
	public static final String MD_LIST_MEMBERS = "list/members";

	private final DistListServiceAsync distListService = DistListService.Util.getInstance();
		
	public MailDistAppController(HasWidgets container, EventBus eventBus,
			LoginServiceAsync rpcLoginService) {
		super(container, eventBus, rpcLoginService);
	}

	@Override
	protected void bind() {
		eventBus.addHandler(AddDistListEvent.getType(), new AddDistListEvent.Handler() {
			
			@Override
			public void onAddDistList(AddDistListEvent event) {
				doAddDistList();
			}
		});
		eventBus.addHandler(SaveDistListCancelledEvent.getType(), new SaveDistListCancelledEvent.Handler() {
			
			@Override
			public void onCancelled(SaveDistListCancelledEvent event) {
				History.newItem(MD_HOME);
			}
		});
		eventBus.addHandler(SaveDistListEvent.getType(), new SaveDistListEvent.Handler() {
			@Override
			public void onSaveDistList(SaveDistListEvent event) {
				doSaveDistList(event.getDistList());
			}

		});
		eventBus.addHandler(DistListDeletedEvent.getType(), new DistListDeletedEvent.Handler() {
			
			@Override
			public void onDistListDeleted(DistListDeletedEvent event) {
				History.fireCurrentHistoryState();
			}
		});
		eventBus.addHandler(ShowDistListMemberEvent.getType(),new ShowDistListMemberEvent.Handler() {
			
			@Override
			public void onEditDistList(ShowDistListMemberEvent event) {
				doShowDistListMembers(event.getDistListId());
			}
		});
		eventBus.addHandler(DistListMemberDeletedEvent.getType(), new DistListMemberDeletedEvent.Handler() {
			
			@Override
			public void onDistListMemberDeleted(DistListMemberDeletedEvent event) {
				History.fireCurrentHistoryState();
			}
		});
		eventBus.addHandler(AddDistListMemberEvent.getType(), new AddDistListMemberEvent.Handler() {
			
			@Override
			public void onAddDistListMember(AddDistListMemberEvent event) {
				doAddDistListMemberEvent(event.getDistListId());
			}
		});
		eventBus.addHandler(SaveDistListMemberEvent.getType(), new SaveDistListMemberEvent.Handler() {
			
			@Override
			public void onSaveDistListMember(SaveDistListMemberEvent event) {
				doSaveDistListMember(event.getDistListMembers(), event.getDistListId());
			}
		});
		eventBus.addHandler(SaveDistListMemberCancelledEvent.getType(), new SaveDistListMemberCancelledEvent.Handler() {
			
			@Override
			public void onCancelled(SaveDistListMemberCancelledEvent event) {
				History.fireCurrentHistoryState();
			}
		});
		
	}
	
	private void doSaveDistListMember(
			ArrayList<DistListMember> distListMembers, final String distListId) {
		distListService.updateDistListMember(distListMembers, new DefaultAsyncCallBack<Void>("add distribution list members") {
			@Override
			public void _onSuccess(Void result) {
				History.fireCurrentHistoryState();
			}
		});
	}

	private void doAddDistListMemberEvent(String distListId) {
		AddDistListMemberView view = new AddDistListMemberViewImpl();
		new AddDistListMemberPresenter(distListService, eventBus, view, distListId).go(container);
	}
	
	private void doShowDistListMembers(String distListId) {
		doShowDistListMembers(distListId, true);
	}

	private void doShowDistListMembers(String distListId, boolean issueEvent) {
		History.newItem(MD_LIST_MEMBERS + "/" + distListId, issueEvent);
	}

	private void doSaveDistList(String distListName) {
		DistList distList = new DistList();
		distList.setDistListId(distListName);
		distListService.createDistList(distList, new DefaultAsyncCallBack<Void>("save distribution list") {
			public void _onSuccess(Void result) {
				History.newItem(MD_HOME);
			}
		});
	}
	/**
	 * navigate to list/add
	 */
	private void doAddDistList() {
		History.newItem(MD_LIST_ADD);
	}
	
	@Override
	protected void onHistoryChange(String token) {
		if (MD_HOME.equals(token)) {
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onFailure(Throwable reason) {
				}
				
				@Override
				public void onSuccess() {
					new DistListPresenter(distListService, eventBus, new DistListViewImpl()).go(container);
				}
			});
		} else if (MD_LIST_ADD.equals(token)) {
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onFailure(Throwable reason) {
				}
				
				@Override
				public void onSuccess() {
					new AddDistListPresenter(distListService, eventBus, new AddDistListViewImpl()).go(container);
				}
			});
		} else if (token.startsWith(MD_LIST_MEMBERS)) {
			// extract distListId from the token
			String distListId = token.substring(token.lastIndexOf("/") + 1);
			DistListMembersView view = new DistListMembersViewImpl();
			new DistListMembersPresenter(distListService, eventBus, view, distListId).go(container);		
		}
	}
}
