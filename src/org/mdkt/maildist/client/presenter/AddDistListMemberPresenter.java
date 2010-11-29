/**
 * 
 */
package org.mdkt.maildist.client.presenter;

import java.util.ArrayList;

import org.mdkt.library.client.presenter.Presenter;
import org.mdkt.maildist.client.dto.DistListMember;
import org.mdkt.maildist.client.event.SaveDistListMemberCancelledEvent;
import org.mdkt.maildist.client.event.SaveDistListMemberEvent;
import org.mdkt.maildist.client.rpc.DistListServiceAsync;
import org.mdkt.maildist.client.view.AddDistListMemberView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * @author trung
 * 
 */
public class AddDistListMemberPresenter implements Presenter, AddDistListMemberView.Presenter {

	private final DistListServiceAsync distListService;
	private final EventBus eventBus;
	private final AddDistListMemberView view;
	private final String distListId;

	public AddDistListMemberPresenter(DistListServiceAsync userService,
			EventBus eventBus, AddDistListMemberView addDistListMemberView, String distListId) {
		this.distListService = userService;
		this.eventBus = eventBus;
		this.view = addDistListMemberView;
		this.distListId = distListId;
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
		// parsing the raw data into emails and names
		ArrayList<DistListMember> members = new ArrayList<DistListMember>();
		String raw = view.getMembersRaw().getText();
		String[] emails = raw.split(",");
		if (emails != null && emails.length > 0) {
			for (String token : emails) {
				StringBuffer email = new StringBuffer(token.trim());
				if (email.length() > 0) {
					int id = email.lastIndexOf(" ");
					String e;
					String n = "";
					if (id > -1) { // has name
						e = email.substring(id + 1);
						n = email.substring(0, id).replaceAll("\"", "").trim();						
					} else {
						e = email.toString();
					}					
					DistListMember m = new DistListMember();
					m.setDistListId(distListId);
					m.setDistListMemberId(distListId + e);
					m.setEmail(e);
					m.setName(n);
					members.add(m);
				}
			}
		}
		eventBus.fireEvent(new SaveDistListMemberEvent(distListId, members));
	}

	@Override
	public void onCancelButtonClicked() {
		eventBus.fireEvent(new SaveDistListMemberCancelledEvent(distListId));
	}
}
