package com.flowmaster.workflow.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 工作流基础设施配置
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.flowmaster.workflow.infrastructure.persistence.repository")
@EnableTransactionManagement
@Slf4j
public class InfrastructureConfig {

    public InfrastructureConfig() {
        log.info("工作流基础设施配置初始化完成");
    }
}
