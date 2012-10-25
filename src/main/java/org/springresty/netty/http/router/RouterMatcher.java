package org.springresty.netty.http.router;

public interface RouterMatcher {
    public boolean match(String uri);
}
