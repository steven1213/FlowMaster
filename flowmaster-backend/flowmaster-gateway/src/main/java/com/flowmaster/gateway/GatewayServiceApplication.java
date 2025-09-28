package com.flowmaster.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * FlowMaster API网关服务启动类
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@SpringBootApplication(
    scanBasePackages = {
        "com.flowmaster.gateway",
        "com.flowmaster.common"
    },
    exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        RedisAutoConfiguration.class
    }
)
@EnableDiscoveryClient
@Slf4j
public class GatewayServiceApplication {

    public static void main(String[] args) {
        log.info("启动 FlowMaster API网关服务...");
        
        try {
            SpringApplication.run(GatewayServiceApplication.class, args);
            log.info("FlowMaster API网关服务启动成功！");
        } catch (Exception e) {
            log.error("FlowMaster API网关服务启动失败: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}
