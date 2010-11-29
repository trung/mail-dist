/**
 * 
 */
package org.mdkt.library.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author trung
 *
 */
public class Header extends Composite {

	private static HeaderUiBinder uiBinder = GWT.create(HeaderUiBinder.class);

	interface HeaderUiBinder extends UiBinder<Widget, Header> {
	}
	
	@UiField HTML userEmail;

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
	public Header() {
		initWidget(uiBinder.createAndBindUi(this));
		// info js object is constructed in support/rpc/login_js.jsp
		String email = "<unknown>";
		try {
			Dictionary info = Dictionary.getDictionary("info");
			email = info.get("email");
		} catch (java.util.MissingResourceException mre) {
			// info is not defined means user may not log in yet
		}
		userEmail.setHTML("Welcome, <b>" + email + "</b>");
	}

}
