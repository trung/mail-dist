/**
 * 
 */
package org.mdkt.maildist.client.presenter;

import java.util.ArrayList;
import java.util.Set;

import org.mdkt.library.client.presenter.Presenter;
import org.mdkt.library.client.rpc.DefaultAsyncCallBack;
import org.mdkt.maildist.client.dto.DistList;
import org.mdkt.maildist.client.event.AddDistListEvent;
import org.mdkt.maildist.client.event.DistListDeletedEvent;
import org.mdkt.maildist.client.event.ShowDistListMemberEvent;
import org.mdkt.maildist.client.rpc.DistListServiceAsync;
import org.mdkt.maildist.client.view.DistListView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.MultiSelectionModel;

/**
 * @author trung
 * 
 */
public class DistListPresenter implements Presenter, DistListView.Presenter {

	private final DistListServiceAsync distListService;
	private final EventBus eventBus;
	private final DistListView view;

	public DistListPresenter(DistListServiceAsync userService,
			EventBus eventBus, DistListView distListView) {
		this.distListService = userService;
		this.eventBus = eventBus;
		this.view = distListView;
		this.view.setPresenter(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mdkt.library.client.presenter.Presenter#go(com.google.gwt.user.client
	 * .ui.HasWidgets)
	 */
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		fetchDistList();
	}

	private void fetchDistList() {
		distListService.getDistLists(new DefaultAsyncCallBack<ArrayList<DistList>>("get distribution list") {
			@Override
			public void _onSuccess(ArrayList<DistList> result) {
				view.setRowData(result);
			}
		});

	}

	@Override
	public void onAddButtonClicked() {
		eventBus.fireEvent(new AddDistListEvent());
	}

	@Override
	public void onExportCsvButtonClicked() {

	}

	@Override
	public void onDeleteButtonClicked() {
		MultiSelectionModel<DistList> selectionModel = view.getSelectionModel();
		Set<DistList> selectedDistList = selectionModel.getSelectedSet();
		ArrayList<String> ids = new ArrayList<String>();
		for (DistList u : selectedDistList) {
			ids.add((String) selectionModel.getKey(u));
		}

		// rpc here
		distListService.deleteDistLists(ids, new DefaultAsyncCallBack<Void>("delete distribution lists") {
			@Override
			public void _onSuccess(Void result) {
				eventBus.fireEvent(new DistListDeletedEvent());
			}
		});
	}

	@Override
	public void onDistListClicked(DistList clickedDistList) {
		eventBus.fireEvent(new ShowDistListMemberEvent(clickedDistList.getDistListId()));
	}

}
