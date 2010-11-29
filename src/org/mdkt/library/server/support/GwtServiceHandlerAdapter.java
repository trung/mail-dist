package org.mdkt.library.server.support;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtServiceHandlerAdapter extends RemoteServiceServlet implements
		HandlerAdapter {
	private static final long serialVersionUID = -4950877707203083375L;
	
	private final Logger logger = Logger.getLogger(getClass());

	private static ThreadLocal<Object> handlerHolder = new ThreadLocal<Object>();

	public long getLastModified(HttpServletRequest request, Object handler) {
		return -1;
	}

	@Override
	public ModelAndView handle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			logger.debug("[" + request.getRequestURL() + "] handle");
			handlerHolder.set(handler);
			doPost(request, response);
		} finally {
			handlerHolder.set(null);
		}
		return null;
	}

	protected Object getCurrentHandler() {
		return handlerHolder.get();
	}

	public boolean supports(Object handler) {
		return handler instanceof RemoteService;
	}

	@Override
	public String processCall(String payload) throws SerializationException {
		String retVal = null;

		try {
			final Object currentHandler = getCurrentHandler();
			final RPCRequest rpcRequest = RPC.decodeRequest(payload,
					currentHandler.getClass());
			onAfterRequestDeserialized(rpcRequest);
			if (currentHandler instanceof HttpServletRequestAware) {
				((HttpServletRequestAware) currentHandler).setHttpServletRequest(getThreadLocalRequest());
			}
			retVal = RPC.invokeAndEncodeResponse(currentHandler,
					rpcRequest.getMethod(), rpcRequest.getParameters());
		} catch (Throwable t) {
			logger.error("Failed to call RPC", t);
			retVal = RPC.encodeResponseForFailure(null, t);
		}

		return retVal;
	}

	@Override
	public void doUnexpectedFailure(Throwable e) {
		throw new RuntimeException(e);
	}

	@Override
	public ServletContext getServletContext() {
		return null;
	}
}
