/**
 * 
 */
package org.mdkt.maildist.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Define the distList page. Provide <br/>
 * <ul>
 * <li>Create a new user</li>
 * <li>Edit an existing user</li>
 * <li>Delete selected user(s)</li>
 * <li>Export distList to CSV</li>
 * </ul>
 * 
 * @author trung
 * 
 */
public class AddDistListMemberViewImpl extends Composite implements AddDistListMemberView {

	private static AddDistListMemberUiBinder uiBinder = GWT
			.create(AddDistListMemberUiBinder.class);

	@UiTemplate("AddDistListMemberView.ui.xml")
	interface AddDistListMemberUiBinder extends UiBinder<Widget, AddDistListMemberViewImpl> {
	}

	@UiField
	Button addButton;
	@UiField
	Button cancelButton;
	@UiField
	TextArea members;
	
	private Presenter presenter;

	public AddDistListMemberViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	private void bind() {
	}

	@Override
	public void setPresenter(AddDistListMemberView.Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("addButton")
	void onAddButonClicked(ClickEvent ce) {
		if (presenter != null) {
			presenter.onAddButtonClicked();
		}
	}

	@UiHandler("cancelButton")
	void onCancelButonClicked(ClickEvent ce) {
		if (presenter != null) {
			presenter.onCancelButtonClicked();
		}
	}	
	
	@Override
	public HasText getMembersRaw() {
		return members;
	}
	
	@Override	
	public Widget asWidget() {
		return this;
	}
}
