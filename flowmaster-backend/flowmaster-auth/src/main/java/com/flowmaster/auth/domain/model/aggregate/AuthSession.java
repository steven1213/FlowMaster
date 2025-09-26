package com.flowmaster.auth.domain.model.aggregate;

import com.flowmaster.common.domain.BaseDomainEntity;
import com.flowmaster.auth.domain.model.valueobject.AccessToken;
import com.flowmaster.auth.domain.model.valueobject.RefreshToken;
import com.flowmaster.auth.domain.model.valueobject.Username;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 认证会话聚合根
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Setter
@Slf4j
public class AuthSession extends BaseDomainEntity {

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
    private Username username;

    /**
     * 访问令牌
     */
    private AccessToken accessToken;

    /**
     * 刷新令牌
     */
    private RefreshToken refreshToken;

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
    private SessionStatus status;

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
     * 领域事件列表
     */
    private final List<Object> domainEvents = new ArrayList<>();

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
     * 创建认证会话
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     * @param clientIp 客户端IP
     * @param userAgent 用户代理
     * @return 认证会话
     */
    public static AuthSession create(Long userId, Username username, AccessToken accessToken, 
                                   RefreshToken refreshToken, String clientIp, String userAgent) {
        AuthSession session = new AuthSession();
        session.userId = userId;
        session.username = username;
        session.accessToken = accessToken;
        session.refreshToken = refreshToken;
        session.clientIp = clientIp;
        session.userAgent = userAgent;
        session.status = SessionStatus.ACTIVE;
        session.lastActivityAt = LocalDateTime.now();
        
        // 设置过期时间（从配置中获取）
        session.accessTokenExpiresAt = LocalDateTime.now().plusHours(24); // TODO: 从配置获取
        session.refreshTokenExpiresAt = LocalDateTime.now().plusDays(7); // TODO: 从配置获取
        
        session.addDomainEvent(new SessionCreatedEvent(session));
        return session;
    }

    /**
     * 刷新访问令牌
     *
     * @param newAccessToken 新的访问令牌
     * @param newRefreshToken 新的刷新令牌
     */
    public void refreshTokens(AccessToken newAccessToken, RefreshToken newRefreshToken) {
        this.accessToken = newAccessToken;
        this.refreshToken = newRefreshToken;
        this.lastActivityAt = LocalDateTime.now();
        this.accessTokenExpiresAt = LocalDateTime.now().plusHours(24); // TODO: 从配置获取
        this.refreshTokenExpiresAt = LocalDateTime.now().plusDays(7); // TODO: 从配置获取
        
        addDomainEvent(new SessionRefreshedEvent(this));
    }

    /**
     * 撤销会话
     */
    public void revoke() {
        this.status = SessionStatus.REVOKED;
        this.lastActivityAt = LocalDateTime.now();
        
        addDomainEvent(new SessionRevokedEvent(this));
    }

    /**
     * 暂停会话
     */
    public void suspend() {
        this.status = SessionStatus.SUSPENDED;
        this.lastActivityAt = LocalDateTime.now();
        
        addDomainEvent(new SessionSuspendedEvent(this));
    }

    /**
     * 激活会话
     */
    public void activate() {
        this.status = SessionStatus.ACTIVE;
        this.lastActivityAt = LocalDateTime.now();
        
        addDomainEvent(new SessionActivatedEvent(this));
    }

    /**
     * 更新最后活动时间
     */
    public void updateLastActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }

    /**
     * 检查访问令牌是否过期
     *
     * @return 是否过期
     */
    public boolean isAccessTokenExpired() {
        return LocalDateTime.now().isAfter(accessTokenExpiresAt);
    }

    /**
     * 检查刷新令牌是否过期
     *
     * @return 是否过期
     */
    public boolean isRefreshTokenExpired() {
        return LocalDateTime.now().isAfter(refreshTokenExpiresAt);
    }

    /**
     * 检查会话是否有效
     *
     * @return 是否有效
     */
    public boolean isValid() {
        return status == SessionStatus.ACTIVE && 
               !isAccessTokenExpired() && 
               !isRefreshTokenExpired();
    }

    /**
     * 添加领域事件
     *
     * @param event 领域事件
     */
    private void addDomainEvent(Object event) {
        domainEvents.add(event);
    }

    /**
     * 获取领域事件
     *
     * @return 领域事件列表
     */
    @DomainEvents
    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * 清理领域事件
     */
    @AfterDomainEventPublication
    public void clearDomainEvents() {
        domainEvents.clear();
    }

    // 领域事件类
    public static class SessionCreatedEvent {
        private final AuthSession session;
        
        public SessionCreatedEvent(AuthSession session) {
            this.session = session;
        }
        
        public AuthSession getSession() {
            return session;
        }
    }

    public static class SessionRefreshedEvent {
        private final AuthSession session;
        
        public SessionRefreshedEvent(AuthSession session) {
            this.session = session;
        }
        
        public AuthSession getSession() {
            return session;
        }
    }

    public static class SessionRevokedEvent {
        private final AuthSession session;
        
        public SessionRevokedEvent(AuthSession session) {
            this.session = session;
        }
        
        public AuthSession getSession() {
            return session;
        }
    }

    public static class SessionSuspendedEvent {
        private final AuthSession session;
        
        public SessionSuspendedEvent(AuthSession session) {
            this.session = session;
        }
        
        public AuthSession getSession() {
            return session;
        }
    }

    public static class SessionActivatedEvent {
        private final AuthSession session;
        
        public SessionActivatedEvent(AuthSession session) {
            this.session = session;
        }
        
        public AuthSession getSession() {
            return session;
        }
    }
}
