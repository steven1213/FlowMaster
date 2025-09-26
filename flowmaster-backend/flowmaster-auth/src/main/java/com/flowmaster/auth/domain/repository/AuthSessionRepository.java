package com.flowmaster.auth.domain.repository;

import com.flowmaster.auth.domain.model.aggregate.AuthSession;
import com.flowmaster.auth.domain.model.valueobject.AccessToken;
import com.flowmaster.auth.domain.model.valueobject.RefreshToken;
import com.flowmaster.auth.domain.model.valueobject.Username;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 认证会话仓储接口
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
public interface AuthSessionRepository {

    /**
     * 保存认证会话
     *
     * @param session 认证会话
     * @return 保存后的会话
     */
    AuthSession save(AuthSession session);

    /**
     * 根据会话ID查找会话
     *
     * @param sessionId 会话ID
     * @return 会话
     */
    Optional<AuthSession> findById(Long sessionId);

    /**
     * 根据访问令牌查找会话
     *
     * @param accessToken 访问令牌
     * @return 会话
     */
    Optional<AuthSession> findByAccessToken(AccessToken accessToken);

    /**
     * 根据刷新令牌查找会话
     *
     * @param refreshToken 刷新令牌
     * @return 会话
     */
    Optional<AuthSession> findByRefreshToken(RefreshToken refreshToken);

    /**
     * 根据用户ID查找活跃会话
     *
     * @param userId 用户ID
     * @return 活跃会话列表
     */
    List<AuthSession> findActiveSessionsByUserId(Long userId);

    /**
     * 根据用户名查找活跃会话
     *
     * @param username 用户名
     * @return 活跃会话列表
     */
    List<AuthSession> findActiveSessionsByUsername(Username username);

    /**
     * 分页查询用户会话
     *
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 会话分页结果
     */
    Page<AuthSession> findByUserId(Long userId, Pageable pageable);

    /**
     * 查找过期的会话
     *
     * @param expiredBefore 过期时间
     * @return 过期会话列表
     */
    List<AuthSession> findExpiredSessions(LocalDateTime expiredBefore);

    /**
     * 删除会话
     *
     * @param sessionId 会话ID
     */
    void deleteById(Long sessionId);

    /**
     * 删除用户的所有会话
     *
     * @param userId 用户ID
     */
    void deleteByUserId(Long userId);

    /**
     * 删除过期的会话
     *
     * @param expiredBefore 过期时间
     * @return 删除的会话数量
     */
    int deleteExpiredSessions(LocalDateTime expiredBefore);

    /**
     * 统计用户活跃会话数量
     *
     * @param userId 用户ID
     * @return 活跃会话数量
     */
    long countActiveSessionsByUserId(Long userId);

    /**
     * 统计总会话数量
     *
     * @return 总会话数量
     */
    long count();
}
