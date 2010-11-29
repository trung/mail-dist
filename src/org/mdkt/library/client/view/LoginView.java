/**
 * 
 */
package org.mdkt.library.client.view;

import org.mdkt.library.client.AbstractEntryPoint;
import org.mdkt.library.client.presenter.LoginPresenter.Display;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author trung
 *
 */
public class LoginView extends Composite implements Display {
	
	@Override
	public void show() {	
		AbstractEntryPoint.LOGIN_DIALOG.center();
		AbstractEntryPoint.LOGIN_DIALOG.show();
	}
	
	@Override
	public HasClickHandlers getLoginButton() {
		return AbstractEntryPoint.LOGIN_DIALOG.getLoginButton();
	}
}
