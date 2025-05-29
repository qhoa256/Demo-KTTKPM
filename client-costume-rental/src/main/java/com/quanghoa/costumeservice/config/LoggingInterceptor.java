package com.quanghoa.costumeservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Generate unique request ID
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("requestId", requestId);
        MDC.put("serviceName", "client-costume-rental");

        // Log request details
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        logger.info("[REQUEST:{}] [{}:{}] [DETAILS] Method: {}, Path: {}, Query: {}, User-Agent: {}", 
            requestId,
            "client-costume-rental", 
            request.getServerPort(),
            request.getMethod(), 
            request.getRequestURI(),
            request.getQueryString() != null ? request.getQueryString() : "",
            request.getHeader("User-Agent") != null ? request.getHeader("User-Agent").substring(0, Math.min(50, request.getHeader("User-Agent").length())) + "..." : "");

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            Long startTime = (Long) request.getAttribute("startTime");
            long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;
            String requestId = MDC.get("requestId");

            // Log response details
            logger.info("[RESPONSE:{}] [{}:{}] [PROXY] Response from Client Service: {} {} {} ms", 
                requestId,
                "client-costume-rental",
                request.getServerPort(),
                request.getMethod(),
                request.getRequestURI(),
                duration);

            if (ex != null) {
                logger.error("[ERROR:{}] [{}:{}] Exception occurred: {}", 
                    requestId,
                    "client-costume-rental",
                    request.getServerPort(),
                    ex.getMessage(), ex);
            }
        } finally {
            // Clean up MDC
            MDC.clear();
        }
    }
}
