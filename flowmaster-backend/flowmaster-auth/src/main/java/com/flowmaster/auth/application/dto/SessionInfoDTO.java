package com.flowmaster.auth.application.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 会话信息DTO
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class SessionInfoDTO {

    /**
     * 会话ID
     */
    private Long sessionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 会话状态
     */
    private String status;

    /**
     * 会话状态描述
     */
    private String statusDescription;

    /**
     * 访问令牌过期时间
     */
    private LocalDateTime accessTokenExpiresAt;

    /**
     * 刷新令牌过期时间
     */
    private LocalDateTime refreshTokenExpiresAt;

    /**
     * 最后活动时间
     */
    private LocalDateTime lastActivityAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
