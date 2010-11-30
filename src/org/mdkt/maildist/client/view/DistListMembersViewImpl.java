/**
 * 
 */
package org.mdkt.maildist.client.view;

import java.util.ArrayList;

import org.mdkt.maildist.client.MailDistAppController;
import org.mdkt.maildist.client.dto.DistListMember;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;

/**
 * Define the distListMembers page. Provide <br/>
 * <ul>
 * <li>Create a new user</li>
 * <li>Edit an existing user</li>
 * <li>Delete selected user(s)</li>
 * <li>Export distListMembers to CSV</li>
 * </ul>
 * 
 * @author trung
 * 
 */
public class DistListMembersViewImpl extends Composite implements
		DistListMembersView {

	private static DistListMembersViewUiBinder uiBinder = GWT
			.create(DistListMembersViewUiBinder.class);

	@UiTemplate("DistListMembersView.ui.xml")
	interface DistListMembersViewUiBinder extends
			UiBinder<Widget, DistListMembersViewImpl> {
	}

	interface DistListMembersTableResources extends CellTable.Resources {
		@Source(value = { CellTable.Style.DEFAULT_CSS,
				"../css/DistListTableStyle.css" })
		Style cellTableStyle();
	}

	@UiField
	Button addButton;
	@UiField(provided = true)
	CellTable<DistListMember> distListMembersTable;
	@UiField(provided = true)
	SimplePager pager;
	@UiField
	Anchor selectNone;
	@UiField
	Anchor selectAll;
	@UiField
	MenuItem deleteDistListMembers;
	@UiField
	MenuItem exportCsv;
	@UiField(provided = true)
	Hyperlink backButton;

	private Presenter presenter;
	private ArrayList<DistListMember> rowData;
	private MultiSelectionModel<DistListMember> selectionModel;

	public DistListMembersViewImpl() {
		initializeDistListMembersTable();
		initWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	private void bind() {
		selectNone.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (DistListMember u : selectionModel.getSelectedSet()) {
					selectionModel.setSelected(u, false);
				}
			}
		});
		selectAll.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for (DistListMember object : distListMembersTable
						.getDisplayedItems()) {
					selectionModel.setSelected(object, true);
				}
			}
		});
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						deleteDistListMembers.setEnabled(selectionModel
								.getSelectedSet().size() > 0);
					}
				});
		deleteDistListMembers.setEnabled(false);
		deleteDistListMembers.setCommand(new Command() {

			@Override
			public void execute() {
				if (presenter != null) {
					presenter.onDeleteButtonClicked();
				}
				deleteDistListMembers.setEnabled(false);
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

	private void initializeDistListMembersTable() {
		ProvidesKey<DistListMember> key = new ProvidesKey<DistListMember>() {
			@Override
			public Object getKey(DistListMember item) {
				return item.getDistListMemberId();
			}
		};
		distListMembersTable = new CellTable<DistListMember>(
				20,
				GWT.<DistListMembersTableResources> create(DistListMembersTableResources.class),
				key);
	    // Create a Pager to control the table.
	    SimplePager.Resources pagerResources = GWT.create(
	        SimplePager.Resources.class);
	    pager = new SimplePager(
	        TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(distListMembersTable);

		distListMembersTable
				.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		selectionModel = new MultiSelectionModel<DistListMember>(key);
		distListMembersTable.setSelectionModel(selectionModel);
		Column<DistListMember, Boolean> checkColumn = new Column<DistListMember, Boolean>(
				new CheckboxCell(true)) {
			@Override
			public Boolean getValue(DistListMember object) {
				return selectionModel.isSelected(object);
			}
		};
		checkColumn
				.setFieldUpdater(new FieldUpdater<DistListMember, Boolean>() {

					@Override
					public void update(int index, DistListMember object,
							Boolean value) {
						selectionModel.setSelected(object, value);
					}
				});
		distListMembersTable.addColumn(checkColumn);
		final Column<DistListMember, String> nameCol = new Column<DistListMember, String>(
				new TextCell()) {
			@Override
			public String getValue(DistListMember object) {
				return object.getName();
			}
		};
		distListMembersTable.addColumn(nameCol, "Name");
		distListMembersTable.addColumn(new Column<DistListMember, String>(
				new TextCell()) {
			@Override
			public String getValue(DistListMember object) {
				return object.getEmail();
			}
		}, "Email");
		backButton = new Hyperlink(SafeHtmlUtils.fromTrustedString("&laquo;&nbsp;back"), MailDistAppController.MD_HOME);
	}

	@Override
	public void setPresenter(DistListMembersView.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setRowData(ArrayList<DistListMember> rowData) {
		this.rowData = rowData;
		// construct distListMembers table data here
		new ListDataProvider<DistListMember>(rowData)
				.addDataDisplay(distListMembersTable);
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
	public MultiSelectionModel<DistListMember> getSelectionModel() {
		return selectionModel;
	}

	void onDeleteButonClicked() {
		if (presenter != null) {
			presenter.onDeleteButtonClicked();
		}
	}
}
