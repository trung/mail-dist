/**
 * 
 */
package org.mdkt.library.server.support;

import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author trung
 *
 */
public class GwtHandlerMapping extends AbstractDetectingUrlHandlerMapping {

	@Override
	protected String[] determineUrlsForHandler(String beanName) {
		String[] urls = null;
		final Object bean = getApplicationContext().getBean(beanName);
		if (bean instanceof RemoteService) {
			urls = new String[] {"/" + beanName};
		}
		return urls;
	}
}
