/**
 * 
 */
package org.mdkt.library.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Main interface for MVP
 * @author trung
 *
 */
public abstract interface Presenter {
	public abstract void go(final HasWidgets container);
}
