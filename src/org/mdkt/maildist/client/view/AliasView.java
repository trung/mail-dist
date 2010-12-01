/**
 * 
 */
package org.mdkt.maildist.client.view;

import java.util.ArrayList;

import org.mdkt.maildist.client.dto.Alias;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

/**
 * @author trung
 *
 */
public interface AliasView {
	public interface Presenter {
		void onAddButtonClicked();
		void onDeleteButtonClicked();
		void onExportCsvButtonClicked();
	}
	
	void setPresenter(Presenter presenter);
	void setRowData(ArrayList<Alias> rowData);
	MultiSelectionModel<Alias> getSelectionModel();
	Widget asWidget();
}
