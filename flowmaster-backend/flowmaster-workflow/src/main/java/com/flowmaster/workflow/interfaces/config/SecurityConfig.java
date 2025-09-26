package com.flowmaster.workflow.interfaces.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security配置
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("配置工作流引擎服务安全策略");
        
        http
            // 禁用CSRF保护
            .csrf(AbstractHttpConfigurer::disable)
            
            // 配置CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 配置会话管理
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 配置授权规则
            .authorizeHttpRequests(authz -> authz
                // Swagger相关路径允许匿名访问
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/webjars/**",
                    "/swagger-resources/**"
                ).permitAll()
                
                // 健康检查路径允许匿名访问
                .requestMatchers(
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()
                
                // 工作流API需要认证
                .requestMatchers("/api/v1/workflow/**").authenticated()
                
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            
            // 禁用HTTP Basic认证
            .httpBasic(AbstractHttpConfigurer::disable)
            
            // 禁用表单登录
            .formLogin(AbstractHttpConfigurer::disable);
        
        log.info("工作流引擎服务安全策略配置完成");
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
            "X-Trace-Id"
        ));
        
        // 允许的响应头
        configuration.setExposedHeaders(Arrays.asList(
            "X-Trace-Id",
            "X-Request-Id"
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
