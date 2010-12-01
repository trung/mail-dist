/**
 * 
 */
package org.mdkt.maildist.client.presenter;

import java.util.ArrayList;
import java.util.Set;

import org.mdkt.library.client.presenter.Presenter;
import org.mdkt.library.client.rpc.DefaultAsyncCallBack;
import org.mdkt.maildist.client.dto.Alias;
import org.mdkt.maildist.client.event.AddAliasEvent;
import org.mdkt.maildist.client.event.AliasDeletedEvent;
import org.mdkt.maildist.client.rpc.DistListServiceAsync;
import org.mdkt.maildist.client.view.AliasView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.MultiSelectionModel;

/**
 * @author trung
 * 
 */
public class AliasPresenter implements Presenter, AliasView.Presenter {

	private final DistListServiceAsync aliasService;
	private final EventBus eventBus;
	private final AliasView view;

	public AliasPresenter(DistListServiceAsync userService,
			EventBus eventBus, AliasView aliasView) {
		this.aliasService = userService;
		this.eventBus = eventBus;
		this.view = aliasView;
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
		fetchAlias();
	}

	private void fetchAlias() {
		aliasService.getAliasEmails(new DefaultAsyncCallBack<ArrayList<Alias>>("get alias list") {
			@Override
			public void _onSuccess(ArrayList<Alias> result) {
				view.setRowData(result);
			}
		});

	}

	@Override
	public void onAddButtonClicked() {
		eventBus.fireEvent(new AddAliasEvent());
	}

	@Override
	public void onExportCsvButtonClicked() {

	}

	@Override
	public void onDeleteButtonClicked() {
		MultiSelectionModel<Alias> selectionModel = view.getSelectionModel();
		Set<Alias> selectedAlias = selectionModel.getSelectedSet();
		ArrayList<String> ids = new ArrayList<String>();
		for (Alias u : selectedAlias) {
			ids.add((String) selectionModel.getKey(u));
		}

		// rpc here
		aliasService.deleteAliasEmails(ids, new DefaultAsyncCallBack<Void>("delete distribution lists") {
			@Override
			public void _onSuccess(Void result) {
				eventBus.fireEvent(new AliasDeletedEvent());
			}
		});
	}
}
