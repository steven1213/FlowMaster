package com.flowmaster.auth.infrastructure.service;

import com.flowmaster.auth.domain.model.valueobject.AccessToken;
import com.flowmaster.auth.domain.model.valueobject.Password;
import com.flowmaster.auth.domain.model.valueobject.RefreshToken;
import com.flowmaster.auth.domain.model.valueobject.Username;
import com.flowmaster.auth.domain.service.AuthDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 认证领域服务实现
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthDomainServiceImpl implements AuthDomainService {

    private final JwtService jwtService;
    private final PasswordService passwordService;
    private final UserServiceClient userServiceClient;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Long validateCredentials(Username username, Password password) {
        try {
            log.debug("验证用户凭据: username={}", username.getValue());
            
            // 使用用户服务客户端验证凭据
            Long userId = userServiceClient.validateUserPassword(username, password.getEncodedValue());
            
            if (userId != null) {
                log.debug("用户凭据验证成功: username={}, userId={}", username.getValue(), userId);
            } else {
                log.warn("用户凭据验证失败: username={}", username.getValue());
            }
            
            return userId;
        } catch (Exception e) {
            log.error("验证用户凭据异常: username={}, error={}", username.getValue(), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public AccessToken generateAccessToken(Long userId, Username username) {
        try {
            log.debug("生成访问令牌: userId={}, username={}", userId, username.getValue());
            return jwtService.generateAccessToken(userId, username);
        } catch (Exception e) {
            log.error("生成访问令牌异常: userId={}, username={}, error={}", userId, username.getValue(), e.getMessage(), e);
            throw new RuntimeException("生成访问令牌失败", e);
        }
    }

    @Override
    public RefreshToken generateRefreshToken(Long userId, Username username) {
        try {
            log.debug("生成刷新令牌: userId={}, username={}", userId, username.getValue());
            return jwtService.generateRefreshToken(userId, username);
        } catch (Exception e) {
            log.error("生成刷新令牌异常: userId={}, username={}, error={}", userId, username.getValue(), e.getMessage(), e);
            throw new RuntimeException("生成刷新令牌失败", e);
        }
    }

    @Override
    public Long validateAccessToken(AccessToken accessToken) {
        try {
            log.debug("验证访问令牌");
            
            // 检查令牌是否在黑名单中
            if (isTokenBlacklisted(accessToken.getJwt())) {
                log.warn("访问令牌在黑名单中");
                return null;
            }
            
            return jwtService.validateAccessToken(accessToken);
        } catch (Exception e) {
            log.error("验证访问令牌异常: error={}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Long validateRefreshToken(RefreshToken refreshToken) {
        try {
            log.debug("验证刷新令牌");
            
            // 检查令牌是否在黑名单中
            if (isTokenBlacklisted(refreshToken.getJwt())) {
                log.warn("刷新令牌在黑名单中");
                return null;
            }
            
            return jwtService.validateRefreshToken(refreshToken);
        } catch (Exception e) {
            log.error("验证刷新令牌异常: error={}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void revokeAccessToken(AccessToken accessToken) {
        try {
            log.debug("撤销访问令牌");
            blacklistToken(accessToken.getJwt());
        } catch (Exception e) {
            log.error("撤销访问令牌异常: error={}", e.getMessage(), e);
        }
    }

    @Override
    public void revokeRefreshToken(RefreshToken refreshToken) {
        try {
            log.debug("撤销刷新令牌");
            blacklistToken(refreshToken.getJwt());
        } catch (Exception e) {
            log.error("撤销刷新令牌异常: error={}", e.getMessage(), e);
        }
    }

    @Override
    public void revokeAllUserTokens(Long userId) {
        try {
            log.debug("撤销用户所有令牌: userId={}", userId);
            
            // 使用Redis模式匹配查找用户的所有令牌
            String pattern = "jwt:user:" + userId + ":*";
            Set<String> keys = redisTemplate.keys(pattern);
            
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.debug("撤销用户令牌成功: userId={}, count={}", userId, keys.size());
            }
            
        } catch (Exception e) {
            log.error("撤销用户所有令牌异常: userId={}, error={}", userId, e.getMessage(), e);
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        try {
            return jwtService.isTokenBlacklisted(token);
        } catch (Exception e) {
            log.error("检查令牌黑名单异常: error={}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void blacklistToken(String token) {
        try {
            log.debug("将令牌加入黑名单");
            jwtService.blacklistToken(token);
        } catch (Exception e) {
            log.error("将令牌加入黑名单异常: error={}", e.getMessage(), e);
        }
    }

    @Override
    public Password encryptPassword(Password password) {
        try {
            log.debug("加密密码");
            return passwordService.encryptPassword(password);
        } catch (Exception e) {
            log.error("加密密码异常: error={}", e.getMessage(), e);
            throw new RuntimeException("加密密码失败", e);
        }
    }

    @Override
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        try {
            log.debug("验证密码");
            return passwordService.matchesPassword(rawPassword, encodedPassword);
        } catch (Exception e) {
            log.error("验证密码异常: error={}", e.getMessage(), e);
            return false;
        }
    }
}
