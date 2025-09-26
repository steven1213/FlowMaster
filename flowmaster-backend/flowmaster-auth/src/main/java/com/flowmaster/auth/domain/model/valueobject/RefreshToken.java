package com.flowmaster.auth.domain.model.valueobject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 刷新令牌值对象
 * 
 * @author FlowMaster Team
 * @since 1.0.0
 */
@Getter
@Slf4j
public class RefreshToken {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$");

    private final String value;

    private RefreshToken(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("刷新令牌不能为空");
        }
        if (!TOKEN_PATTERN.matcher(value.trim()).matches()) {
            throw new IllegalArgumentException("刷新令牌格式不正确");
        }
        this.value = value.trim();
    }

    /**
     * 创建刷新令牌
     *
     * @param value 令牌值
     * @return 刷新令牌
     */
    public static RefreshToken of(String value) {
        return new RefreshToken(value);
    }

    /**
     * 创建刷新令牌（从JWT字符串）
     *
     * @param jwt JWT字符串
     * @return 刷新令牌
     */
    public static RefreshToken fromJwt(String jwt) {
        return new RefreshToken(jwt);
    }

    /**
     * 获取JWT字符串
     *
     * @return JWT字符串
     */
    public String getJwt() {
        return this.value;
    }

    /**
     * 验证令牌是否有效
     *
     * @return 是否有效
     */
    public boolean isValid() {
        return value != null && !value.trim().isEmpty() && TOKEN_PATTERN.matcher(value).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "value='" + value.substring(0, Math.min(20, value.length())) + "..." +
                '}';
    }
}
