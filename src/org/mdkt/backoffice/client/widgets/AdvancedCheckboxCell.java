/**
 * 
 */
package org.mdkt.backoffice.client.widgets;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Allow to customize whether to render/act  
 * 
 * @author trung
 *
 */
public abstract class AdvancedCheckboxCell extends CheckboxCell {
	
	public AdvancedCheckboxCell(boolean isSelectBox) {
		super(isSelectBox);
	}

	public AdvancedCheckboxCell() {
		super();
	}
	
	@Override
	public void onBrowserEvent(Element parent, Boolean value, Object key,
			NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
		if (isEnabled(key)) {
			super.onBrowserEvent(parent, value, key, event, valueUpdater);
		}
	}
	
	@Override
	public void render(Boolean value, Object key, SafeHtmlBuilder sb) {
		if (isEnabled(key)) {
			super.render(value, key, sb);
		}
	}
	
	public abstract boolean isEnabled(Object key);
	
}
