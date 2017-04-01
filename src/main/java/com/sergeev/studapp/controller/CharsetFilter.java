package com.sergeev.studapp.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.TimeZone;

@WebFilter(filterName = "CharsetFilter", urlPatterns = "/*")
public class CharsetFilter implements Filter {

    private String encoding;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
            if (null == request.getCharacterEncoding()) {
                request.setCharacterEncoding(encoding);
            }
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("requestEncoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    public void destroy() {
    }
}
