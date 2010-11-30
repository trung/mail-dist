/**
 * 
 */
package org.mdkt.maildist.client.view;

import java.util.ArrayList;

import org.mdkt.maildist.client.dto.DistList;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;

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
public class DistListViewImpl extends Composite implements DistListView {

	private static DistListViewUiBinder uiBinder = GWT
			.create(DistListViewUiBinder.class);

	@UiTemplate("DistListView.ui.xml")
	interface DistListViewUiBinder extends UiBinder<Widget, DistListViewImpl> {
	}

	interface DistListTableResources extends CellTable.Resources {
		@Source(value = { CellTable.Style.DEFAULT_CSS,
				"../css/DistListTableStyle.css" })
		Style cellTableStyle();
	}

	@UiField
	Button addButton;
	@UiField(provided = true)
	CellTable<DistList> distListTable;
	@UiField(provided = true)
	SimplePager pager;
	@UiField
	Anchor selectNone;
	@UiField
	Anchor selectAll;

	@UiField
	MenuItem deleteDistList;
	@UiField
	MenuItem exportCsv;

	private Presenter presenter;
	private ArrayList<DistList> rowData;
	private MultiSelectionModel<DistList> selectionModel;

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**DistListClassName**>Hello!</g:**DistListClassName> </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public DistListViewImpl() {
		initializeDistListTable();
		initWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	private void bind() {
		selectNone.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (DistList u : selectionModel.getSelectedSet()) {
					selectionModel.setSelected(u, false);
				}
			}
		});
		selectAll.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (DistList object : distListTable.getDisplayedItems()) {
					selectionModel.setSelected(object, true);
				}
			}
		});
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						deleteDistList.setEnabled(selectionModel
								.getSelectedSet().size() > 0);
					}
				});
		deleteDistList.setEnabled(false);
		deleteDistList.setCommand(new Command() {

			@Override
			public void execute() {
				if (presenter != null) {
					presenter.onDeleteButtonClicked();
				}
				deleteDistList.setEnabled(false);
			}
		});

		exportCsv.setCommand(new Command() {

			@Override
			public void execute() {
				if (presenter != null) {
					presenter.onExportCsvButtonClicked();
				}
			}
		});
	}

	private void initializeDistListTable() {
		ProvidesKey<DistList> key = new ProvidesKey<DistList>() {
			@Override
			public Object getKey(DistList item) {
				return item.getDistListId();
			}
		};
		distListTable = new CellTable<DistList>(
				20,
				GWT.<DistListTableResources> create(DistListTableResources.class),
				key);
	    // Create a Pager to control the table.
	    SimplePager.Resources pagerResources = GWT.create(
	        SimplePager.Resources.class);
	    pager = new SimplePager(
	        TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(distListTable);

		distListTable
				.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		selectionModel = new MultiSelectionModel<DistList>(key);
		distListTable.setSelectionModel(selectionModel);
		Column<DistList, Boolean> checkColumn = new Column<DistList, Boolean>(
				new CheckboxCell(true)) {
			@Override
			public Boolean getValue(DistList object) {
				return selectionModel.isSelected(object);
			}
		};
		checkColumn.setFieldUpdater(new FieldUpdater<DistList, Boolean>() {

			@Override
			public void update(int index, DistList object, Boolean value) {
				selectionModel.setSelected(object, value);
			}
		});
		distListTable.addColumn(checkColumn);
		final Column<DistList, String> nameCol = new Column<DistList, String>(
				new ClickableTextCell()) {
			@Override
			public String getValue(DistList object) {
				return object.getDistListId();
			}

			@Override
			public void render(DistList object,
					ProvidesKey<DistList> keyProvider, SafeHtmlBuilder sb) {
				String v = getValue(object);
				if (v != null) {
					sb.append(SafeHtmlUtils
							.fromTrustedString("<a style=\"cursor: pointer; text-decoration: underline\">"
									+ v + "</a>"));
				}
			}
		};
		nameCol.setFieldUpdater(new FieldUpdater<DistList, String>() {
			@Override
			public void update(int index, DistList object, String value) {
				if (presenter != null) {
					presenter.onDistListClicked(object);
				}
			}
		});
		distListTable.addColumn(nameCol, "Name");
		
		distListTable.addColumn(new Column<DistList, String>(new TextCell()) {
			@Override
			public String getValue(DistList object) {
				return String.valueOf(object.getNoOfMembers());
			}
		}, "No of members");
	}

	@Override
	public void setPresenter(DistListView.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setRowData(ArrayList<DistList> rowData) {
		this.rowData = rowData;
		// construct distList table data here
		new ListDataProvider<DistList>(rowData).addDataDisplay(distListTable);
	}

	@UiHandler("addButton")
	void onAddButonClicked(ClickEvent ce) {
		if (presenter != null) {
			presenter.onAddButtonClicked();
		}
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public MultiSelectionModel<DistList> getSelectionModel() {
		return selectionModel;
	}

	void onDeleteButonClicked() {
		if (presenter != null) {
			presenter.onDeleteButtonClicked();
		}
	}
}
