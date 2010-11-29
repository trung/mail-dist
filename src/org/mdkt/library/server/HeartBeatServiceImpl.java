package org.mdkt.library.server;

import javax.servlet.http.HttpServletRequest;

import org.mdkt.library.client.HeartBeatService;
import org.mdkt.library.server.support.HttpServletRequestAware;

public class HeartBeatServiceImpl implements HeartBeatService, HttpServletRequestAware {
	
	private HttpServletRequest request;
	
	@Override
	public void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public Integer getSessionTimeout() {
		return request.getSession().getMaxInactiveInterval() * 1000;
	}
}
