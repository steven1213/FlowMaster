package com.flowmaster.auth.domain.service;

import com.flowmaster.auth.domain.model.aggregate.AuthSession;
import com.flowmaster.auth.domain.model.valueobject.AccessToken;
import com.flowmaster.auth.domain.model.valueobject.Password;
import com.flowmaster.auth.domain.model.valueobject.RefreshToken;
import com.flowmaster.auth.domain.model.valueobject.Username;

/**
 * 认证领域服务
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
public interface AuthDomainService {

    /**
     * 验证用户凭据
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户ID，验证失败返回null
     */
    Long validateCredentials(Username username, Password password);

    /**
     * 生成访问令牌
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return 访问令牌
     */
    AccessToken generateAccessToken(Long userId, Username username);

    /**
     * 生成刷新令牌
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return 刷新令牌
     */
    RefreshToken generateRefreshToken(Long userId, Username username);

    /**
     * 验证访问令牌
     *
     * @param accessToken 访问令牌
     * @return 用户ID，验证失败返回null
     */
    Long validateAccessToken(AccessToken accessToken);

    /**
     * 验证刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 用户ID，验证失败返回null
     */
    Long validateRefreshToken(RefreshToken refreshToken);

    /**
     * 撤销访问令牌
     *
     * @param accessToken 访问令牌
     */
    void revokeAccessToken(AccessToken accessToken);

    /**
     * 撤销刷新令牌
     *
     * @param refreshToken 刷新令牌
     */
    void revokeRefreshToken(RefreshToken refreshToken);

    /**
     * 撤销用户的所有令牌
     *
     * @param userId 用户ID
     */
    void revokeAllUserTokens(Long userId);

    /**
     * 检查令牌是否在黑名单中
     *
     * @param token 令牌
     * @return 是否在黑名单中
     */
    boolean isTokenBlacklisted(String token);

    /**
     * 将令牌加入黑名单
     *
     * @param token 令牌
     */
    void blacklistToken(String token);

    /**
     * 加密密码
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    Password encryptPassword(Password password);

    /**
     * 验证密码
     *
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean matchesPassword(String rawPassword, String encodedPassword);
}
