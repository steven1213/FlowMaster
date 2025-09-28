package com.flowmaster.workflow.infrastructure.config;

import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.engine.ProcessEngineConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Flowable配置类
 * 配置Flowable引擎的自动建表功能
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<ProcessEngineConfiguration> {
    
    @Override
    public void configure(ProcessEngineConfiguration engineConfiguration) {
        // 自动建表，设置为true以启用自动建表功能
        engineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        
        // 设置数据库类型为MySQL
        engineConfiguration.setDatabaseType("mysql");
        
        // 设置数据库字符集
        engineConfiguration.setDatabaseSchema("flowable");
        
        // 启用异步执行器
        engineConfiguration.setAsyncExecutorActivate(true);
    }
}
