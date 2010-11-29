/**
 * 
 */
package org.mdkt.maildist.client.presenter;

import org.mdkt.library.client.presenter.Presenter;
import org.mdkt.library.client.rpc.DefaultAsyncCallBack;
import org.mdkt.maildist.client.event.SaveDistListCancelledEvent;
import org.mdkt.maildist.client.event.SaveDistListEvent;
import org.mdkt.maildist.client.rpc.DistListServiceAsync;
import org.mdkt.maildist.client.view.AddDistListView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * @author trung
 * 
 */
public class AddDistListPresenter implements Presenter, AddDistListView.Presenter {

	private final DistListServiceAsync distListService;
	private final EventBus eventBus;
	private final AddDistListView view;

	public AddDistListPresenter(DistListServiceAsync userService,
			EventBus eventBus, AddDistListView addDistListView) {
		this.distListService = userService;
		this.eventBus = eventBus;
		this.view = addDistListView;
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
	}

	@Override
	public void onAddButtonClicked() {
		final String distListName = view.getDistListValue().getText();
		distListService.findDistList(distListName, new DefaultAsyncCallBack<Boolean>("find dist list") {
			public void _onSuccess(Boolean result) {
				if (result) {
					view.setErrorDistListName("The distribution list already exists");
				} else {
					eventBus.fireEvent(new SaveDistListEvent(distListName));
				}
			}
		});
	}

	@Override
	public void onCancelButtonClicked() {
		eventBus.fireEvent(new SaveDistListCancelledEvent());
	}
}
