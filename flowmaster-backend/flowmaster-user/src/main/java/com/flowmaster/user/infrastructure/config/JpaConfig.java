package com.flowmaster.user.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA配置
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.flowmaster.user.infrastructure.persistence.repository")
@EntityScan(basePackages = "com.flowmaster.user.infrastructure.persistence.entity")
@EnableTransactionManagement
public class JpaConfig {
}
