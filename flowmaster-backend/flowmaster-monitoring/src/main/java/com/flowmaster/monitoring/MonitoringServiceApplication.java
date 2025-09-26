package com.flowmaster.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * FlowMaster 监控和日志服务启动类
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {
    "com.flowmaster.monitoring",
    "com.flowmaster.common"
})
@EnableElasticsearchRepositories(basePackages = "com.flowmaster.monitoring.infrastructure.logging")
@Slf4j
public class MonitoringServiceApplication {

    public static void main(String[] args) {
        log.info("启动 FlowMaster 监控和日志服务...");
        
        try {
            SpringApplication.run(MonitoringServiceApplication.class, args);
            log.info("FlowMaster 监控和日志服务启动成功！");
        } catch (Exception e) {
            log.error("FlowMaster 监控和日志服务启动失败: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}
