/**
 * 
 */
package org.mdkt.backoffice.client.table;

import java.util.ArrayList;
import java.util.List;

import org.mdkt.backoffice.client.dto.User;
import org.mdkt.library.client.table.ColumnDefinition;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;

/**
 * Singleton defines headers and rendering cells for Users table
 * 
 * @author trung
 *
 */
public class UsersTableColumnDefinition {
	private static UsersTableColumnDefinition singleton = null;
	
	private final List<ColumnDefinition<User>> headerDefinitions = new ArrayList<ColumnDefinition<User>>();
	private final List<ColumnDefinition<User>> cellDefinitions = new ArrayList<ColumnDefinition<User>>(); 

	public static UsersTableColumnDefinition instance() {
		if (singleton == null) {
			singleton = new UsersTableColumnDefinition();
		}
		return singleton;
	}
	
	private UsersTableColumnDefinition() {
		headerDefinitions.add(new ColumnDefinition<User>() {
			@Override
			public HTML render(User t) {
				return new HTML("<input type='checkbox' />");
			}
			
			@Override
			public boolean isSelectable() {
				return true;
			}
		});
		headerDefinitions.add(buildHeaderDefinition("Name"));
		headerDefinitions.add(buildHeaderDefinition("Email"));
		headerDefinitions.add(buildHeaderDefinition("Status"));
		headerDefinitions.add(buildHeaderDefinition("Roles"));
		headerDefinitions.add(buildHeaderDefinition("Last signed in"));
		cellDefinitions.add(new ColumnDefinition<User>() {
			@Override
			public HTML render(User t) {
				return new HTML("<input type='checkbox' />");
			}
			
			@Override
			public boolean isSelectable() {
				return true;
			}
		});
		cellDefinitions.add(new ColumnDefinition<User>() {
			@Override
			public HTML render(User t) {
				return new HTML(SafeHtmlUtils.fromString(t.getFirstname() + " " + t.getLastname()));
			}
			
			@Override
			public boolean isClickable() {
				return true;
			}
		});
		cellDefinitions.add(new ColumnDefinition<User>() {
			@Override
			public HTML render(User t) {
				return new HTML(SafeHtmlUtils.fromString(t.getEmail()));
			}			
		});
		cellDefinitions.add(new ColumnDefinition<User>() {
			@Override
			public HTML render(User t) {
				return new HTML(SafeHtmlUtils.fromString(String.valueOf(t.isEnabled())));
			}			
		});
		cellDefinitions.add(new ColumnDefinition<User>() {
			@Override
			public HTML render(User t) {
				return new HTML(SafeHtmlUtils.fromString(t.getAuthorities().toString()));
			}			
		});
		cellDefinitions.add(new ColumnDefinition<User>() {
			@Override
			public HTML render(User t) {
				return new HTML(SafeHtmlUtils.fromString(t.getLastSignedIn().toString()));
			}
		});
	}

	public List<ColumnDefinition<User>> getCellDefinitions() {
		return cellDefinitions;
	}
	
	public List<ColumnDefinition<User>> getHeaderDefinitions() {
		return headerDefinitions;
	}
	
	private ColumnDefinition<User> buildHeaderDefinition(final String value) {
		return new ColumnDefinition<User>() {
			@Override
			public HTML render(User t) {
				return new HTML(SafeHtmlUtils.fromString(value));
			}
		};
	}
}
