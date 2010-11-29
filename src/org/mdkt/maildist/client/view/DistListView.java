/**
 * 
 */
package org.mdkt.maildist.client.view;

import java.util.ArrayList;

import org.mdkt.maildist.client.dto.DistList;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

/**
 * @author trung
 *
 */
public interface DistListView {
	public interface Presenter {
		void onAddButtonClicked();
		void onDeleteButtonClicked();
		/**
		 * Display member list
		 * @param clickedDistList
		 */
		void onDistListClicked(DistList clickedDistList);
		void onExportCsvButtonClicked();
	}
	
	void setPresenter(Presenter presenter);
	void setRowData(ArrayList<DistList> rowData);
	MultiSelectionModel<DistList> getSelectionModel();
	Widget asWidget();
}
