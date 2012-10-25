package org.springresty.netty.http.router;


 
import java.util.LinkedHashMap;
import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.springresty.framework.controller.ControllerHandler;


public class HandlerRouter {

    //private Map<RouterMatcher, HttpMessageHandler> routes = new LinkedHashMap<RouterMatcher, HttpMessageHandler>();
//    private Map<RouterMatcher, String> routes = new LinkedHashMap<RouterMatcher, String>();
    private Map<RouterMatcher, ControllerHandler> routes = new LinkedHashMap<RouterMatcher, ControllerHandler>();
    public static final String STARTS_WITH = "<";
    public static final String ENDS_WITH = ">";
    public static final String EQUALS = "=";
    //private static final ChannelHandler HANDLER_404 = new SimpleResponseHandler("Not found", 404);

 
    public HandlerRouter(Map<String, ControllerHandler> routes) throws Exception {
        setupRoutes(routes);
    }
    
    
    public ControllerHandler match(HttpMethod method, String path){
        String urlPath = method.getName() + ":" + path;
        return match(urlPath);
    }
    
    private ControllerHandler match(String path){
        for (Map.Entry<RouterMatcher, ControllerHandler> m : routes.entrySet()) {
            if (m.getKey().match(path)) {
                return m.getValue() ;               
            }
        }
        /*
        If the route can't be found and we are supposed to handle not found URLs we append a 404 handler
         */
        return null;
    }

   
    private class StartsWithMatcher implements RouterMatcher {
        private String route;

        private StartsWithMatcher(String route) {
            this.route = route;
        }

        public boolean match(String uri) {
            return uri.startsWith(route);
        }
    }

    private class EndsWithMatcher implements RouterMatcher {
        private String route;

        private EndsWithMatcher(String route) {
            this.route = route;
        }

        public boolean match(String uri) {
            return uri.endsWith(route);
        }
    }

    private class EqualsMatcher implements RouterMatcher {
        private String route;

        private EqualsMatcher(String route) {
            this.route = route;
        }

        public boolean match(String uri) {
            return uri.equals(route);
        }
    }

    private void setupRoutes(Map<String, ControllerHandler> routes) throws Exception {
        for (Map.Entry<String, ControllerHandler> m : routes.entrySet()) {
            if (m.getKey().startsWith(STARTS_WITH)) {
                this.routes.put(new StartsWithMatcher(m.getKey().replace(STARTS_WITH, "")), m.getValue());
            } else if (m.getKey().startsWith(ENDS_WITH)) {
                this.routes.put(new EndsWithMatcher(m.getKey().replace(ENDS_WITH, "")), m.getValue());
            } else if (m.getKey().startsWith(EQUALS)) {
                this.routes.put(new EqualsMatcher(m.getKey().replace(EQUALS, "")), m.getValue());
            } else {
                throw new Exception("No matcher found in route " + m.getKey());
            }
        }
    }

}
