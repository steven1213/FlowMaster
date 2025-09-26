package com.flowmaster.auth.interfaces.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
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
                title = "FlowMaster 认证授权服务 API",
                version = "1.0.0",
                description = "FlowMaster 认证授权服务的RESTful API文档",
                contact = @Contact(name = "FlowMaster Team", email = "support@flowmaster.com"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        servers = {
                @Server(url = "http://localhost:8082/auth-service", description = "本地开发环境"),
                @Server(url = "http://dev.flowmaster.com/auth-service", description = "开发环境"),
                @Server(url = "http://prod.flowmaster.com/auth-service", description = "生产环境")
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT认证，请在输入框中输入'Bearer '后跟您的JWT令牌"
)
public class SwaggerConfig {
}
