package com.flowmaster.workflow.infrastructure.config;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * ProcessEngine配置类
 * 手动创建ProcessEngine Bean
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Configuration
public class ProcessEngineConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    @Primary
    public ProcessEngine processEngine() {
        StandaloneProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        
        // 设置数据源
        configuration.setDataSource(dataSource);
        
        // 设置数据库类型
        configuration.setDatabaseType("mysql");
        
        // 设置数据库字符集
        configuration.setDatabaseSchema("flowable");
        
        // 自动建表 - 使用TRUE来更新现有表
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        
        // 启用异步执行器
        configuration.setAsyncExecutorActivate(true);
        
        // 移除EventRegistry和IDM引擎配置器
        configuration.setConfigurators(new java.util.ArrayList<>());
        
        // 构建ProcessEngine
        return configuration.buildProcessEngine();
    }
}
