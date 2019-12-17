/**
 * Copyright (C), 2011-2016, 微贷网.
 */
package com.base.seed.webapp.support;


import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 添加跨域请求头
 */
@WebFilter(urlPatterns = "/*", filterName = "crossFilter")
@Service
public class CrossFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;

        String method = request1.getMethod();
        response1.setHeader("Access-Control-Allow-Origin",
                request1.getHeader("Origin") == null ? "*" : request1.getHeader("Origin"));
        response1.setHeader("Access-Control-Allow-Methods", request1.getHeader("Access-Control-Request-Method"));
        response1.setHeader("Access-Control-Allow-Headers", request1.getHeader("Access-Control-Request-Headers"));
        response1.setHeader("Access-Control-Allow-Credentials", "true");
        if ("OPTIONS".equals(method)) {
            response1.setStatus(200);
            return;
        }

        chain.doFilter(request1, response1);
    }

    @Override
    public void destroy() {

    }
}
