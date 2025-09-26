package com.flowmaster.auth.infrastructure.persistence.entity;

import com.flowmaster.common.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

/**
 * 认证会话JPA实体
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "auth_sessions", indexes = {
    @Index(name = "idx_auth_sessions_user_id", columnList = "user_id"),
    @Index(name = "idx_auth_sessions_username", columnList = "username"),
    @Index(name = "idx_auth_sessions_status", columnList = "status"),
    @Index(name = "idx_auth_sessions_created_at", columnList = "created_at")
})
@Getter
@Setter
@Slf4j
@SQLDelete(sql = "UPDATE auth_sessions SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class AuthSessionEntity extends BaseJpaEntity {

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    /**
     * 访问令牌
     */
    @Column(name = "access_token", nullable = false, length = 2000)
    private String accessToken;

    /**
     * 刷新令牌
     */
    @Column(name = "refresh_token", nullable = false, length = 2000)
    private String refreshToken;

    /**
     * 客户端IP
     */
    @Column(name = "client_ip", length = 45)
    private String clientIp;

    /**
     * 用户代理
     */
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    /**
     * 会话状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SessionStatus status;

    /**
     * 访问令牌过期时间
     */
    @Column(name = "access_token_expires_at", nullable = false)
    private LocalDateTime accessTokenExpiresAt;

    /**
     * 刷新令牌过期时间
     */
    @Column(name = "refresh_token_expires_at", nullable = false)
    private LocalDateTime refreshTokenExpiresAt;

    /**
     * 最后活动时间
     */
    @Column(name = "last_activity_at", nullable = false)
    private LocalDateTime lastActivityAt;

    /**
     * 会话状态枚举
     */
    public enum SessionStatus {
        ACTIVE("活跃"),
        EXPIRED("已过期"),
        REVOKED("已撤销"),
        SUSPENDED("已暂停");

        private final String description;

        SessionStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 转换为领域对象
     *
     * @return 认证会话领域对象
     */
    public com.flowmaster.auth.domain.model.aggregate.AuthSession toDomain() {
        com.flowmaster.auth.domain.model.aggregate.AuthSession session = 
            com.flowmaster.auth.domain.model.aggregate.AuthSession.create(
                this.userId,
                com.flowmaster.auth.domain.model.valueobject.Username.of(this.username),
                com.flowmaster.auth.domain.model.valueobject.AccessToken.of(this.accessToken),
                com.flowmaster.auth.domain.model.valueobject.RefreshToken.of(this.refreshToken),
                this.clientIp,
                this.userAgent
            );
        
        // 设置基础字段
        session.setSessionId(this.id);
        session.setStatus(com.flowmaster.auth.domain.model.aggregate.AuthSession.SessionStatus.valueOf(this.status.name()));
        session.setAccessTokenExpiresAt(this.accessTokenExpiresAt);
        session.setRefreshTokenExpiresAt(this.refreshTokenExpiresAt);
        session.setLastActivityAt(this.lastActivityAt);
        session.setCreatedAt(this.createdAt);
        session.setUpdatedAt(this.updatedAt);
        session.setCreatedBy(this.createdBy);
        session.setUpdatedBy(this.updatedBy);
        session.setVersion(this.version);
        session.setDeleted(this.deleted);
        
        return session;
    }

    /**
     * 从领域对象创建
     *
     * @param session 认证会话领域对象
     * @return JPA实体
     */
    public static AuthSessionEntity fromDomain(com.flowmaster.auth.domain.model.aggregate.AuthSession session) {
        AuthSessionEntity entity = new AuthSessionEntity();
        entity.id = session.getSessionId();
        entity.userId = session.getUserId();
        entity.username = session.getUsername().getValue();
        entity.accessToken = session.getAccessToken().getJwt();
        entity.refreshToken = session.getRefreshToken().getJwt();
        entity.clientIp = session.getClientIp();
        entity.userAgent = session.getUserAgent();
        entity.status = SessionStatus.valueOf(session.getStatus().name());
        entity.accessTokenExpiresAt = session.getAccessTokenExpiresAt();
        entity.refreshTokenExpiresAt = session.getRefreshTokenExpiresAt();
        entity.lastActivityAt = session.getLastActivityAt();
        entity.createdAt = session.getCreatedAt();
        entity.updatedAt = session.getUpdatedAt();
        entity.createdBy = session.getCreatedBy();
        entity.updatedBy = session.getUpdatedBy();
        entity.version = session.getVersion();
        entity.deleted = session.getDeleted();
        return entity;
    }
}
