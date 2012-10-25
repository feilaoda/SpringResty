package org.springresty.framework.controller;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

 
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;
import org.springframework.context.ApplicationContext;
import org.springresty.exception.RestyException;
import org.springresty.framework.annotation.Controller;
import org.springresty.framework.annotation.Get;
import org.springresty.framework.annotation.Path;
import org.springresty.framework.annotation.Post;
import org.springresty.framework.annotation.Produces;
import org.springresty.framework.annotation.Validate;
import org.springresty.framework.validation.Validator;
import org.springresty.netty.http.RestyRequest;
import org.springresty.netty.http.router.HandlerRouter;
import org.springresty.utils.ClassUtils;


public class ControllerProxy {

	
	

	private class ControllerInlineHandler implements ControllerHandler{
		public Object bean;
		public Method method;
		public HttpMethod methodType;
		public String contentType;
		public String path;
		public Boolean cacheable;
		public Boolean validate;
		public Method  validateMethod;
		
		public ControllerInlineHandler() {
			contentType = "application/json; charset=UTF-8";
			methodType = HttpMethod.GET;
			path = "";
			cacheable = false;
			validate = false;
		}
		
		private HttpResponse createErrorResponse(String message, HttpResponseStatus status) {
			HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
			response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
			response.setHeader(HttpHeaders.Names.CONNECTION, "close");
			String error = "Failure: " + message + "\r\n";
			response.setContent(ChannelBuffers.copiedBuffer(error, CharsetUtil.UTF_8));
			response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, error.length());
			return response;
		}
		
		
		public void executeValidate(List<Validator> rules, RestyRequest request) throws RestyException{
			for(Validator validator : rules){
				String value = request.getParameterString(validator.getName());
				validator.validate(value);
			}
		}

		public HttpResponse excute(HttpRequest request) {

			// InvocationHandler handler = Proxy.getInvocationHandler(bean);
			// return (HttpResponse) handler.invoke (bean, method, new
			// Object[]{request});
			try {
				RestyRequest restyRequest = new RestyRequest(request);
				if(this.validate){
					List<Validator> rules = (List<Validator>)this.validateMethod.invoke(bean);
					executeValidate(rules, restyRequest);
				}
				
				Class<?> returnType = method.getReturnType();
				if (returnType.equals(HttpResponse.class)) {
					return (HttpResponse) method.invoke(bean, restyRequest);
				} else {
					HttpResponse response = new DefaultHttpResponse(HTTP_1_1,	OK);
					response.setHeader("Content-Type", contentType);
					
					if (returnType.equals(byte[].class)) {

						byte[] bytes = (byte[]) method.invoke(bean, restyRequest);
						ChannelBuffer buffer = ChannelBuffers.copiedBuffer(bytes);
						response.setContent(buffer);
						response.setHeader(CONTENT_LENGTH, bytes.length);
						return response;
					} 
					else // (returnType.equals(String.class))
					{
						String responseHtml = (String) method.invoke(bean,restyRequest);
						ChannelBuffer buffer = ChannelBuffers.copiedBuffer(responseHtml, CharsetUtil.UTF_8);
						response.setContent(buffer);
						response.setHeader(CONTENT_LENGTH,	responseHtml.length());
						return response;
					}
				}
			}catch (RestyException e) {
				e.printStackTrace();
				return createErrorResponse(e.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
			}
			catch (InvocationTargetException e) {
				e.printStackTrace();
				return createErrorResponse(e.getCause().getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return createErrorResponse(e.getCause().getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return createErrorResponse(e.getCause().getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
			}
			catch(Exception e){
				return createErrorResponse(e.getMessage(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
			}
		}

	 

	}

	public Map<String, ControllerHandler> scan(ApplicationContext context, String urlPrefix, String packageName) {
		Set<Class<?>> cs = ClassUtils.packageScanClasses(packageName);

		Map<String, ControllerHandler> handlerMap = new LinkedHashMap<String, ControllerHandler>();

		for (Class<?> clz : cs) {
			// Annotation[] as = clz.getAnnotations();
			// for(int i=0; i<as.length; i++){
			// System.out.println(as[i].getClass().getName());
			// }
			Controller annotation = clz.getAnnotation(Controller.class);
			if (annotation == null) {
				continue;
			}

			Object object = context.getBean(StringUtils.uncapitalize(clz
					.getSimpleName()));

			Path pathAnnotation = clz.getAnnotation(Path.class);
			if (pathAnnotation == null) {
				// throw
				continue;
			}
			String controllerPrefix = pathAnnotation.value();
			Method[] ms = clz.getDeclaredMethods();
			for (Method method : ms) {
				ControllerInlineHandler handler = new ControllerInlineHandler();
				handler.bean = object;
				handler.method = method;

				Get getAnno = method.getAnnotation(Get.class);
				if (getAnno != null) {
					handler.methodType = HttpMethod.GET;
				}
				Post postAnno = method.getAnnotation(Post.class);
				if (postAnno != null) {
					handler.methodType = HttpMethod.POST;
				}
				Path pathAnno = method.getAnnotation(Path.class);
				if (pathAnno == null) {
					handler.path = urlPrefix + controllerPrefix;
				} else {
					handler.path = urlPrefix + controllerPrefix
							+ pathAnno.value();
				}
				Validate validateAnno = method.getAnnotation(Validate.class);
				if(validateAnno != null){
					handler.validate = true;
					String validateName = validateAnno.value();
					if(validateName.equals("")){
						validateName = "validate";
					}
					try {
						Method validateMethod = clz.getDeclaredMethod(validateName);
						assert(validateMethod != null);
						handler.validateMethod = validateMethod;
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}					
					
				}

				Produces producesAnno = method.getAnnotation(Produces.class);
				if (producesAnno != null) {
					String[] vs = producesAnno.value();
					handler.contentType = StringUtils.join(vs, ";");
				}
				String key = HandlerRouter.EQUALS + handler.methodType.getName() + ":"
						+ handler.path;
				handlerMap.put(key, handler);
			}
		}

		 

		return handlerMap;
	}


}
