package com.flowmaster.auth.application.command;

import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;

/**
 * 刷新令牌命令
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class RefreshTokenCommand {

    /**
     * 刷新令牌
     */
    @NotBlank(message = "刷新令牌不能为空")
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
