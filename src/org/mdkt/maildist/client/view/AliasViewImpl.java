/**
 * 
 */
package org.mdkt.maildist.client.view;

import java.util.ArrayList;

import org.mdkt.maildist.client.dto.Alias;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
public class AliasViewImpl extends Composite implements AliasView {

	private static AliasViewUiBinder uiBinder = GWT
			.create(AliasViewUiBinder.class);

	@UiTemplate("AliasView.ui.xml")
	interface AliasViewUiBinder extends UiBinder<Widget, AliasViewImpl> {
	}

	interface AliasTableResources extends CellTable.Resources {
		@Source(value = { CellTable.Style.DEFAULT_CSS,
				"../css/DistListTableStyle.css" })
		Style cellTableStyle();
	}

	@UiField
	Button addButton;
	@UiField(provided = true)
	CellTable<Alias> aliasTable;
	@UiField(provided = true)
	SimplePager pager;
	@UiField
	Anchor selectNone;
	@UiField
	Anchor selectAll;

	@UiField
	MenuItem deleteAlias;
	@UiField
	MenuItem exportCsv;

	private Presenter presenter;
	private ArrayList<Alias> rowData;
	private MultiSelectionModel<Alias> selectionModel;

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**AliasClassName**>Hello!</g:**AliasClassName> </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public AliasViewImpl() {
		initializeAliasTable();
		initWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	private void bind() {
		selectNone.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (Alias u : selectionModel.getSelectedSet()) {
					selectionModel.setSelected(u, false);
				}
			}
		});
		selectAll.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (Alias object : aliasTable.getDisplayedItems()) {
					selectionModel.setSelected(object, true);
				}
			}
		});
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						deleteAlias.setEnabled(selectionModel
								.getSelectedSet().size() > 0);
					}
				});
		deleteAlias.setEnabled(false);
		deleteAlias.setCommand(new Command() {

			@Override
			public void execute() {
				if (presenter != null) {
					presenter.onDeleteButtonClicked();
				}
				deleteAlias.setEnabled(false);
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

	private void initializeAliasTable() {
		ProvidesKey<Alias> key = new ProvidesKey<Alias>() {
			@Override
			public Object getKey(Alias item) {
				return item.getAliasId();
			}
		};
		aliasTable = new CellTable<Alias>(
				10,
				GWT.<AliasTableResources> create(AliasTableResources.class),
				key);
	    // Create a Pager to control the table.
	    SimplePager.Resources pagerResources = GWT.create(
	        SimplePager.Resources.class);
	    pager = new SimplePager(
	        TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(aliasTable);

		aliasTable
				.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		selectionModel = new MultiSelectionModel<Alias>(key);
		aliasTable.setSelectionModel(selectionModel);
		Column<Alias, Boolean> checkColumn = new Column<Alias, Boolean>(
				new CheckboxCell(true)) {
			@Override
			public Boolean getValue(Alias object) {
				return selectionModel.isSelected(object);
			}
		};
		checkColumn.setFieldUpdater(new FieldUpdater<Alias, Boolean>() {

			@Override
			public void update(int index, Alias object, Boolean value) {
				selectionModel.setSelected(object, value);
			}
		});
		aliasTable.addColumn(checkColumn);
		aliasTable.addColumn(new Column<Alias, String>(new TextCell()) {
			@Override
			public String getValue(Alias object) {
				return object.getEmail();
			}
		}, "Email Alias");
	}

	@Override
	public void setPresenter(AliasView.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setRowData(ArrayList<Alias> rowData) {
		this.rowData = rowData;
		// construct alias table data here
		new ListDataProvider<Alias>(rowData).addDataDisplay(aliasTable);
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
	public MultiSelectionModel<Alias> getSelectionModel() {
		return selectionModel;
	}

	void onDeleteButonClicked() {
		if (presenter != null) {
			presenter.onDeleteButtonClicked();
		}
	}
}
