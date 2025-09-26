package com.flowmaster.gateway.infrastructure.filter;

import com.flowmaster.common.response.ResultCode;
import com.flowmaster.gateway.infrastructure.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 认证过滤器
 * 验证JWT令牌的有效性
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    /**
     * 不需要认证的路径
     */
    private static final List<String> SKIP_AUTH_PATHS = Arrays.asList(
        "/auth/login",
        "/auth/refresh",
        "/swagger-ui",
        "/v3/api-docs",
        "/actuator/health",
        "/actuator/info"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        log.debug("认证过滤器处理请求: path={}", path);

        // 检查是否需要跳过认证
        if (shouldSkipAuth(path)) {
            log.debug("跳过认证: path={}", path);
            return chain.filter(exchange);
        }

        // 获取Authorization头
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            log.warn("缺少有效的Authorization头: path={}", path);
            return handleUnauthorized(exchange);
        }

        // 提取JWT令牌
        String token = authHeader.substring(7);
        if (!StringUtils.hasText(token)) {
            log.warn("JWT令牌为空: path={}", path);
            return handleUnauthorized(exchange);
        }

        try {
            // 验证JWT令牌
            if (!jwtService.validateToken(token)) {
                log.warn("JWT令牌验证失败: path={}", path);
                return handleUnauthorized(exchange);
            }

            // 提取用户信息
            String userId = jwtService.extractUserId(token);
            String username = jwtService.extractUsername(token);
            
            if (!StringUtils.hasText(userId) || !StringUtils.hasText(username)) {
                log.warn("JWT令牌中缺少用户信息: path={}", path);
                return handleUnauthorized(exchange);
            }

            // 添加用户信息到请求头
            ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", userId)
                .header("X-Username", username)
                .build();

            log.debug("认证成功: userId={}, username={}, path={}", userId, username, path);
            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            log.error("JWT令牌验证异常: path={}, error={}", path, e.getMessage(), e);
            return handleUnauthorized(exchange);
        }
    }

    /**
     * 检查是否应该跳过认证
     */
    private boolean shouldSkipAuth(String path) {
        return SKIP_AUTH_PATHS.stream().anyMatch(path::startsWith);
    }

    /**
     * 处理未授权请求
     */
    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        
        String body = String.format(
            "{\"code\":%d,\"message\":\"%s\",\"data\":null,\"timestamp\":%d,\"traceId\":\"%s\"}",
            ResultCode.UNAUTHORIZED.getCode(),
            ResultCode.UNAUTHORIZED.getMessage(),
            System.currentTimeMillis(),
            exchange.getRequest().getId()
        );
        
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -100; // 高优先级
    }
}
