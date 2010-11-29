/**
 * 
 */
package org.mdkt.library.server.support;

import javax.servlet.http.HttpServletRequest;

/**
 * @author trung
 *
 */
public interface HttpServletRequestAware {
	void setHttpServletRequest(HttpServletRequest request);
}
