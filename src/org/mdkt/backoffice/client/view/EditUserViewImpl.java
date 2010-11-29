/**
 * 
 */
package org.mdkt.backoffice.client.view;

import java.util.HashSet;
import java.util.Set;

import org.mdkt.backoffice.client.BackOfficeAppController;
import org.mdkt.backoffice.client.dto.User;
import org.mdkt.library.shared.NotEmptyValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author trung
 *
 */
public class EditUserViewImpl extends Composite implements EditUserView {
	
	private static UsersViewUiBinder uiBinder = GWT.create(UsersViewUiBinder.class);

	@UiTemplate("EditUserView.ui.xml")
	interface UsersViewUiBinder extends UiBinder<Widget, EditUserViewImpl> {
	}
	
	@UiField Button saveButton;
	@UiField Button cancelButton;
	@UiField TextBox email;
	@UiField TextBox firstname;
	@UiField TextBox lastname;
	@UiField Label errorEmail;
	@UiField Label errorFirstname;
	@UiField Label errorLastname;
	@UiField VerticalPanel roles;
	
	@UiField HorizontalPanel breadCumb;
	
	private Presenter presenter;
	
	public EditUserViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		breadCumb.add(new Hyperlink("users", BackOfficeAppController.BO_USERS));
		breadCumb.add(new HTML("&nbsp;&raquo;&nbsp;"));
		breadCumb.add(new Label("new user"));		
	}
	
	@Override
	public void reset() {
		email.setText("");
		firstname.setText("");
		lastname.setText("");
		errorEmail.setText("");
		errorFirstname.setText("");
		errorLastname.setText("");
		int count = roles.getWidgetCount();
		for (int i = 0; i < count; i++) {
			Widget w = roles.getWidget(i);
			if (w instanceof CheckBox) {
				CheckBox r  = (CheckBox) w;
				if (r.getValue() && r.isEnabled()) {
					r.setValue(false);
				}
			}
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setUser(User user) {
		breadCumb.remove(breadCumb.getWidgetCount() - 1);
		breadCumb.add(new Label(user.getDisplayName()));
		email.setText(user.getEmail());
		email.setEnabled(false);
		firstname.setText(user.getFirstname());
		lastname.setText(user.getLastname());
		int count = roles.getWidgetCount();
		for (int i = 0; i < count; i++) {
			Widget w = roles.getWidget(i);
			if (w instanceof CheckBox) {
				CheckBox c = (CheckBox) w;
				c.setValue(user.getAuthorities().contains(c.getText()));
			}
		}
	}
	
	@Override
	public boolean isValid() {
		return isValid(email, errorEmail)
			&  isValid(firstname, errorFirstname)
			&  isValid(lastname, errorLastname);
	}
	
	private boolean isValid(HasValue<String> value, Label error) {
		error.setText("");
		if (!NotEmptyValidator.isValid(value.getValue())) {
			error.setText("This field is required");
			return false;
		}
		return true;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}

	@Override
	public HasClickHandlers getCancelButon() {
		return cancelButton;
	}

	@Override
	public HasValue<String> getFirstname() {
		return firstname;
	}

	@Override
	public HasValue<String> getLastName() {
		return lastname;
	}

	@Override
	public HasValue<String> getEmailAddress() {
		return email;
	}

	@Override
	public HasValue<Set<String>> getRoles() {
		final Set<String> values = new HashSet<String>();
		int count = roles.getWidgetCount();
		for (int i = 0; i < count; i++) {
			Widget w = roles.getWidget(i);
			if (w instanceof CheckBox) {
				if (((CheckBox) w).getValue()) {
					values.add(((CheckBox)w).getText());
				}
			}
		}
		return new HasValue<Set<String>>() {

			@Override
			public HandlerRegistration addValueChangeHandler(
					ValueChangeHandler<Set<String>> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public Set<String> getValue() {
				return values;
			}

			@Override
			public void setValue(Set<String> value) {				
			}

			@Override
			public void setValue(Set<String> value, boolean fireEvents) {
			}
			
		};
	}

	@Override
	public void setErrorEmail(String error) {
		errorEmail.setText(error);
	}
	
	@Override
	public Widget asWidget() {
		return this;
	}
}
