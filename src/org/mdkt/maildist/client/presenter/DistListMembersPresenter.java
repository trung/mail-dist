/**
 * 
 */
package org.mdkt.maildist.client.presenter;

import java.util.ArrayList;
import java.util.Set;

import org.mdkt.library.client.presenter.Presenter;
import org.mdkt.library.client.rpc.DefaultAsyncCallBack;
import org.mdkt.maildist.client.MailDistAppController;
import org.mdkt.maildist.client.dto.DistList;
import org.mdkt.maildist.client.dto.DistListMember;
import org.mdkt.maildist.client.event.AddDistListMemberEvent;
import org.mdkt.maildist.client.event.DistListMemberDeletedEvent;
import org.mdkt.maildist.client.rpc.DistListServiceAsync;
import org.mdkt.maildist.client.view.DistListMembersView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.MultiSelectionModel;


/**
 * @author trung
 *
 */
public class DistListMembersPresenter implements Presenter,
		DistListMembersView.Presenter {

	private final DistListServiceAsync distListService;
	private final EventBus eventBus;
	private final DistListMembersView view;
	private final String distListId;
	
	public DistListMembersPresenter(DistListServiceAsync distListMemberService, EventBus eventBus,
			DistListMembersView distListMembersView, String distListId) {
		this.distListService = distListMemberService;
		this.eventBus = eventBus;
		this.view = distListMembersView;
		this.distListId = distListId;
		this.view.setPresenter(this);
	}

	/* (non-Javadoc)
	 * @see org.mdkt.library.client.presenter.Presenter#go(com.google.gwt.distListMember.client.ui.HasWidgets)
	 */
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		fetchDistListMemberDetails();
	}

	private void fetchDistListMemberDetails() {
		distListService.getDistListMembers(distListId, new DefaultAsyncCallBack<ArrayList<DistListMember>>("get distribution list members") {
			@Override
			public void _onSuccess(ArrayList<DistListMember> result) {
				view.setRowData(result);
			}
		});
		
	}

	@Override
	public void onAddButtonClicked() {
		eventBus.fireEvent(new AddDistListMemberEvent(distListId));
	}
	
	@Override
	public void onExportCsvButtonClicked() {
		
	}

	@Override
	public void onDeleteButtonClicked() {
		MultiSelectionModel<DistListMember> selectionModel = view.getSelectionModel();
		Set<DistListMember> selectedDistListMembers = selectionModel.getSelectedSet();
		ArrayList<String> ids = new ArrayList<String>();
		for (DistListMember u : selectedDistListMembers) {
			ids.add((String) selectionModel.getKey(u));
		}
		
		distListService.deleteDistListMembers(ids, new DefaultAsyncCallBack<Void>("delete distribution list members") {
			@Override
			public void _onSuccess(Void result) {
				eventBus.fireEvent(new DistListMemberDeletedEvent(distListId));
			}
		});
	}
}
