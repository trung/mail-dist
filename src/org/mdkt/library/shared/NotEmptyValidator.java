/**
 * 
 */
package org.mdkt.library.shared;

/**
 * @author trung
 *
 */
public class NotEmptyValidator {
	public static boolean isValid(String value) {
		if (value == null) return false;
		return value.trim().length() > 0;
	}
}
