/**
 * 
 */
package org.mdkt.maildist.client.view;

import java.util.ArrayList;

import org.mdkt.maildist.client.dto.DistListMember;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

/**
 * @author trung
 *
 */
public interface DistListMembersView {
	public interface Presenter {
		void onAddButtonClicked();
		void onDeleteButtonClicked();
		void onExportCsvButtonClicked();
	}
	
	void setPresenter(Presenter presenter);
	void setRowData(ArrayList<DistListMember> rowData);
	MultiSelectionModel<DistListMember> getSelectionModel();
	Widget asWidget();
}
