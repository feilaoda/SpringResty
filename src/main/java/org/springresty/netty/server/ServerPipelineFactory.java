package org.springresty.netty.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.MemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.springresty.netty.handler.HttpCacheHandler;
import org.springresty.netty.handler.HttpResponse304Handler;
import org.springresty.netty.handler.HttpServerHandler;
import org.springresty.netty.http.router.HandlerRouter;


public class ServerPipelineFactory implements ChannelPipelineFactory {

    private HttpServerHandler channelHandler;

    private HandlerRouter router;
    
    private ExecutionHandler executionHandler;
    
    private HttpCacheHandler cacheHandler;
    private HttpResponse304Handler notModifiedHandler;
    
    private ExecutorService threadPool;
    
    public ServerPipelineFactory() {
        super();
        cacheHandler = new HttpCacheHandler();
        channelHandler = new HttpServerHandler();
        OrderedMemoryAwareThreadPoolExecutor executor = new OrderedMemoryAwareThreadPoolExecutor(16, 1048576, 1048576);
//		executionHandler = new ExecutionHandler(executor);
//		notModifiedHandler = new HttpResponse304Handler();
//		threadPool = new ThreadPoolExecutor(10, 100, 60L, TimeUnit.MILLISECONDS,
//				 new LinkedBlockingQueue<Runnable>());
		
    }

 

    public ChannelPipeline getPipeline() throws Exception {
        // TODO Auto-generated method stub
        ChannelPipeline channelPipeline = new DefaultChannelPipeline();

        // Uncomment the following line if you want HTTPS
        // SSLEngine engine =
        // SecureChatSslContextFactory.getServerContext().createSSLEngine();
        // engine.setUseClientMode(false);
        // pipeline.addLast("ssl", new SslHandler(engine));

        channelPipeline.addLast("decoder", new HttpRequestDecoder(4096,8192,65535));
        //channelPipeline.addLast("aggregator", new HttpChunkAggregator(65536));
        channelPipeline.addLast("encoder", new HttpResponseEncoder());
        // channelPipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        //channelPipeline.addLast("cacheHandler", cacheHandler);
//        channelPipeline.addLast("execution", executionHandler);
        channelPipeline.addLast("handler", channelHandler);
        return channelPipeline;
    }

	public HandlerRouter getRouter() {
		return router;
	}

	public void setRouter(HandlerRouter router) {
		this.router = router;
		channelHandler.setRouter(router);
	}

	public ExecutionHandler getExecutionHandler() {
		return executionHandler;
	}

	public void setExecutionHandler(ExecutionHandler executionHandler) {
		this.executionHandler = executionHandler;
	}

 

}
