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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Define the alias page. Provide <br/>
 * <ul>
 * <li>Create a new user</li>
 * <li>Edit an existing user</li>
 * <li>Delete selected user(s)</li>
 * <li>Export alias to CSV</li>
 * </ul>
 * 
 * @author trung
 * 
 */
public class AddAliasViewImpl extends Composite implements AddAliasView {

	private static AddAliasViewUiBinder uiBinder = GWT
			.create(AddAliasViewUiBinder.class);

	@UiTemplate("AddAliasView.ui.xml")
	interface AddAliasViewUiBinder extends UiBinder<Widget, AddAliasViewImpl> {
	}

	@UiField
	Button addButton;
	@UiField
	Button cancelButton;
	@UiField
	TextBox aliasName;
	@UiField
	Label errorAliasName;
	
	private Presenter presenter;

	public AddAliasViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	private void bind() {
	}

	@Override
	public void setPresenter(AddAliasView.Presenter presenter) {
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
	public HasText getAliasValue() {
		return aliasName;
	}
	
	@Override	
	public Widget asWidget() {
		return this;
	}
	
	@Override
	public void setErrorAlias(String msg) {
		this.errorAliasName.setText(msg);
	}
}
