package org.springresty.netty.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.NotEnoughDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import org.springresty.exception.RestyException;


public class RestyRequest {
	private HttpRequest httpRequest;
	private Map<String, Object> params;
	
	private static final HttpDataFactory factory = new DefaultHttpDataFactory(
            DefaultHttpDataFactory.MINSIZE);
	
	public RestyRequest(HttpRequest request){
		setHttpRequest(request);
		params = getQueryParameters(request);
	}
	

	public String getParameterString(String parameterName){
		Object value = params.get(parameterName);
		if(value == null){
			return null;
		}
		if(value instanceof String){
			return (String)value;
		}
		else if(value instanceof List){
			List<String> valueList = (List<String>)value;
			if(valueList.size() > 0){
				return valueList.get(0);
			}
			else{
				return null;
			}			
		}
 		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getParameterList(String parameterName){
		Object value = params.get(parameterName);
		if(value == null){
			return null;
		}
		if(value instanceof List){
			return (List<String>)value;
		}		
 		return null;
	}
	
	public FileUpload getParameterFile(String parameterName) {
		Object value = params.get(parameterName);
		if(value == null){
			return null;
		}
		if(value instanceof FileUpload){
			return (FileUpload)value;
		}		
 		return null;
	}
	
	
	
	private Map<String, Object> getQueryParameters(HttpRequest request){
		QueryStringDecoder queryStringDecoder = null ;
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(request.getMethod() == HttpMethod.POST){
			queryStringDecoder = new QueryStringDecoder(request.getUri());
			Map<String, List<String>> queryParams = queryStringDecoder.getParameters();
			params.putAll(queryParams);
			try {
				HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);

				List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
				
				for (InterfaceHttpData data : datas) {
					if (data.getHttpDataType() == HttpDataType.Attribute) {
						Attribute attribute = (Attribute) data;
						String key = attribute.getName();
						List<String> values = null;
						if(params.containsKey(key)){
							Object obj = params.get(key);
							if(obj instanceof List){								
								values = (List<String>)obj;
							}
						}else{
							values = new ArrayList<String>();
						}
						if (values != null) {
							values.add(attribute.getValue());	
							if(values.size() == 0){
								//null
							}else if(values.size() == 1){
								//单个String
								params.put(data.getName(), values.get(0));
							}
							else{
								//存放List
								params.put(data.getName(), values);
							}
						}
					}
				}

			} catch (ErrorDataDecoderException e) {
				e.printStackTrace();
			} catch (IncompatibleDataDecoderException e) {
				e.printStackTrace();
			} catch (NotEnoughDataDecoderException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else{
			 queryStringDecoder = new QueryStringDecoder(request.getUri());
			 Map<String, List<String>> queryParams = queryStringDecoder.getParameters();
			 Iterator<Entry<String, List<String>>> iter = queryParams.entrySet().iterator(); 
			 while (iter.hasNext()) { 
				 Entry<String, List<String>> entry =  iter.next(); 
			     String key = entry.getKey(); 
			     List<String> val = entry.getValue(); 
			     if(val.size() == 0){
			    	 //null
			     }else if(val.size() == 1){
			    	 params.put(key, val.get(0));
			     }else{
			    	 params.put(key, val);
			     }
			 } 
		}
		
		return params;
	}
	
	
	
	public HttpRequest getHttpRequest() {
		return httpRequest;
	}
	public void setHttpRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
}
