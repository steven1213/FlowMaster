package com.flowmaster.auth.infrastructure.repository;

import com.flowmaster.auth.domain.model.aggregate.AuthSession;
import com.flowmaster.auth.domain.model.valueobject.AccessToken;
import com.flowmaster.auth.domain.model.valueobject.RefreshToken;
import com.flowmaster.auth.domain.model.valueobject.Username;
import com.flowmaster.auth.domain.repository.AuthSessionRepository;
import com.flowmaster.auth.infrastructure.persistence.entity.AuthSessionEntity;
import com.flowmaster.auth.infrastructure.persistence.repository.AuthSessionJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 认证会话仓储实现
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthSessionRepositoryImpl implements AuthSessionRepository {

    private final AuthSessionJpaRepository authSessionJpaRepository;

    @Override
    public AuthSession save(AuthSession session) {
        try {
            log.debug("保存认证会话: sessionId={}, userId={}", session.getSessionId(), session.getUserId());
            
            AuthSessionEntity entity = AuthSessionEntity.fromDomain(session);
            AuthSessionEntity savedEntity = authSessionJpaRepository.save(entity);
            
            log.debug("认证会话保存成功: sessionId={}", savedEntity.getId());
            return savedEntity.toDomain();
        } catch (Exception e) {
            log.error("保存认证会话异常: sessionId={}, error={}", session.getSessionId(), e.getMessage(), e);
            throw new RuntimeException("保存认证会话失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthSession> findById(Long sessionId) {
        try {
            log.debug("根据ID查找认证会话: sessionId={}", sessionId);
            
            Optional<AuthSessionEntity> entityOpt = authSessionJpaRepository.findById(sessionId);
            if (entityOpt.isPresent()) {
                AuthSession session = entityOpt.get().toDomain();
                log.debug("认证会话查找成功: sessionId={}", sessionId);
                return Optional.of(session);
            } else {
                log.debug("认证会话不存在: sessionId={}", sessionId);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("根据ID查找认证会话异常: sessionId={}, error={}", sessionId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthSession> findByAccessToken(AccessToken accessToken) {
        try {
            log.debug("根据访问令牌查找认证会话");
            
            Optional<AuthSessionEntity> entityOpt = authSessionJpaRepository.findByAccessTokenAndDeletedFalse(accessToken.getJwt());
            if (entityOpt.isPresent()) {
                AuthSession session = entityOpt.get().toDomain();
                log.debug("根据访问令牌查找认证会话成功: sessionId={}", session.getSessionId());
                return Optional.of(session);
            } else {
                log.debug("根据访问令牌未找到认证会话");
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("根据访问令牌查找认证会话异常: error={}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthSession> findByRefreshToken(RefreshToken refreshToken) {
        try {
            log.debug("根据刷新令牌查找认证会话");
            
            Optional<AuthSessionEntity> entityOpt = authSessionJpaRepository.findByRefreshTokenAndDeletedFalse(refreshToken.getJwt());
            if (entityOpt.isPresent()) {
                AuthSession session = entityOpt.get().toDomain();
                log.debug("根据刷新令牌查找认证会话成功: sessionId={}", session.getSessionId());
                return Optional.of(session);
            } else {
                log.debug("根据刷新令牌未找到认证会话");
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("根据刷新令牌查找认证会话异常: error={}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthSession> findActiveSessionsByUserId(Long userId) {
        try {
            log.debug("查找用户活跃会话: userId={}", userId);
            
            List<AuthSessionEntity> entities = authSessionJpaRepository.findActiveSessionsByUserId(userId);
            List<AuthSession> sessions = entities.stream()
                    .map(AuthSessionEntity::toDomain)
                    .collect(Collectors.toList());
            
            log.debug("查找用户活跃会话成功: userId={}, count={}", userId, sessions.size());
            return sessions;
        } catch (Exception e) {
            log.error("查找用户活跃会话异常: userId={}, error={}", userId, e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthSession> findActiveSessionsByUsername(Username username) {
        try {
            log.debug("根据用户名查找活跃会话: username={}", username.getValue());
            
            List<AuthSessionEntity> entities = authSessionJpaRepository.findActiveSessionsByUsername(username.getValue());
            List<AuthSession> sessions = entities.stream()
                    .map(AuthSessionEntity::toDomain)
                    .collect(Collectors.toList());
            
            log.debug("根据用户名查找活跃会话成功: username={}, count={}", username.getValue(), sessions.size());
            return sessions;
        } catch (Exception e) {
            log.error("根据用户名查找活跃会话异常: username={}, error={}", username.getValue(), e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthSession> findByUserId(Long userId, Pageable pageable) {
        try {
            log.debug("分页查询用户会话: userId={}, page={}, size={}", userId, pageable.getPageNumber(), pageable.getPageSize());
            
            Page<AuthSessionEntity> entityPage = authSessionJpaRepository.findByUserIdAndDeletedFalse(userId, pageable);
            Page<AuthSession> sessionPage = entityPage.map(AuthSessionEntity::toDomain);
            
            log.debug("分页查询用户会话成功: userId={}, total={}", userId, sessionPage.getTotalElements());
            return sessionPage;
        } catch (Exception e) {
            log.error("分页查询用户会话异常: userId={}, error={}", userId, e.getMessage(), e);
            return Page.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthSession> findExpiredSessions(LocalDateTime expiredBefore) {
        try {
            log.debug("查找过期会话: expiredBefore={}", expiredBefore);
            
            List<AuthSessionEntity> entities = authSessionJpaRepository.findExpiredSessions(expiredBefore);
            List<AuthSession> sessions = entities.stream()
                    .map(AuthSessionEntity::toDomain)
                    .collect(Collectors.toList());
            
            log.debug("查找过期会话成功: count={}", sessions.size());
            return sessions;
        } catch (Exception e) {
            log.error("查找过期会话异常: error={}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void deleteById(Long sessionId) {
        try {
            log.debug("删除认证会话: sessionId={}", sessionId);
            
            LocalDateTime now = LocalDateTime.now();
            int deletedCount = authSessionJpaRepository.softDeleteById(sessionId, now, 1L); // TODO: 从认证上下文获取当前用户ID
            
            log.debug("认证会话删除成功: sessionId={}, deletedCount={}", sessionId, deletedCount);
        } catch (Exception e) {
            log.error("删除认证会话异常: sessionId={}, error={}", sessionId, e.getMessage(), e);
            throw new RuntimeException("删除认证会话失败", e);
        }
    }

    @Override
    public void deleteByUserId(Long userId) {
        try {
            log.debug("删除用户所有会话: userId={}", userId);
            
            LocalDateTime now = LocalDateTime.now();
            int deletedCount = authSessionJpaRepository.softDeleteByUserId(userId, now, 1L); // TODO: 从认证上下文获取当前用户ID
            
            log.debug("用户所有会话删除成功: userId={}, deletedCount={}", userId, deletedCount);
        } catch (Exception e) {
            log.error("删除用户所有会话异常: userId={}, error={}", userId, e.getMessage(), e);
            throw new RuntimeException("删除用户所有会话失败", e);
        }
    }

    @Override
    public int deleteExpiredSessions(LocalDateTime expiredBefore) {
        try {
            log.debug("删除过期会话: expiredBefore={}", expiredBefore);
            
            LocalDateTime now = LocalDateTime.now();
            int deletedCount = authSessionJpaRepository.softDeleteExpiredSessions(expiredBefore, now, 1L); // TODO: 从认证上下文获取当前用户ID
            
            log.debug("过期会话删除成功: deletedCount={}", deletedCount);
            return deletedCount;
        } catch (Exception e) {
            log.error("删除过期会话异常: error={}", e.getMessage(), e);
            throw new RuntimeException("删除过期会话失败", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveSessionsByUserId(Long userId) {
        try {
            log.debug("统计用户活跃会话数量: userId={}", userId);
            
            long count = authSessionJpaRepository.countActiveSessionsByUserId(userId);
            
            log.debug("用户活跃会话数量统计成功: userId={}, count={}", userId, count);
            return count;
        } catch (Exception e) {
            log.error("统计用户活跃会话数量异常: userId={}, error={}", userId, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        try {
            log.debug("统计总会话数量");
            
            long count = authSessionJpaRepository.countActiveSessions();
            
            log.debug("总会话数量统计成功: count={}", count);
            return count;
        } catch (Exception e) {
            log.error("统计总会话数量异常: error={}", e.getMessage(), e);
            return 0;
        }
    }
}
