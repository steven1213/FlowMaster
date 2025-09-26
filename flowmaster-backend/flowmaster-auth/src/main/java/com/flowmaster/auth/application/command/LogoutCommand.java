package com.flowmaster.auth.application.command;

import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;

/**
 * 登出命令
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class LogoutCommand {

    /**
     * 访问令牌
     */
    @NotBlank(message = "访问令牌不能为空")
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 用户代理
     */
    private String userAgent;
}
