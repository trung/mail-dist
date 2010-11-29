/**
 * 
 */
package org.mdkt.backoffice.client.view;

import java.util.ArrayList;

import org.mdkt.backoffice.client.dto.User;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

/**
 * @author trung
 *
 */
public interface UsersView {
	public interface Presenter {
		void onAddButtonClicked();
		void onDeleteButtonClicked();
		void onUserClicked(User clickedUser);
		void onExportCsvButtonClicked();
	}
	
	void setPresenter(Presenter presenter);
	void setRowData(ArrayList<User> rowData);
	MultiSelectionModel<User> getSelectionModel();
	Widget asWidget();
}
