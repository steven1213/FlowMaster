package com.flowmaster.gateway.infrastructure.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流过滤器
 * 基于内存的简单限流
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Component
@Slf4j
public class RateLimitFilter implements GlobalFilter, Ordered {

    // 使用内存存储替代Redis
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lastResetTimes = new ConcurrentHashMap<>();
    
    // 限流配置
    private static final int DEFAULT_RATE_LIMIT = 100; // 每分钟100个请求
    private static final int DEFAULT_BURST_CAPACITY = 200; // 突发容量200

    public RateLimitFilter() {
        log.info("初始化限流过滤器（使用内存存储）");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String clientIp = getClientIp(request);
        
        log.debug("限流过滤器处理请求: path={}, clientIp={}", path, clientIp);

        // 根据路径确定限流策略
        RateLimitConfig config = getRateLimitConfig(path);
        
        // 构建限流key
        String key = String.format("rate_limit:%s:%s", clientIp, path);
        
        // 执行限流检查（使用内存存储）
        if (isRateLimited(key, config)) {
            log.warn("请求被限流: path={}, clientIp={}, limit={}", path, clientIp, config.getLimit());
            return handleRateLimited(exchange);
        } else {
            log.debug("限流检查通过: path={}, clientIp={}", path, clientIp);
            return chain.filter(exchange);
        }
    }

    /**
     * 检查是否超过限流
     */
    private boolean isRateLimited(String key, RateLimitConfig config) {
        long currentTime = System.currentTimeMillis();
        
        // 检查是否需要重置计数器
        Long lastReset = lastResetTimes.get(key);
        if (lastReset == null || currentTime - lastReset > config.getWindow() * 1000) {
            requestCounts.put(key, new AtomicInteger(0));
            lastResetTimes.put(key, currentTime);
        }
        
        AtomicInteger count = requestCounts.get(key);
        int currentCount = count.incrementAndGet();
        
        return currentCount > config.getLimit();
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddress() != null ? 
            request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
    }

    /**
     * 根据路径获取限流配置
     */
    private RateLimitConfig getRateLimitConfig(String path) {
        if (path.startsWith("/auth/")) {
            return new RateLimitConfig(5, 60); // 认证接口：5次/分钟
        } else if (path.startsWith("/user/")) {
            return new RateLimitConfig(10, 60); // 用户接口：10次/分钟
        } else if (path.startsWith("/workflow/")) {
            return new RateLimitConfig(20, 60); // 工作流接口：20次/分钟
        } else {
            return new RateLimitConfig(100, 60); // 默认：100次/分钟
        }
    }

    /**
     * 处理限流响应
     */
    private Mono<Void> handleRateLimited(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().add("Content-Type", "application/json");
        response.getHeaders().add("Retry-After", "60");
        
        String body = String.format(
            "{\"code\":429,\"message\":\"请求过于频繁，请稍后重试\",\"data\":null,\"timestamp\":%d,\"traceId\":\"%s\"}",
            System.currentTimeMillis(),
            exchange.getRequest().getId()
        );
        
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -50; // 中等优先级
    }

    /**
     * 限流配置
     */
    private static class RateLimitConfig {
        private final int limit;
        private final int window;

        public RateLimitConfig(int limit, int window) {
            this.limit = limit;
            this.window = window;
        }

        public int getLimit() {
            return limit;
        }

        public int getWindow() {
            return window;
        }
    }
}
