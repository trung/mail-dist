/**
 * 
 */
package org.mdkt.library.client.table;

import com.google.gwt.user.client.ui.HTML;


/**
 * 
 * Define how each column will be treated/rendered for data T 
 * 
 * @author trung
 *
 */
public abstract class ColumnDefinition<T> {
	/**
	 * Render to HTML the data T
	 * @param t
	 * @return
	 */
	public abstract HTML render(T t);
	
	/**
	 * Allow user to click? default return value is false
	 * @return
	 */
	public boolean isClickable() {
		return false;
	}
	
	/**
	 * Allow user to select (providing checkbox)? default return value is false
	 * @return
	 */
	public boolean isSelectable() {
		return false;
	}
}
