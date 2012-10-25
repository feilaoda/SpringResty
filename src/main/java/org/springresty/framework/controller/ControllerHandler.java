package org.springresty.framework.controller;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public interface ControllerHandler {
	public HttpResponse excute(HttpRequest request);
}
