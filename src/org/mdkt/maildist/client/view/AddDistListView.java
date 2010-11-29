/**
 * 
 */
package org.mdkt.maildist.client.view;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author trung
 *
 */
public interface AddDistListView {
	public interface Presenter {
		void onAddButtonClicked();
		void onCancelButtonClicked();
	}
	
	void setPresenter(Presenter p);
	HasText getDistListValue();
	
	Widget asWidget();
	void setErrorDistListName(String msg);
}
