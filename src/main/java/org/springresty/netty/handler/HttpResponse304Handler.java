package org.springresty.netty.handler;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;
import org.springresty.utils.SecurityUtils;


import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.NOT_MODIFIED;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpResponse304Handler extends SimpleChannelHandler {
    private static Pattern maxAgePattern = Pattern.compile("max-age=\\d+");
    private ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<String, CacheEntry>();

    

    @Override
    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof MessageEvent && ((MessageEvent)e).getMessage() instanceof HttpResponse) {
            HttpResponse r = (HttpResponse)((MessageEvent)e).getMessage();
            String sha1 = SecurityUtils.toSHA1(r.getContent().array());
            CacheEntry ce = cache.get(sha1);
            
            if (ce == null) {
                cache.putIfAbsent(sha1,
                        new CacheEntry(System.currentTimeMillis() +  30*60*1000));
            }else{
            	//return 304
            	send304Response(ctx);
//            	return;
            }
        }

        super.handleDownstream(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        Channel ch = e.getChannel();
        Throwable cause = e.getCause();

        cause.printStackTrace();
        if (ch.isConnected()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }
    
    private void send304Response(ChannelHandlerContext ctx) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, NOT_MODIFIED);
		ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
	}

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
        response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setContent(ChannelBuffers.copiedBuffer(
                "Failure: " + status.toString() + "\r\n",
                CharsetUtil.UTF_8));

        // Close the connection as soon as the error message is sent.
        ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static class CacheEntry {
        public long expires;

        private CacheEntry(long expires) {
            this.expires = expires;
        }
    }
}
