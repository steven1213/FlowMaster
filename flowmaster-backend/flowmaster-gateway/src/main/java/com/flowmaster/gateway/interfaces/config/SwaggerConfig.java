package com.flowmaster.gateway.interfaces.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "FlowMaster API Gateway",
        version = "1.0.0",
        description = "FlowMaster API Gateway 统一入口服务",
        contact = @Contact(
            name = "FlowMaster Team",
            email = "support@flowmaster.com",
            url = "https://www.flowmaster.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "开发环境"),
        @Server(url = "https://api-dev.flowmaster.com", description = "测试环境"),
        @Server(url = "https://api.flowmaster.com", description = "生产环境")
    }
)
@SecuritySchemes({
    @SecurityScheme(
        name = "Bearer Token",
        type = io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT认证令牌"
    ),
    @SecurityScheme(
        name = "API Key",
        type = io.swagger.v3.oas.annotations.enums.SecuritySchemeType.APIKEY,
        in = io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER,
        paramName = "X-API-Key",
        description = "API密钥认证"
    )
})
@Slf4j
public class SwaggerConfig {

    public SwaggerConfig() {
        log.info("API网关Swagger配置初始化完成");
    }
}
