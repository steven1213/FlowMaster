package com.flowmaster.auth.infrastructure.persistence.repository;

import com.flowmaster.auth.infrastructure.persistence.entity.AuthSessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 认证会话JPA仓储接口
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Repository
public interface AuthSessionJpaRepository extends JpaRepository<AuthSessionEntity, Long>, JpaSpecificationExecutor<AuthSessionEntity> {

    /**
     * 根据访问令牌查找会话
     *
     * @param accessToken 访问令牌
     * @return 会话实体
     */
    Optional<AuthSessionEntity> findByAccessTokenAndDeletedFalse(String accessToken);

    /**
     * 根据刷新令牌查找会话
     *
     * @param refreshToken 刷新令牌
     * @return 会话实体
     */
    Optional<AuthSessionEntity> findByRefreshTokenAndDeletedFalse(String refreshToken);

    /**
     * 根据用户ID查找活跃会话
     *
     * @param userId 用户ID
     * @return 活跃会话列表
     */
    @Query("SELECT s FROM AuthSessionEntity s WHERE s.userId = :userId AND s.status = 'ACTIVE' AND s.deleted = false")
    List<AuthSessionEntity> findActiveSessionsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户名查找活跃会话
     *
     * @param username 用户名
     * @return 活跃会话列表
     */
    @Query("SELECT s FROM AuthSessionEntity s WHERE s.username = :username AND s.status = 'ACTIVE' AND s.deleted = false")
    List<AuthSessionEntity> findActiveSessionsByUsername(@Param("username") String username);

    /**
     * 分页查询用户会话
     *
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 会话分页结果
     */
    Page<AuthSessionEntity> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    /**
     * 查找过期的会话
     *
     * @param expiredBefore 过期时间
     * @return 过期会话列表
     */
    @Query("SELECT s FROM AuthSessionEntity s WHERE s.accessTokenExpiresAt < :expiredBefore AND s.deleted = false")
    List<AuthSessionEntity> findExpiredSessions(@Param("expiredBefore") LocalDateTime expiredBefore);

    /**
     * 删除会话
     *
     * @param sessionId 会话ID
     * @param updatedAt 更新时间
     * @param updatedBy 更新人
     * @return 删除数量
     */
    @Modifying
    @Query("UPDATE AuthSessionEntity s SET s.deleted = true, s.updatedAt = :updatedAt, s.updatedBy = :updatedBy WHERE s.id = :sessionId")
    int softDeleteById(@Param("sessionId") Long sessionId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 删除用户的所有会话
     *
     * @param userId 用户ID
     * @param updatedAt 更新时间
     * @param updatedBy 更新人
     * @return 删除数量
     */
    @Modifying
    @Query("UPDATE AuthSessionEntity s SET s.deleted = true, s.updatedAt = :updatedAt, s.updatedBy = :updatedBy WHERE s.userId = :userId")
    int softDeleteByUserId(@Param("userId") Long userId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 删除过期的会话
     *
     * @param expiredBefore 过期时间
     * @param updatedAt 更新时间
     * @param updatedBy 更新人
     * @return 删除的会话数量
     */
    @Modifying
    @Query("UPDATE AuthSessionEntity s SET s.deleted = true, s.updatedAt = :updatedAt, s.updatedBy = :updatedBy WHERE s.accessTokenExpiresAt < :expiredBefore AND s.deleted = false")
    int softDeleteExpiredSessions(@Param("expiredBefore") LocalDateTime expiredBefore, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 统计用户活跃会话数量
     *
     * @param userId 用户ID
     * @return 活跃会话数量
     */
    @Query("SELECT COUNT(s) FROM AuthSessionEntity s WHERE s.userId = :userId AND s.status = 'ACTIVE' AND s.deleted = false")
    long countActiveSessionsByUserId(@Param("userId") Long userId);

    /**
     * 统计总会话数量
     *
     * @return 总会话数量
     */
    @Query("SELECT COUNT(s) FROM AuthSessionEntity s WHERE s.deleted = false")
    long countActiveSessions();

    /**
     * 更新会话状态
     *
     * @param sessionId 会话ID
     * @param status 新状态
     * @param updatedAt 更新时间
     * @param updatedBy 更新人
     * @return 更新数量
     */
    @Modifying
    @Query("UPDATE AuthSessionEntity s SET s.status = :status, s.updatedAt = :updatedAt, s.updatedBy = :updatedBy WHERE s.id = :sessionId")
    int updateStatusById(@Param("sessionId") Long sessionId, @Param("status") AuthSessionEntity.SessionStatus status, 
                       @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 更新最后活动时间
     *
     * @param sessionId 会话ID
     * @param lastActivityAt 最后活动时间
     * @param updatedAt 更新时间
     * @param updatedBy 更新人
     * @return 更新数量
     */
    @Modifying
    @Query("UPDATE AuthSessionEntity s SET s.lastActivityAt = :lastActivityAt, s.updatedAt = :updatedAt, s.updatedBy = :updatedBy WHERE s.id = :sessionId")
    int updateLastActivityById(@Param("sessionId") Long sessionId, @Param("lastActivityAt") LocalDateTime lastActivityAt,
                              @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 更新访问令牌
     *
     * @param sessionId 会话ID
     * @param accessToken 新访问令牌
     * @param accessTokenExpiresAt 访问令牌过期时间
     * @param updatedAt 更新时间
     * @param updatedBy 更新人
     * @return 更新数量
     */
    @Modifying
    @Query("UPDATE AuthSessionEntity s SET s.accessToken = :accessToken, s.accessTokenExpiresAt = :accessTokenExpiresAt, s.updatedAt = :updatedAt, s.updatedBy = :updatedBy WHERE s.id = :sessionId")
    int updateAccessTokenById(@Param("sessionId") Long sessionId, @Param("accessToken") String accessToken,
                             @Param("accessTokenExpiresAt") LocalDateTime accessTokenExpiresAt,
                             @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);

    /**
     * 更新刷新令牌
     *
     * @param sessionId 会话ID
     * @param refreshToken 新刷新令牌
     * @param refreshTokenExpiresAt 刷新令牌过期时间
     * @param updatedAt 更新时间
     * @param updatedBy 更新人
     * @return 更新数量
     */
    @Modifying
    @Query("UPDATE AuthSessionEntity s SET s.refreshToken = :refreshToken, s.refreshTokenExpiresAt = :refreshTokenExpiresAt, s.updatedAt = :updatedAt, s.updatedBy = :updatedBy WHERE s.id = :sessionId")
    int updateRefreshTokenById(@Param("sessionId") Long sessionId, @Param("refreshToken") String refreshToken,
                              @Param("refreshTokenExpiresAt") LocalDateTime refreshTokenExpiresAt,
                              @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") Long updatedBy);
}
