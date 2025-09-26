package com.flowmaster.auth.application.service;

import com.flowmaster.common.response.PageResult;
import com.flowmaster.common.response.Result;
import com.flowmaster.auth.application.command.LoginCommand;
import com.flowmaster.auth.application.command.LogoutCommand;
import com.flowmaster.auth.application.command.RefreshTokenCommand;
import com.flowmaster.auth.application.dto.AuthResponseDTO;
import com.flowmaster.auth.application.dto.SessionInfoDTO;
import com.flowmaster.auth.domain.model.aggregate.AuthSession;
import com.flowmaster.auth.domain.model.valueobject.AccessToken;
import com.flowmaster.auth.domain.model.valueobject.Password;
import com.flowmaster.auth.domain.model.valueobject.RefreshToken;
import com.flowmaster.auth.domain.model.valueobject.Username;
import com.flowmaster.auth.domain.repository.AuthSessionRepository;
import com.flowmaster.auth.domain.service.AuthDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 认证应用服务
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthApplicationService {

    private final AuthDomainService authDomainService;
    private final AuthSessionRepository authSessionRepository;

    /**
     * 用户登录
     *
     * @param command 登录命令
     * @return 认证响应
     */
    public Result<AuthResponseDTO> login(LoginCommand command) {
        try {
            log.info("用户登录请求: username={}, clientIp={}", command.getUsername(), command.getClientIp());

            // 验证用户凭据
            Username username = Username.of(command.getUsername());
            Password password = Password.of(command.getPassword());
            
            Long userId = authDomainService.validateCredentials(username, password);
            if (userId == null) {
                log.warn("用户登录失败: username={}, reason=invalid_credentials", command.getUsername());
                return Result.fail("用户名或密码错误");
            }

            // 生成令牌
            AccessToken accessToken = authDomainService.generateAccessToken(userId, username);
            RefreshToken refreshToken = authDomainService.generateRefreshToken(userId, username);

            // 创建认证会话
            AuthSession session = AuthSession.create(
                userId, 
                username, 
                accessToken, 
                refreshToken, 
                command.getClientIp(), 
                command.getUserAgent()
            );

            // 保存会话
            AuthSession savedSession = authSessionRepository.save(session);

            // 构建响应
            AuthResponseDTO response = new AuthResponseDTO()
                .setAccessToken(accessToken.getJwt())
                .setRefreshToken(refreshToken.getJwt())
                .setTokenType("Bearer")
                .setExpiresIn(86400L) // 24小时
                .setRefreshExpiresIn(604800L) // 7天
                .setUserId(userId)
                .setUsername(username.getValue())
                .setSessionId(savedSession.getSessionId())
                .setCreatedAt(savedSession.getCreatedAt());

            log.info("用户登录成功: userId={}, username={}, sessionId={}", userId, username.getValue(), savedSession.getSessionId());
            return Result.success(response);

        } catch (IllegalArgumentException e) {
            log.warn("用户登录参数错误: username={}, error={}", command.getUsername(), e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("用户登录异常: username={}, error={}", command.getUsername(), e.getMessage(), e);
            return Result.fail("登录失败，请稍后重试");
        }
    }

    /**
     * 刷新令牌
     *
     * @param command 刷新令牌命令
     * @return 认证响应
     */
    public Result<AuthResponseDTO> refreshToken(RefreshTokenCommand command) {
        try {
            log.info("刷新令牌请求: clientIp={}", command.getClientIp());

            RefreshToken refreshToken = RefreshToken.of(command.getRefreshToken());
            
            // 验证刷新令牌
            Long userId = authDomainService.validateRefreshToken(refreshToken);
            if (userId == null) {
                log.warn("刷新令牌失败: reason=invalid_refresh_token");
                return Result.fail("刷新令牌无效或已过期");
            }

            // 查找会话
            Optional<AuthSession> sessionOpt = authSessionRepository.findByRefreshToken(refreshToken);
            if (sessionOpt.isEmpty()) {
                log.warn("刷新令牌失败: reason=session_not_found");
                return Result.fail("会话不存在");
            }

            AuthSession session = sessionOpt.get();
            if (!session.isValid()) {
                log.warn("刷新令牌失败: reason=session_invalid");
                return Result.fail("会话已失效");
            }

            // 生成新令牌
            AccessToken newAccessToken = authDomainService.generateAccessToken(userId, session.getUsername());
            RefreshToken newRefreshToken = authDomainService.generateRefreshToken(userId, session.getUsername());

            // 更新会话
            session.refreshTokens(newAccessToken, newRefreshToken);
            authSessionRepository.save(session);

            // 构建响应
            AuthResponseDTO response = new AuthResponseDTO()
                .setAccessToken(newAccessToken.getJwt())
                .setRefreshToken(newRefreshToken.getJwt())
                .setTokenType("Bearer")
                .setExpiresIn(86400L) // 24小时
                .setRefreshExpiresIn(604800L) // 7天
                .setUserId(userId)
                .setUsername(session.getUsername().getValue())
                .setSessionId(session.getSessionId())
                .setCreatedAt(session.getCreatedAt());

            log.info("刷新令牌成功: userId={}, sessionId={}", userId, session.getSessionId());
            return Result.success(response);

        } catch (IllegalArgumentException e) {
            log.warn("刷新令牌参数错误: error={}", e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("刷新令牌异常: error={}", e.getMessage(), e);
            return Result.fail("刷新令牌失败，请稍后重试");
        }
    }

    /**
     * 用户登出
     *
     * @param command 登出命令
     * @return 操作结果
     */
    public Result<Void> logout(LogoutCommand command) {
        try {
            log.info("用户登出请求: clientIp={}", command.getClientIp());

            AccessToken accessToken = AccessToken.of(command.getAccessToken());
            
            // 查找会话
            Optional<AuthSession> sessionOpt = authSessionRepository.findByAccessToken(accessToken);
            if (sessionOpt.isEmpty()) {
                log.warn("登出失败: reason=session_not_found");
                return Result.fail("会话不存在");
            }

            AuthSession session = sessionOpt.get();
            
            // 撤销会话
            session.revoke();
            authSessionRepository.save(session);

            // 将令牌加入黑名单
            authDomainService.blacklistToken(accessToken.getJwt());
            if (command.getRefreshToken() != null) {
                authDomainService.blacklistToken(command.getRefreshToken());
            }

            log.info("用户登出成功: userId={}, sessionId={}", session.getUserId(), session.getSessionId());
            return Result.success();

        } catch (IllegalArgumentException e) {
            log.warn("登出参数错误: error={}", e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("登出异常: error={}", e.getMessage(), e);
            return Result.fail("登出失败，请稍后重试");
        }
    }

    /**
     * 验证访问令牌
     *
     * @param accessToken 访问令牌
     * @return 用户ID
     */
    @Transactional(readOnly = true)
    public Result<Long> validateToken(String accessToken) {
        try {
            AccessToken token = AccessToken.of(accessToken);
            
            // 检查令牌是否在黑名单中
            if (authDomainService.isTokenBlacklisted(token.getJwt())) {
                log.warn("令牌验证失败: reason=token_blacklisted");
                return Result.fail("令牌已失效");
            }

            // 验证令牌
            Long userId = authDomainService.validateAccessToken(token);
            if (userId == null) {
                log.warn("令牌验证失败: reason=invalid_token");
                return Result.fail("令牌无效或已过期");
            }

            // 查找会话并更新活动时间
            Optional<AuthSession> sessionOpt = authSessionRepository.findByAccessToken(token);
            if (sessionOpt.isPresent()) {
                AuthSession session = sessionOpt.get();
                session.updateLastActivity();
                authSessionRepository.save(session);
            }

            return Result.success(userId);

        } catch (IllegalArgumentException e) {
            log.warn("令牌验证参数错误: error={}", e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("令牌验证异常: error={}", e.getMessage(), e);
            return Result.fail("令牌验证失败");
        }
    }

    /**
     * 获取用户会话列表
     *
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 会话列表
     */
    @Transactional(readOnly = true)
    public Result<PageResult<SessionInfoDTO>> getUserSessions(Long userId, int page, int size) {
        try {
            log.info("获取用户会话列表: userId={}, page={}, size={}", userId, page, size);

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastActivityAt"));
            Page<AuthSession> sessionPage = authSessionRepository.findByUserId(userId, pageable);

            List<SessionInfoDTO> sessions = sessionPage.getContent().stream()
                .map(this::toSessionInfoDTO)
                .collect(Collectors.toList());

            PageResult<SessionInfoDTO> result = PageResult.of(
                sessions,
                sessionPage.getTotalElements(),
                (long) sessionPage.getNumber(),
                (long) sessionPage.getSize()
            );

            return Result.success(result);

        } catch (Exception e) {
            log.error("获取用户会话列表异常: userId={}, error={}", userId, e.getMessage(), e);
            return Result.fail("获取会话列表失败");
        }
    }

    /**
     * 撤销用户所有会话
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    public Result<Void> revokeAllUserSessions(Long userId) {
        try {
            log.info("撤销用户所有会话: userId={}", userId);

            // 撤销所有会话
            authDomainService.revokeAllUserTokens(userId);
            
            // 删除所有会话
            authSessionRepository.deleteByUserId(userId);

            log.info("撤销用户所有会话成功: userId={}", userId);
            return Result.success();

        } catch (Exception e) {
            log.error("撤销用户所有会话异常: userId={}, error={}", userId, e.getMessage(), e);
            return Result.fail("撤销会话失败");
        }
    }

    /**
     * 清理过期会话
     *
     * @return 清理的会话数量
     */
    public Result<Integer> cleanupExpiredSessions() {
        try {
            log.info("开始清理过期会话");

            LocalDateTime expiredBefore = LocalDateTime.now().minusDays(7); // 清理7天前的过期会话
            int deletedCount = authSessionRepository.deleteExpiredSessions(expiredBefore);

            log.info("清理过期会话完成: deletedCount={}", deletedCount);
            return Result.success(deletedCount);

        } catch (Exception e) {
            log.error("清理过期会话异常: error={}", e.getMessage(), e);
            return Result.fail("清理过期会话失败");
        }
    }

    /**
     * 转换为会话信息DTO
     *
     * @param session 认证会话
     * @return 会话信息DTO
     */
    private SessionInfoDTO toSessionInfoDTO(AuthSession session) {
        return new SessionInfoDTO()
            .setSessionId(session.getSessionId())
            .setUserId(session.getUserId())
            .setUsername(session.getUsername().getValue())
            .setClientIp(session.getClientIp())
            .setUserAgent(session.getUserAgent())
            .setStatus(session.getStatus().name())
            .setStatusDescription(session.getStatus().getDescription())
            .setAccessTokenExpiresAt(session.getAccessTokenExpiresAt())
            .setRefreshTokenExpiresAt(session.getRefreshTokenExpiresAt())
            .setLastActivityAt(session.getLastActivityAt())
            .setCreatedAt(session.getCreatedAt())
            .setUpdatedAt(session.getUpdatedAt());
    }
}
