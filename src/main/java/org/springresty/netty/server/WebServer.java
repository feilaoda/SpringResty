package org.springresty.netty.server;

import java.net.InetSocketAddress;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.sf.ehcache.util.NamedThreadFactory;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class WebServer {
	private ChannelPipelineFactory pipelineFactory;
	private int listenPort = 8000;
	private ServerBootstrap bootstrap;

	public WebServer(int port) {
		listenPort = port;
		// bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
		// new ThreadPoolExecutor(100, 200, 30L, TimeUnit.MILLISECONDS,
		// new LinkedBlockingQueue<Runnable>()),
		// new ThreadPoolExecutor(100, 200, 30L, TimeUnit.MILLISECONDS,
		// new LinkedBlockingQueue<Runnable>())));

		ThreadFactory serverBossTF = new NamedThreadFactory("NETTYSERVER-BOSS-");
		ThreadFactory serverWorkerTF = new NamedThreadFactory(
				"NETTYSERVER-WORKER-");
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(serverBossTF),
				Executors.newCachedThreadPool(serverWorkerTF)));

		 bootstrap.setOption("backlog", 600);
		bootstrap.setOption("connectTimeoutMillis", 10000);
		bootstrap.setOption("reuseAddress", true); // kernel optimization
		bootstrap.setOption("keepAlive", true); // for mobiles & our
												// stateful app
		bootstrap.setOption("tcpNoDelay", true); // better latency over
													// bandwidth
		bootstrap.setOption("reuseAddress", true); // kernel
													// optimization

	}

	public Channel bind() {
		bootstrap.setPipelineFactory(pipelineFactory);
		return bootstrap.bind(new InetSocketAddress(listenPort));
	}

	public void release() {
		bootstrap.releaseExternalResources();

	}

	public ChannelPipelineFactory getPipelineFactory() {
		return pipelineFactory;
	}

	public void setPipelineFactory(ChannelPipelineFactory pipelineFactory) {
		this.pipelineFactory = pipelineFactory;
	}

	public int getListenPort() {
		return listenPort;
	}

	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
	}

	public ServerBootstrap getBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(ServerBootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}
}
