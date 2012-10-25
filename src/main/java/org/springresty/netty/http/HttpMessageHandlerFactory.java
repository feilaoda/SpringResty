package org.springresty.netty.http;

import org.jboss.netty.channel.MessageEvent;
//import org.jboss.netty.handler.codec.http.HttpRequest;
//import org.jboss.netty.handler.codec.http.QueryStringDecoder;




public  class HttpMessageHandlerFactory
{
	private static HttpMessageHandlerFactory factory = null;
    private HttpMessageHandlerFactory()
    {   
    }
    
    public static HttpMessageHandlerFactory getInstance(){
    	if(factory == null)
    	{
    		factory = new HttpMessageHandlerFactory();
    	}
    	return factory;
    }
 
}
