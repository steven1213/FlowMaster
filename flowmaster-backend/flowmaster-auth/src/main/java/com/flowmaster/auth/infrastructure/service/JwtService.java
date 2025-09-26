package com.flowmaster.auth.infrastructure.service;

import com.flowmaster.auth.domain.model.valueobject.AccessToken;
import com.flowmaster.auth.domain.model.valueobject.RefreshToken;
import com.flowmaster.auth.domain.model.valueobject.Username;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT服务实现
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Service
@Slf4j
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final String issuer;
    private final String audience;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpiration,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.audience}") String audience,
            RedisTemplate<String, String> redisTemplate) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.issuer = issuer;
        this.audience = audience;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成访问令牌
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return 访问令牌
     */
    public AccessToken generateAccessToken(Long userId, Username username) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiration = now.plusSeconds(accessTokenExpiration / 1000);

            String token = Jwts.builder()
                    .setSubject(userId.toString())
                    .setIssuer(issuer)
                    .setAudience(audience)
                    .claim("username", username.getValue())
                    .claim("type", "access")
                    .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .compact();

            log.debug("生成访问令牌成功: userId={}, username={}", userId, username.getValue());
            return AccessToken.of(token);

        } catch (Exception e) {
            log.error("生成访问令牌失败: userId={}, username={}, error={}", userId, username.getValue(), e.getMessage(), e);
            throw new RuntimeException("生成访问令牌失败", e);
        }
    }

    /**
     * 生成刷新令牌
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return 刷新令牌
     */
    public RefreshToken generateRefreshToken(Long userId, Username username) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiration = now.plusSeconds(refreshTokenExpiration / 1000);

            String token = Jwts.builder()
                    .setSubject(userId.toString())
                    .setIssuer(issuer)
                    .setAudience(audience)
                    .claim("username", username.getValue())
                    .claim("type", "refresh")
                    .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .compact();

            log.debug("生成刷新令牌成功: userId={}, username={}", userId, username.getValue());
            return RefreshToken.of(token);

        } catch (Exception e) {
            log.error("生成刷新令牌失败: userId={}, username={}, error={}", userId, username.getValue(), e.getMessage(), e);
            throw new RuntimeException("生成刷新令牌失败", e);
        }
    }

    /**
     * 验证访问令牌
     *
     * @param accessToken 访问令牌
     * @return 用户ID
     */
    public Long validateAccessToken(AccessToken accessToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken.getJwt())
                    .getBody();

            // 验证令牌类型
            String type = claims.get("type", String.class);
            if (!"access".equals(type)) {
                log.warn("访问令牌类型不正确: type={}", type);
                return null;
            }

            // 验证发行者和受众
            if (!issuer.equals(claims.getIssuer()) || !audience.equals(claims.getAudience())) {
                log.warn("访问令牌发行者或受众不正确: issuer={}, audience={}", claims.getIssuer(), claims.getAudience());
                return null;
            }

            Long userId = Long.parseLong(claims.getSubject());
            log.debug("访问令牌验证成功: userId={}", userId);
            return userId;

        } catch (ExpiredJwtException e) {
            log.warn("访问令牌已过期: {}", e.getMessage());
            return null;
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的访问令牌: {}", e.getMessage());
            return null;
        } catch (MalformedJwtException e) {
            log.warn("访问令牌格式错误: {}", e.getMessage());
            return null;
        } catch (SignatureException e) {
            log.warn("访问令牌签名验证失败: {}", e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            log.warn("访问令牌参数错误: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("访问令牌验证异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 验证刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 用户ID
     */
    public Long validateRefreshToken(RefreshToken refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken.getJwt())
                    .getBody();

            // 验证令牌类型
            String type = claims.get("type", String.class);
            if (!"refresh".equals(type)) {
                log.warn("刷新令牌类型不正确: type={}", type);
                return null;
            }

            // 验证发行者和受众
            if (!issuer.equals(claims.getIssuer()) || !audience.equals(claims.getAudience())) {
                log.warn("刷新令牌发行者或受众不正确: issuer={}, audience={}", claims.getIssuer(), claims.getAudience());
                return null;
            }

            Long userId = Long.parseLong(claims.getSubject());
            log.debug("刷新令牌验证成功: userId={}", userId);
            return userId;

        } catch (ExpiredJwtException e) {
            log.warn("刷新令牌已过期: {}", e.getMessage());
            return null;
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的刷新令牌: {}", e.getMessage());
            return null;
        } catch (MalformedJwtException e) {
            log.warn("刷新令牌格式错误: {}", e.getMessage());
            return null;
        } catch (SignatureException e) {
            log.warn("刷新令牌签名验证失败: {}", e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            log.warn("刷新令牌参数错误: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("刷新令牌验证异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 检查令牌是否在黑名单中
     *
     * @param token 令牌
     * @return 是否在黑名单中
     */
    public boolean isTokenBlacklisted(String token) {
        try {
            String blacklistKey = "jwt:blacklist:" + token;
            Boolean exists = redisTemplate.hasKey(blacklistKey);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("检查令牌黑名单异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将令牌加入黑名单
     *
     * @param token 令牌
     */
    public void blacklistToken(String token) {
        try {
            String blacklistKey = "jwt:blacklist:" + token;
            
            // 计算令牌剩余过期时间
            long expirationTime = getTokenExpirationTime(token);
            long currentTime = System.currentTimeMillis();
            long ttl = Math.max(0, expirationTime - currentTime);
            
            // 将令牌加入黑名单，设置过期时间
            redisTemplate.opsForValue().set(blacklistKey, "1", ttl, TimeUnit.MILLISECONDS);
            
            log.debug("令牌已加入黑名单: ttl={}ms", ttl);
        } catch (Exception e) {
            log.error("将令牌加入黑名单异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取令牌过期时间
     *
     * @param token 令牌
     * @return 过期时间（毫秒）
     */
    private long getTokenExpirationTime(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().getTime();
        } catch (Exception e) {
            log.warn("获取令牌过期时间失败: {}", e.getMessage());
            return System.currentTimeMillis() + 3600000; // 默认1小时后过期
        }
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("username", String.class);
        } catch (Exception e) {
            log.warn("从令牌获取用户名失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            log.warn("从令牌获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }
}
