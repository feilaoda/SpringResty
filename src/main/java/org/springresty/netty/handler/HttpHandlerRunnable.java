package org.springresty.netty.handler;

import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;
import org.springresty.framework.controller.ControllerHandler;
import org.springresty.netty.http.router.HandlerRouter;


public class HttpHandlerRunnable implements Runnable {
	private final HandlerRouter router;
	private final HttpRequest request;
	private final ChannelHandlerContext ctx;
	private long guid;

	public HttpRequest getRequest() {
		return request;
	}

	public HttpHandlerRunnable(ChannelHandlerContext ctx, HttpRequest request,
			HandlerRouter router) {
		this.request = request;
		this.ctx = ctx;
		this.router = router;

		guid = System.currentTimeMillis();
	}

	public void run() {
		// TODO Auto-generated method stub
		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(
				request.getUri());
		
		ControllerHandler handler = router.match(request.getMethod(), queryStringDecoder.getPath());

		// 检查ACCESS TOKEN，验证权限
		if(handler == null){
			 sendError(ctx, HttpResponseStatus.NOT_FOUND);
			 return;
		}
		try {
			HttpResponse response = handler.excute(request); // handler.handleMessage(request);

			ChannelFuture future = ctx.getChannel().write(response);
			boolean keepAlive = isKeepAlive(request);

			if (!keepAlive) {
				future.addListener(ChannelFutureListener.CLOSE);
//				LogUtils.info(HttpHandlerRunnable.class, "SERVER CLOSED " + guid + " CONNECTION");
			}
		}catch (Exception e) {
			e.printStackTrace();
			sendError(e, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	
		
	}
	
	private HttpResponse sendError(Exception e, HttpResponseStatus status) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
		response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
		response.setHeader(HttpHeaders.Names.CONNECTION, "close");
		e.printStackTrace();
		String error = "Failure: " + e.getClass().getName() + "\r\n" + e.getMessage() + "\r\n";
		response.setContent(ChannelBuffers.copiedBuffer(error, CharsetUtil.UTF_8));
		response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, error.length());
		return response;
	}

	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
		response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");

		ctx.getChannel().write(response)
				.addListener(ChannelFutureListener.CLOSE);
	}
}
