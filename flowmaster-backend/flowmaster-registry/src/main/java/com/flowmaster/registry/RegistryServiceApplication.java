package com.flowmaster.registry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * FlowMaster 服务注册与发现启动类
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {
    "com.flowmaster.registry"
})
@EnableEurekaServer
@Slf4j
public class RegistryServiceApplication {

    public static void main(String[] args) {
        log.info("启动 FlowMaster 服务注册与发现...");
        
        try {
            SpringApplication.run(RegistryServiceApplication.class, args);
            log.info("FlowMaster 服务注册与发现启动成功！");
        } catch (Exception e) {
            log.error("FlowMaster 服务注册与发现启动失败: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}
