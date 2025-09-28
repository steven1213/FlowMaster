package com.flowmaster.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * FlowMaster 工作流引擎服务启动类
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {
    "com.flowmaster.workflow",
    "com.flowmaster.common"
}, exclude = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableJpaAuditing
@EnableTransactionManagement
@Slf4j
public class WorkflowServiceApplication {

    public static void main(String[] args) {
        log.info("启动 FlowMaster 工作流引擎服务...");
        
        try {
            SpringApplication.run(WorkflowServiceApplication.class, args);
            log.info("FlowMaster 工作流引擎服务启动成功！");
        } catch (Exception e) {
            log.error("FlowMaster 工作流引擎服务启动失败: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}
