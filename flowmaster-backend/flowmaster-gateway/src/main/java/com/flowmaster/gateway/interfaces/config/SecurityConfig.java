package com.flowmaster.gateway.interfaces.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security配置
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        log.info("配置API网关安全策略");
        
        http
            // 禁用CSRF保护
            .csrf(csrf -> csrf.disable())
            
            // 配置CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 配置授权规则
            .authorizeExchange(exchanges -> exchanges
                // Swagger相关路径允许匿名访问
                .pathMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/swagger-resources/**"
                ).permitAll()
                
                // 健康检查路径允许匿名访问
                .pathMatchers(
                    "/actuator/health",
                    "/actuator/info",
                    "/gateway/health",
                    "/gateway/info"
                ).permitAll()
                
                // 认证相关路径允许匿名访问
                .pathMatchers("/auth/login", "/auth/refresh").permitAll()
                
                // 其他请求需要认证
                .anyExchange().authenticated()
            )
            
            // 禁用HTTP Basic认证
            .httpBasic(httpBasic -> httpBasic.disable())
            
            // 禁用表单登录
            .formLogin(formLogin -> formLogin.disable());
        
        log.info("API网关安全策略配置完成");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("配置CORS策略");
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 允许的源
        configuration.setAllowedOriginPatterns(List.of(
            "http://localhost:*",
            "https://*.flowmaster.com",
            "https://flowmaster.com"
        ));
        
        // 允许的HTTP方法
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "X-API-Key",
            "X-Trace-Id",
            "X-User-Id",
            "X-Username"
        ));
        
        // 允许的响应头
        configuration.setExposedHeaders(Arrays.asList(
            "X-Trace-Id",
            "X-Request-Id",
            "X-Gateway-Source"
        ));
        
        // 允许发送凭证
        configuration.setAllowCredentials(true);
        
        // 预检请求缓存时间
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        log.info("CORS策略配置完成");
        return source;
    }
}
