package com.flowmaster.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 认证授权服务启动类
 *
 * @author FlowMaster Team
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.flowmaster")
@EnableJpaAuditing
@EnableTransactionManagement
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
