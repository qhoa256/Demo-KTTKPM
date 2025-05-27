FROM nginx:alpine

# Copy custom HTML
COPY index.html /usr/share/nginx/html/index.html

# Create health endpoint
RUN echo '{"status":"UP","service":"client-costume-rental","timestamp":"'$(date -Iseconds)'"}' > /usr/share/nginx/html/health

# Configure nginx
RUN echo 'server { \
    listen 8080; \
    server_name localhost; \
    root /usr/share/nginx/html; \
    index index.html; \
    \
    location / { \
        try_files $uri $uri/ /index.html; \
    } \
    \
    location /health { \
        add_header Content-Type application/json; \
        return 200 "{\"status\":\"UP\",\"service\":\"client-costume-rental\"}"; \
    } \
    \
    location /actuator/health { \
        add_header Content-Type application/json; \
        return 200 "{\"status\":\"UP\",\"service\":\"client-costume-rental\"}"; \
    } \
}' > /etc/nginx/conf.d/default.conf

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
