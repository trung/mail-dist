/**
 * 
 */
package org.mdkt.backoffice.client.view;

import java.util.Set;

import org.mdkt.backoffice.client.dto.User;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

/**
 * Generic view to display edit user screen
 * @author trung
 *
 */
public interface EditUserView {
	public interface Presenter {
		void onSaveButtonClicked();
		void onCancelButtonClicked();
	}
	
	HasClickHandlers getSaveButton();
	HasClickHandlers getCancelButon();
	HasValue<String> getFirstname();
    HasValue<String> getLastName();
    HasValue<String> getEmailAddress();
    HasValue<Set<String>> getRoles();
    void setErrorEmail(String error);
    
    /**
     * verify the input data
     * @return
     */
    boolean isValid();
    void reset();

    void setPresenter(Presenter presenter);
	void setUser(User user);
	Widget asWidget();
}
