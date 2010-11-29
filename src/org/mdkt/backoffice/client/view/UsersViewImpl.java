/**
 * 
 */
package org.mdkt.backoffice.client.view;

import java.util.ArrayList;
import java.util.Date;

import org.mdkt.backoffice.client.dto.User;
import org.mdkt.backoffice.client.widgets.AdvancedCheckboxCell;

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
 * Define the users page. Provide <br/>
 * <ul>
 * 	<li>Create a new user</li>
 * 	<li>Edit an existing user</li>
 * 	<li>Delete selected user(s)</li>
 * 	<li>Export users to CSV</li>
 * </ul>
 * @author trung
 *
 */
public class UsersViewImpl extends Composite implements UsersView {

	private static UsersViewUiBinder uiBinder = GWT
			.create(UsersViewUiBinder.class);
	
	private static final String KEY_POSTFIX = "loggedin";

	@UiTemplate("UsersView.ui.xml")
	interface UsersViewUiBinder extends UiBinder<Widget, UsersViewImpl> {
	}
	
	interface UsersTableResources extends CellTable.Resources {
		@Source(value={CellTable.Style.DEFAULT_CSS, "../css/UsersTableStyle.css"})
		Style cellTableStyle();
	}

	
	@UiField Button addButton;
	@UiField(provided=true) CellTable<User> usersTable;
	@UiField Anchor selectNone;
	@UiField Anchor selectAll;
	@UiField Anchor selectAdmin;
	@UiField Anchor selectPos;
	
	@UiField MenuItem deleteUsers;
	@UiField MenuItem exportCsv;
	
	private Presenter presenter;
	private ArrayList<User> rowData;
	private MultiSelectionModel<User> selectionModel;
	
	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public UsersViewImpl() { 
		initializeUsersTable();
		initWidget(uiBinder.createAndBindUi(this));
		bind();
	}
	
	private void bind() {
		selectNone.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				for (User u : selectionModel.getSelectedSet()) {
					selectionModel.setSelected(u, false);
				}
			}
		});
		selectAll.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				for (User object : usersTable.getDisplayedItems()) {
					if (object.isCurrentUser()) continue;
					selectionModel.setSelected(object, true);
				}
			}
		});
		selectAdmin.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				for (User object : usersTable.getDisplayedItems()) {
					if (object.isCurrentUser()) continue;
					selectionModel.setSelected(object, object.getAuthorities().contains("ADMIN"));
				}
			}
		});
		selectPos.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				for (User object : usersTable.getDisplayedItems()) {
					if (object.isCurrentUser()) continue;
					selectionModel.setSelected(object, object.getAuthorities().contains("REG_USER"));
				}
			}
		});
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				deleteUsers.setEnabled(selectionModel.getSelectedSet().size() > 0);				
			}
		});
		deleteUsers.setEnabled(false);
		deleteUsers.setCommand(new Command() {
			
			@Override
			public void execute() {
				if (presenter != null) {
					presenter.onDeleteButtonClicked();
				}
				deleteUsers.setEnabled(false);
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

	private void initializeUsersTable() {
		ProvidesKey<User> key = new ProvidesKey<User>() {
			@Override
			public Object getKey(User item) {
				// added KEY_POSTFIX to the current user to mark
				return new StringBuffer(item.getUserId()).append(item.isCurrentUser() ? KEY_POSTFIX:"").toString();
			}
		};
		usersTable = new CellTable<User>(20, GWT.<UsersTableResources> create(UsersTableResources.class), key);
		usersTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		selectionModel = new MultiSelectionModel<User>(key);
		usersTable.setSelectionModel(selectionModel);
		Column<User, Boolean> checkColumn = new Column<User, Boolean>(new AdvancedCheckboxCell(true) {
			
			@Override
			public boolean isEnabled(Object key) {
				String keyStr = (String) key;
				return !keyStr.endsWith(KEY_POSTFIX);
			}
		}) {
			@Override
			public Boolean getValue(User object) {
				return selectionModel.isSelected(object);
			}
		};
		checkColumn.setFieldUpdater(new FieldUpdater<User, Boolean>() {
			
			@Override
			public void update(int index, User object, Boolean value) {
				selectionModel.setSelected(object, value);
			}
		});
		usersTable.addColumn(checkColumn);
		final Column<User, String> nameCol = new Column<User, String>(new ClickableTextCell()) {
			@Override
			public String getValue(User object) {
				return object.getDisplayName();
			}
			
			@Override
			public void render(User object, ProvidesKey<User> keyProvider,
					SafeHtmlBuilder sb) {
			    String v = getValue(object);
			    if (v != null) {
			    	sb.append(SafeHtmlUtils.fromTrustedString("<a style=\"cursor: pointer; text-decoration: underline\">" + v + "</a>"));
			    }
			}
		};
		nameCol.setFieldUpdater(new FieldUpdater<User, String>() {
			@Override
			public void update(int index, User object, String value) {
				if (presenter != null) {
					presenter.onUserClicked(object);
				}
			}
		});
		usersTable.addColumn(nameCol, "Name");
		usersTable.addColumn(new Column<User, String>(new TextCell()) {
			@Override
			public String getValue(User object) {
				return object.getEmail();
			}
		}, "Email");
		usersTable.addColumn(new Column<User, String>(new TextCell()) {
			@Override
			public String getValue(User object) {
				return object.isEnabled() ? "Verified" : "Not verified";
			}
		}, "Status");
		usersTable.addColumn(new Column<User, String>(new TextCell()) {
			@Override
			public String getValue(User object) {
				return object.getAuthorities().toString();
			}
		}, "Roles");
		usersTable.addColumn(new Column<User, String>(new TextCell()) {
			@Override
			public String getValue(User object) {
				final Date d = object.getLastSignedIn();
				return d.getTime() == 0 ? "Never logged in" : d.toString();
			}
		}, "Last signed in");
	}
	
	@Override
	public void setPresenter(
			org.mdkt.backoffice.client.view.UsersView.Presenter presenter) {
		this.presenter = presenter;		
	}

	@Override
	public void setRowData(ArrayList<User> rowData) {
		this.rowData = rowData;
		// construct users table data here
		new ListDataProvider<User>(rowData).addDataDisplay(usersTable);
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
	public MultiSelectionModel<User> getSelectionModel() {
		return selectionModel;
	}

	void onDeleteButonClicked() {
		if (presenter != null) {
			presenter.onDeleteButtonClicked();
		}
	}
}
