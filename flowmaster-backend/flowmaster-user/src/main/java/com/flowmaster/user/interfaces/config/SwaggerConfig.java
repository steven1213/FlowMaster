package com.flowmaster.user.interfaces.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger配置
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FlowMaster 用户管理服务 API")
                        .description("FlowMaster 工作流管理平台 - 用户管理服务 REST API 文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("FlowMaster Team")
                                .email("team@flowmaster.com")
                                .url("https://www.flowmaster.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081/user-service")
                                .description("开发环境"),
                        new Server()
                                .url("https://api.flowmaster.com/user-service")
                                .description("生产环境")
                ));
    }
}
