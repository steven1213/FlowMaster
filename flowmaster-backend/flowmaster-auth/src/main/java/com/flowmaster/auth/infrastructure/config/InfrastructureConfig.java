package com.flowmaster.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

/**
 * 基础设施配置
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.flowmaster.auth.infrastructure.persistence.repository")
public class InfrastructureConfig {

    /**
     * RestTemplate配置
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
