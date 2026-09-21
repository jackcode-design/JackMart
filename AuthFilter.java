package com.jack.jackmart.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Adds a request ID to MDC and the response for log correlation.
 */
public class RequestIdFilter implements Filter {
    public static final String REQUEST_ID = "requestId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String id = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID, id);
        ((HttpServletResponse) response).setHeader("X-Request-Id", id);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID);
        }
    }
}
