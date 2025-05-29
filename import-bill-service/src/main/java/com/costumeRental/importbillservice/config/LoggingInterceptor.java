package com.costumeRental.importbillservice.config;

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
        MDC.put("serviceName", "import-bill-service");

        // Log request details
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        logger.info("[REQUEST:{}] [{}:{}] [DETAILS] Method: {}, Path: {}, Query params: {}", 
            requestId,
            "import-bill-service", 
            request.getServerPort(),
            request.getMethod(), 
            request.getRequestURI(),
            request.getQueryString() != null ? request.getQueryString() : "{}");

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            Long startTime = (Long) request.getAttribute("startTime");
            long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;
            String requestId = MDC.get("requestId");

            // Log response details
            logger.info("[RESPONSE:{}] [{}:{}] [PROXY] Response from Import Bill Service: {} {} {} ms", 
                requestId,
                "import-bill-service",
                request.getServerPort(),
                response.getStatus(),
                request.getRequestURI(),
                duration);

            if (ex != null) {
                logger.error("[ERROR:{}] [{}:{}] Exception occurred: {}", 
                    requestId,
                    "import-bill-service",
                    request.getServerPort(),
                    ex.getMessage(), ex);
            }
        } finally {
            // Clean up MDC
            MDC.clear();
        }
    }
}
